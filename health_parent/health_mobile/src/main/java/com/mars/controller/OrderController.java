package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.constant.RedisMessageConstant;
import com.mars.entity.Result;
import com.mars.pojo.Order;
import com.mars.service.OrderService;
import com.mars.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * ClassName:OrderController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/2/3 11:53
 * @Author:Mars
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        /*
         *思路：
         * 1、从Redis中获取保存的验证码
         * 2、将用户输入的验证码与redis中保存的验证码进行比对
         * 3、如果比对成功，调用服务完成预约业务处理
         * 4、如果比对失败，返回结果给页面
         */
        String validateCodeInRedis = jedisPool.getResource().get(map.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);

        if (validateCodeInRedis != null && map.get("validateCode") != null && validateCodeInRedis.equals(map.get("validateCode"))) {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);// 设置预约类型

            Result result = null;
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.isFlag()) {
                // 预约成功则发送短信通知
                try {
                    SMSUtils.sendSms("1294824", "+86", map.get("telephone").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}

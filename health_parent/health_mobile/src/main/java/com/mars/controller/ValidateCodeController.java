package com.mars.controller;

import com.mars.constant.MessageConstant;
import com.mars.constant.RedisMessageConstant;
import com.mars.entity.Result;
import com.mars.utils.SMSUtils;
import com.mars.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * ClassName:ValidateCodeController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/2/3 10:55
 * @Author:Mars
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        // 发送验证码
        try {
            SMSUtils.sendCode(telephone, "+86", validateCode.toString(), 1294819);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        // 将验证码缓存到redis（5分钟）
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 600, validateCode.toString());

        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}

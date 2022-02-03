package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.mars.constant.MessageConstant;
import com.mars.constant.RedisMessageConstant;
import com.mars.entity.Result;
import com.mars.pojo.Member;
import com.mars.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * ClassName:LoginController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/2/3 19:56
 * @Author:Mars
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map) {
        // 校验验证码是否输入正确
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // 获取Redis中保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
            // 如果输入验证码输入正确则判断当前用户是否是会员，如果不是则自动添加
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                // 自动注册
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            // 登录成功
            // 写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);

            // 将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}

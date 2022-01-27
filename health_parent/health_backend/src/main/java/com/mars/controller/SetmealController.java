package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.constant.RedisConstant;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.entity.Result;
import com.mars.pojo.Setmeal;
import com.mars.service.SetmealService;
import com.mars.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * ClassName:SetmealController
 * Package:com.mars.controller
 * Description:体检套餐管理
 *
 * @Date:2022/1/27 12:18
 * @Author:Mars
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    // 使用JedisPool操作Redis数据库
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    /**
     * 文件上传，需要在springmvc.xml中配置文件上传组件
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {

        String originalFilename = imgFile.getOriginalFilename();//获取文件原始名称
        int index = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(index - 1); // .jpg
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            // 使用七牛云的工具类上传图片文件
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){

        try {
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }
}

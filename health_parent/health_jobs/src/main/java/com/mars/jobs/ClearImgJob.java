package com.mars.jobs;

import com.mars.constant.RedisConstant;
import com.mars.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * ClassName:ClearImgJob
 * Package:com.mars.jobs
 * Description:
 *
 * @Date:2022/1/27 22:05
 * @Author:Mars
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                // 删除七牛云上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                // 删除Redis数据库中的文件名
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
            }
        }
    }
}

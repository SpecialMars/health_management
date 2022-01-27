package com.mars.constant;

/**
 * ClassName:RedisConstant
 * Package:com.mars.constant
 * Description:
 *
 * @Date:2022/1/27 21:01
 * @Author:Mars
 */
public class RedisConstant {
    // 思路就是大集合减小集合差集就是保存在数据库中的图片
    //套餐图片所有图片名称
    public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
    //套餐图片保存在数据库中的图片名称
    public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
}

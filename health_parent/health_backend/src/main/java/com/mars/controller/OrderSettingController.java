package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.entity.Result;
import com.mars.pojo.OrderSetting;
import com.mars.service.OrderSettingService;
import com.mars.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName:OrderSettingController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/1/28 20:14
 * @Author:Mars
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {

        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            // 将list中的数据类型转换为List<OrderSetting>
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                String orderTime = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderTime), Integer.parseInt(number));
                data.add(orderSetting);
            }
            orderSettingService.add(data);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) { // 参数格式为 year-month

        try {
            // 需要返回的数据格式：{ date: 1, number: 120, reservations: 1 }因此使用List<Map>作为返回对象
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            // 需要返回的数据格式：{ date: 1, number: 120, reservations: 1 }因此使用List<Map>作为返回对象
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}

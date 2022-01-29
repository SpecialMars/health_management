package com.mars.service;

import com.mars.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * ClassName:OrderSettingService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/1/28 20:47
 * @Author:Mars
 */
public interface OrderSettingService {

    void add(List<OrderSetting> list);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}

package com.mars.dao;

import com.mars.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName:OrderSettingDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/1/28 20:50
 * @Author:Mars
 */
public interface OrderSettingDao {

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}

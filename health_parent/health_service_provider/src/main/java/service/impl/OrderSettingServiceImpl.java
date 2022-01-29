package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.OrderSettingDao;
import com.mars.pojo.OrderSetting;
import com.mars.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ClassName:OrderSettingServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/1/28 20:48
 * @Author:Mars
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {

        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //先判断orderSettingDate是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    // 如果countByOrderDate > 0 说明当前预约日期已存在，执行修改方法
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据月份查询对应的预约设置数据
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("date", orderSetting.getOrderDate().getDate());
            resultMap.put("number", orderSetting.getNumber());
            resultMap.put("reservations", orderSetting.getReservations());
            result.add(resultMap);
        }
        return result;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        // 获取当前日期是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count > 0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}

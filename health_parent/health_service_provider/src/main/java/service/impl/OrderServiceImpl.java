package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.constant.MessageConstant;
import com.mars.dao.MemberDao;
import com.mars.dao.OrderDao;
import com.mars.dao.OrderSettingDao;
import com.mars.entity.Result;
import com.mars.pojo.Member;
import com.mars.pojo.Order;
import com.mars.pojo.OrderSetting;
import com.mars.service.OrderService;
import com.mars.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName:OrderServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/2/3 12:18
 * @Author:Mars
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        // 体检预约方法处理逻辑比较复杂，需要进行如下业务处理：
        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();// 可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            // 预约已满，无法预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = map.get("telephone").toString();
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            // 判断是否存在重复预约
            Integer memberId = member.getId(); // 会员id
            Date order_date = DateUtils.parseString2Date(orderDate);// 预约日期
            String setmealId = (String) map.get("setmealId");// 套餐id
            Order order = new Order(memberId, order_date, Integer.parseInt(setmealId));
            // 根据条件查询
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            // 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        // 5、预约成功，更新当日的已预约人数
        Order order = new Order(member.getId(), date, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            try {
                map.put("orderDate", DateUtils.parseDate2String(orderDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}

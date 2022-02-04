package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.MemberDao;
import com.mars.dao.OrderDao;
import com.mars.service.ReportService;
import com.mars.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:ReportServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/2/4 22:15
 * @Author:Mars
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 获得运营统计数据
     * Map数据格式：
     * todayNewMember -> number
     * totalMember -> number
     * thisWeekNewMember -> number
     * thisMonthNewMember -> number
     * todayOrderNumber -> number
     * todayVisitsNumber -> number
     * thisWeekOrderNumber -> number
     * thisWeekVisitsNumber -> number
     * thisMonthOrderNumber -> number
     * thisMonthVisitsNumber -> number
     * hotSetmeals -> List<Setmeal>
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String, Object> map = new HashMap<>();

        // 报表日期
        String today = DateUtils.parseDate2String(new Date());

        // 本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 本周新增会员数
        // 本周一的日期
        String thisMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisMonday);

        // 本月新增会员数
        String thisFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(thisFirstDay);


        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(thisFirstDay);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(thisFirstDay);

        //热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        // 添加数据
        map.put("reportDate", today);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrderNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);
        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        map.put("hotSetmeal", hotSetmeal);

        return map;
    }
}

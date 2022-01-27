package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mars.constant.RedisConstant;
import com.mars.dao.SetmealDao;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.Setmeal;
import com.mars.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:SetmealServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/1/27 14:01
 * @Author:Mars
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 向t_setmeal表中添加套餐
        setmealDao.add(setmeal);

        if(checkgroupIds != null && checkgroupIds.length > 0){
            // 绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmealId", setmealId);
            map.put("checkgroupId", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        String queryString = queryPageBean.getQueryString();
        Page<Setmeal> page = setmealDao.findPage(queryString);

        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total, rows);
    }
}

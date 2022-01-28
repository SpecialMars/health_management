package com.mars.service;

import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.Setmeal;

import java.util.List;

/**
 * ClassName:SetmealService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/1/27 14:00
 * @Author:Mars
 */
public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    void deleteById(Integer id);
}

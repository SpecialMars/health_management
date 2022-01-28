package com.mars.dao;

import com.github.pagehelper.Page;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * ClassName:SetmealDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/1/27 14:02
 * @Author:Mars
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void setSetmealAndCheckGroups(Map<String, Integer> map);

    void deleteCheckGroupBySetemalId(Integer id);

    void deleteById(Integer id);
}

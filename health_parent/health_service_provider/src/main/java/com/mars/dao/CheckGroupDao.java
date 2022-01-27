package com.mars.dao;

import com.github.pagehelper.Page;
import com.mars.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * ClassName:CheckGroupDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/1/26 20:16
 * @Author:Mars
 */
public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public Page<CheckGroup> selectByCondition(String queryString);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup);

    public void deleteAssociation(Integer checkGroupId);

    public void setCheckGroupAndCheckItem(Map<String, Integer> map);

    public void deleteById(Integer id);

    public void deleteCheckGroupAndCheckItemById(Integer id);

    public List<CheckGroup> findAll();
}

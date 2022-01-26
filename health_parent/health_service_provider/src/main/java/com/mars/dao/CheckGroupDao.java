package com.mars.dao;

import com.mars.pojo.CheckGroup;

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

    public void setCheckGroupAndCheckItem(Map<String, Integer> map);
}

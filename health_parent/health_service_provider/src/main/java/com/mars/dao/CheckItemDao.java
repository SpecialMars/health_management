package com.mars.dao;

import com.github.pagehelper.Page;
import com.mars.pojo.CheckItem;

import java.util.List;

/**
 * ClassName:CheckitemDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/1/24 10:42
 * @Author:Mars
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public void deleteById(Integer id);

    public long findCountByCheckItemId(Integer checkItemId);

    public CheckItem findById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}

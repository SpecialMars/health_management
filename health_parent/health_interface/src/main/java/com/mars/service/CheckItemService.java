package com.mars.service;

import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.CheckItem;

import java.util.List;

/**
 * ClassName:CheckItemService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/1/24 12:50
 * @Author:Mars
 */
public interface CheckItemService {

    public void add(CheckItem checkItem);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public CheckItem findById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}

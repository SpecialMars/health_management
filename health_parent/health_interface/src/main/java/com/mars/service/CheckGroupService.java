package com.mars.service;

import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.CheckGroup;

import java.util.List;

/**
 * ClassName:CheckGroupService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/1/26 20:09
 * @Author:Mars
 */
public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup,Integer[] checkitemIds);

    public void deleteById(Integer id);

    public List<CheckGroup> findAll();
}

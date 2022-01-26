package com.mars.service;

import com.mars.pojo.CheckGroup;

/**
 * ClassName:CheckGroupService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/1/26 20:09
 * @Author:Mars
 */
public interface CheckGroupService {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);
}

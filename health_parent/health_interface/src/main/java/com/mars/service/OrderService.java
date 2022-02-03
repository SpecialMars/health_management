package com.mars.service;

import com.mars.entity.Result;

import java.util.Map;

/**
 * ClassName:OrderService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/2/3 12:05
 * @Author:Mars
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map findById(Integer id);
}

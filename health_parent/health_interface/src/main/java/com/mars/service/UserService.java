package com.mars.service;

import com.mars.pojo.User;

/**
 * ClassName:UserService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/2/4 11:28
 * @Author:Mars
 */
public interface UserService {
    User findByUsername(String username);
}

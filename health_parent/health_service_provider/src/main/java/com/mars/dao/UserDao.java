package com.mars.dao;

import com.mars.pojo.User;

/**
 * ClassName:UserDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/2/4 11:54
 * @Author:Mars
 */
public interface UserDao {
    User findByUsername(String username);
}

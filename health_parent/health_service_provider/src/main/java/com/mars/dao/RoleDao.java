package com.mars.dao;

import com.mars.pojo.Role;

import java.util.Set;

/**
 * ClassName:RoleDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/2/4 12:05
 * @Author:Mars
 */
public interface RoleDao {
    Set<Role> findByUserId(Integer id);
}

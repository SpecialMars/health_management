package com.mars.dao;

import com.mars.pojo.Permission;

import java.util.Set;

/**
 * ClassName:PermissionDao
 * Package:com.mars.dao
 * Description:
 *
 * @Date:2022/2/4 12:06
 * @Author:Mars
 */
public interface PermissionDao {
    Set<Permission> findByRoleId(Integer id);
}

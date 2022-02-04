package com.mars.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.pojo.Permission;
import com.mars.pojo.Role;
import com.mars.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * ClassName:SpringSecurityUserService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/2/4 11:20
 * @Author:Mars
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    // 通过dubbo远程调用服务提供方获取数据库中的用户信息
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询数据库获取用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中获取用户
        User user = userService.findByUsername(username);
        if (user == null) {
            // 如果用户不存在
            return null;
        }

        List<GrantedAuthority> list = new LinkedList<>();
        // 用户存在则获取用户角色信息
        Set<Role> roles = user.getRoles();
        // 遍历用户角色信息，为用户授予角色
        for (Role role : roles) {
            // 授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            // 通过角色获取权限信息
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                // 授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);

        return securityUser;
    }
}

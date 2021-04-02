package com.shiro.service;


import com.shiro.utils.Response;

public interface HomeService {

    /**
     * 获取首页数据（用户名称、权限菜单）
     * @param userId
     * @return
     */
    Response getHome(String userId);
}

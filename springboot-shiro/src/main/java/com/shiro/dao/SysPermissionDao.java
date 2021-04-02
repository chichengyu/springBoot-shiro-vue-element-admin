package com.shiro.dao;

import com.shiro.pojo.SysPermission;

import java.util.List;

public interface SysPermissionDao {
    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String permissionId);

    /**
     * 查询所有权限（表格展示）
     * @return
     */
    List<SysPermission> selectAll();

    /**
     * 通过用户id查询用户拥有的权限
     * @param userId
     * @return
     */
    List<SysPermission> getPermissionByUserId(String userId);

    /**
     * 通过 id 查询是否有子级
     * @param id
     * @return
     */
    int selectChild(String id);

    /**
     * 更新权限菜单数据
     * @param permission
     * @return
     */
    int updateByPrimaryKeySelective(SysPermission permission);
}
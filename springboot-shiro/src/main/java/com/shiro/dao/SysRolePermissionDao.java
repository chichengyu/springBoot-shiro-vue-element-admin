package com.shiro.dao;

import com.shiro.pojo.SysRolePermission;
import com.shiro.vo.req.RolePermissionOperationReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRolePermissionDao {
    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    /**
     * 根据角色 id 删除多条角色 id 和菜单权限 id 关联数据
     */
    int removeByRoleId(String roleId);

    /**
     * 批量插入角色和菜单权限关联
     */
    int batchRolePermission(List<SysRolePermission> list);

    /**
     * 通过权限 id 查询所有的角色 id
     * @param permissionId
     * @return
     */
    List<String> getRoleIdsByPermissionId(String permissionId);

    /**
     * 通过权限id删除相关角色和该菜单权限的关联表信息
     * @param permissionId
     * @return
     */
    int removeByPermissionId(String permissionId);

    /**
     * 通过角色id查询拥有的权限id
     * @param roleId
     * @return
     */
    List<String> getPermissionIdsByRoleId(String roleId);
}
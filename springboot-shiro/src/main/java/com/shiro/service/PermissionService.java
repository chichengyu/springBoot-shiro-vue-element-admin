package com.shiro.service;

import com.shiro.pojo.SysPermission;
import com.shiro.utils.Response;
import com.shiro.vo.req.PermissionAddReqVo;
import com.shiro.vo.req.PermissionUpdateReqVo;
import com.shiro.vo.resp.PermissionRespNodeTreeVo;
import com.shiro.vo.resp.PermissionRespNodeVo;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    /**
     * 用户id查询用户拥有的权限菜单列表
     * @param userId
     * @return
     */
    List<PermissionRespNodeVo> permissionTreeListByUserId(String userId);

    /**查询所有权限列表（表格展示）
     * 树形表格结果数据组装
     * @return
     */
    List<SysPermission> selectAll();

    /**
     * 添加权限的上级选择权限树结构展示（递归）
     * @return
     */
    Response<List<PermissionRespNodeTreeVo>> selectAllMenuByTree();

    /**
     * 新增权限
     * @param permissionAddReqVO
     * @return
     */
    Response addPermission(PermissionAddReqVo permissionAddReqVO);

    /**
     * 获取所有权限（包括按钮），角色添加/编辑/分配权限时用到的结果数据
     * @return
     */
    List<PermissionRespNodeVo> selectAllTree();

    /**
     * 更新权限
     * @param permissionUpdateReqVo
     * @return
     */
    Response<String> updatePermission(PermissionUpdateReqVo permissionUpdateReqVo);

    /**
     * 删除菜单权限
     * @param permissionId
     * @return
     */
    Response<String> deletedPermission(String permissionId);
}

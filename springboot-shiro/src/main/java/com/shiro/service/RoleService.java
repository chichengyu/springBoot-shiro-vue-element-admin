package com.shiro.service;

import com.shiro.pojo.SysRole;
import com.shiro.utils.Response;
import com.shiro.vo.req.RolePageReqVo;
import com.shiro.vo.req.RoleReqVo;
import com.shiro.vo.req.RoleUpdateReqVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.RoleDetailInfoRespVo;

import java.util.List;
import java.util.Set;

public interface RoleService {

    /**
     * 角色管理分页数据
     * @return
     */
    Response<PageVo> pageInfoRoles(RolePageReqVo rolePageReqVo);

    /**
     * 新增角色
     * @param roleReqVo
     * @return
     */
    Response<String> createRole(RoleReqVo roleReqVo);

    /**
     * 查询所有权限
     * @return
     */
    List<SysRole> selectAll();

    /**
     * 获取角色详情数据
     * @param roleId
     * @return
     */
    Response<Set<String>> detailInfo(String roleId);

    /**
     * 更新角色信息
     * @param roleUpdateReqVo
     * @return
     */
    Response<String> updateRole(RoleUpdateReqVo roleUpdateReqVo);

    /**
     * 删除角色信息
     * @param roleId
     * @return
     */
    Response<String> deletedRole(String roleId);

    /**
     * 更新角色状态
     * @param roleId
     * @param status
     * @return
     */
    Response<String> updateRoleStatus(String roleId, Integer status);
}

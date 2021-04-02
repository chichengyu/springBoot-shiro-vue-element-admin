package com.shiro.dao;

import com.shiro.pojo.SysRole;
import com.shiro.vo.req.RolePageReqVo;

import java.util.List;

public interface SysRoleDao {
    int insert(SysRole record);

    int insertSelective(SysRole record);

    /**
     * 角色管理分页数据查询
     * @param rolePageReqVo
     * @return
     */
    List<SysRole> selectAll(RolePageReqVo rolePageReqVo);

    /**
     * 根据主键 id 查询
     * @param roleId
     * @return
     */
    SysRole selectByPrimaryKey(String roleId);

    /**
     * 更新角色信息
     * @param sysRole
     * @return
     */
    int updateByPrimaryKeySelective(SysRole sysRole);
}
package com.shiro.dao;

import com.shiro.pojo.SysUser;
import com.shiro.vo.req.UserPageReqVo;
import com.shiro.vo.resp.UserTableRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserDao {
    int insert(SysUser record);

    int insertSelective(SysUser record);

    /**
     * 根据 username 用户名查询
     * @param username
     * @return
     */
    SysUser findByUsername(@Param("username") String username);

    /**
     * 根据 id 主键查询
     * @param userId
     * @return
     */
    SysUser selectByPrimaryKey(String userId);

    /**
     * 分页查询用户（包括搜索条件）
     * @param userPageReqVo
     * @return
     */
    List<UserTableRespVo> selectAll(UserPageReqVo userPageReqVo);

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    int updateSelective(SysUser sysUser);

    /**
     * 批量/删除用户接口
     * @param sysUser
     * @param list
     * @return
     */
    int deletedUsers(@Param("sysUser") SysUser sysUser, @Param("list") List<String> list);

    /**
     * 通过部门id集合统计用户
     * @param deptIds
     * @return
     */
    int selectUserInfoByDeptIds(List<String> deptIds);
}
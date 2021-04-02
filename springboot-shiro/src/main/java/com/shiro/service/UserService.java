package com.shiro.service;

import com.shiro.pojo.SysUser;
import com.shiro.utils.Response;
import com.shiro.vo.req.LoginReqVo;
import com.shiro.vo.req.UserAddReqVo;
import com.shiro.vo.req.UserOwnRoleReqVo;
import com.shiro.vo.req.UserPageReqVo;
import com.shiro.vo.req.UserUpdateDetailInfoReqVo;
import com.shiro.vo.req.UserUpdatePasswordReqVo;
import com.shiro.vo.req.UserUpdateReqVo;
import com.shiro.vo.resp.LoginRespVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.UserOwnRoleRespVo;
import com.shiro.vo.resp.UserRespVo;
import com.shiro.vo.resp.UserTableRespVo;

import java.util.List;

public interface UserService {

    /**
     * 登陆
     * @param loginReqVo
     * @return
     */
    Response<LoginRespVo> login(LoginReqVo loginReqVo);

    /**
     * 退出登陆
     * @param accessToken
     * @return
     */
    Response<String> logout(String accessToken);

    /**
     * 分页查询用户（包括搜索条件）
     * @param userPageReqVo
     * @return
     */
    Response<PageVo<UserTableRespVo>> pageInfo(UserPageReqVo userPageReqVo);

    /**
     * 新增用户
     * @param userAddReqVo
     * @return
     */
    Response<String> addUser(UserAddReqVo userAddReqVo);

    /**
     * 查询用户拥有的角色数据接口
     * @param userId
     * @return
     */
    Response<UserOwnRoleRespVo> getUserOwnRole(String userId);

    /**
     * 更新用户角色
     * @param vo
     * @return
     */
    Response<String> setUserOwnRole(UserOwnRoleReqVo vo);

    /**
     * 更新用户信息
     * @param userUpdateReqVo
     * @param operationId 操作人
     * @return
     */
    Response<String> updateUserInfo(UserUpdateReqVo userUpdateReqVo, String operationId);

    /**
     * 批量/删除用户接口
     * @param list
     * @param operationId 操作人
     * @return
     */
    Response<String> deletedUsers(List<String> list, String operationId);

    /**
     * 获取个人资料编辑信息
     * @param userId
     * @return
     */
    Response<UserRespVo> detailInfo(String userId);

    /**
     * 保存个人信息接口
     * @param updateDetailInfoReqVo
     * @param userId
     * @return
     */
    Response<String> userUpdateDetailInfo(UserUpdateDetailInfoReqVo updateDetailInfoReqVo, String userId);

    /**
     * 修改个人密码
     * @param userUpdatePasswordReqVo
     * @param accessToken
     * @return
     */
    Response<String> userUpdatePassword(UserUpdatePasswordReqVo userUpdatePasswordReqVo, String accessToken);

    /**
     * 重置用户密码
     * @param userId
     * @return
     */
    Response<String> resetUpdatePassword(String userId);
}

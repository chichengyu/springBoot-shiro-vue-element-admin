package com.shiro.controller;

import com.shiro.aop.annotation.MyLog;
import com.shiro.constarts.Constant;
import com.shiro.pojo.SysUser;
import com.shiro.service.UserService;
import com.shiro.utils.JwtTokenUtil;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(value = "用户登陆/登出",tags = "用户登陆/登出相关的接口")
@RestController
@RequestMapping("/api")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param loginReqVo
     * @return
     */
    @ApiOperation(value = "用户登陆",notes = "用户登陆的接口")
    @PostMapping("/login")
    public Response<LoginRespVo> login(@RequestBody @Valid LoginReqVo loginReqVo){
        return userService.login(loginReqVo);
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @ApiOperation(value = "用户登出",notes = "用户登出的接口")
    @GetMapping("/logout")
    public Response<String> logout(HttpServletRequest request){
        String accessToken = null;
        //String refreshToken = null;
        // 退出时，不管成功还是失败都要退出
        try {
            accessToken = request.getHeader(Constant.ACCESS_TOKEN);
            //refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
            return userService.logout(accessToken);
        } catch (Exception e) {
            logger.error("【logout error】，{}",e);
        }
        return Response.success();
    }

    @MyLog(title = "组织管理-用户管理",action = "分页查询用户接口")
    @RequiresPermissions("sys:user:list")
    @ApiOperation(value = "分页查询用户",notes = "分页查询用户接口")
    @PostMapping("/users")
    public Response<PageVo<UserTableRespVo>> pageInfo(@RequestBody @Valid UserPageReqVo userPageReqVo){
        return userService.pageInfo(userPageReqVo);
    }

    @ApiOperation(value = "新增用户",notes = "新增用户接口")
    @RequiresPermissions("sys:user:add")
    @MyLog(title = "组织管理-用户管理",action = "新增用户接口")
    @PostMapping("/user")
    public Response<String> addUser(@RequestBody @Valid UserAddReqVo userAddReqVo){
        return userService.addUser(userAddReqVo);
    }

    @MyLog(title = "组织管理-用户管理",action = "查询用户拥有的角色数据接口")
    @ApiOperation(value = "查询用户拥有的角色数据接口",notes = "查询用户拥有的角色数据接口")
    @GetMapping("/user/roles/{userId}")
    public Response<UserOwnRoleRespVo> getUserOwnRole(@PathVariable("userId")String userId){
        return userService.getUserOwnRole(userId);
    }

    @ApiOperation(value = "更新用户角色",notes = "保存用户拥有的角色信息接口")
    @RequiresPermissions("sys:user:role:update")
    @MyLog(title = "组织管理-用户管理",action = "保存用户拥有的角色信息接口")
    @PutMapping("/user/roles")
    public Response<String> saveUserOwnRole(@RequestBody @Valid UserOwnRoleReqVo vo){
        return userService.setUserOwnRole(vo);
    }

    @ApiOperation(value = "重新用户密码",notes = "重新用户密码接口")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "form")
    @RequiresPermissions("sys:user:role:update")
    @MyLog(title = "组织管理-重置用户密码",action = "重置用户密码接口")
    @GetMapping("/user/password/{id}")
    public Response<String> resetUpdatePassword(@PathVariable("id") String userId){
        return userService.resetUpdatePassword(userId);
    }

    /*@MyLog(title = "组织管理-用户管理",action = "jwt token 刷新接口")
    @ApiOperation(value = "jwt token 刷新接口",notes = "jwt token 刷新接口")
    @GetMapping("/user/token")
    public Response<String> refreshToken(HttpServletRequest request){
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        return userService.refreshToken(refreshToken);
    }*/

    @ApiOperation(value = "更新用户",notes = "更新用户接口")
    @RequiresPermissions("sys:user:update")
    @MyLog(title = "组织管理-用户管理",action = "列表修改用户信息接口")
    @PutMapping("/user")
    public Response<String> updateUserInfo(@RequestBody @Valid UserUpdateReqVo userUpdateReqVo,HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getInstance().getUserId(accessToken);// 操作人
        return userService.updateUserInfo(userUpdateReqVo,operationId);
    }

    @ApiOperation(value = "批量/删除用户",notes = "批量/删除用户接口")
    @RequiresPermissions("sys:user:delete")
    @MyLog(title = "组织管理-用户管理",action = "批量/删除用户接口")
    @DeleteMapping("/user")
    public Response<String> deletedUsers(@RequestBody @ApiParam(value = "用户id集合")List<String> list,HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getInstance().getUserId(accessToken);// 操作人
        return userService.deletedUsers(list,operationId);
    }

    @ApiOperation(value = "用户信息详情",notes = "用户信息详情接口")
    @MyLog(title = "组织管理-用户管理",action = "用户信息详情接口")
    @GetMapping("/user/info")
    public Response<UserRespVo> detailInfo(HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        return userService.detailInfo(userId);
    }

    @ApiOperation(value = "保存个人信息",notes = "保存个人信息接口")
    @MyLog(title = "组织管理-用户管理",action = "保存个人信息接口")
    @PutMapping("/user/info")
    public Response<String> saveUserInfo(@RequestBody UserUpdateDetailInfoReqVo updateDetailInfoReqVo, HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        return userService.userUpdateDetailInfo(updateDetailInfoReqVo,userId);
    }

    @ApiOperation(value = "修改个人密码",notes = "修改个人密码接口")
    @MyLog(title = "组织管理-用户管理",action = "修改个人密码接口")
    @PutMapping("/user/password")
    public Response<String> updatePassword(@RequestBody @Valid UserUpdatePasswordReqVo userUpdatePasswordReqVo, HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        //String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        return userService.userUpdatePassword(userUpdatePasswordReqVo,accessToken);
    }
}
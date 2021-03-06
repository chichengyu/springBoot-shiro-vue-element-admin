package com.shiro.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiro.config.TokenConfig;
import com.shiro.constarts.Constant;
import com.shiro.dao.SysUserDao;
import com.shiro.dao.SysUserRoleDao;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.pojo.SysUser;
import com.shiro.service.PermissionService;
import com.shiro.service.RoleService;
import com.shiro.service.UserRoleService;
import com.shiro.service.UserService;
import com.shiro.utils.IdWorker;
import com.shiro.utils.JwtTokenUtil;
import com.shiro.service.RedisService;
import com.shiro.utils.PageUtil;
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
import com.shiro.vo.resp.PermissionRespNodeVo;
import com.shiro.vo.resp.UserOwnRoleRespVo;
import com.shiro.vo.resp.UserRespVo;
import com.shiro.vo.resp.UserTableRespVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenConfig tokenConfig;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    /**
     * ??????
     * @param loginReqVo
     * @return
     */
    @Override
    public Response<LoginRespVo> login(LoginReqVo loginReqVo) {
        SysUser user = sysUserDao.findByUsername(loginReqVo.getUsername());
        if (null == user){// ???????????????
            throw new BusinessException(ResponseCode.ACCOUNT_ERROR);
        }
        if (!BCrypt.checkpw(loginReqVo.getPassword(),user.getPassword())){// ???????????????
            throw new BusinessException(ResponseCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (user.getStatus() == 2){// ????????????
            return Response.error(ResponseCode.ACCOUNT_LOCK.getMessage());
        }
        // ???????????????????????????????????????
        List<PermissionRespNodeVo> permissionRespNodeVos = permissionService.permissionTreeListByUserId(user.getId());
        // ????????????????????????
        List<String> permissionCodes = sysUserRoleDao.getPermissionCodesByUserId(user.getId());
        // ????????????id???????????????????????????????????????
        List<String> roleNames = sysUserRoleDao.getRoleNameByUserId(user.getId());
        // ????????????id??????????????????????????????????????? ??????sys:user:add
        List<String> permissionPerms = sysUserRoleDao.getPermissionPermsByUserId(user.getId());
        /*List<String> roleNames = new ArrayList<>();
        roleNames.add("admin");
        List<String> permissionPerms = new ArrayList<>();
        permissionPerms.add("sys:user");
        permissionPerms.add("user:test");*/
        // ???????????? token ??????
        String accessToken = JwtTokenUtil.getInstance()
                .setIssuer(tokenConfig.getIssuer())
                .setSecret(tokenConfig.getSecretKey())
                .setExpired(tokenConfig.getAccessTokenExpireTime().toMillis())
                .setSubObject(user.getId())
                .setClaim(Constant.JWT_ROLES_KEY, JSON.toJSONString(roleNames))
                .setClaim(Constant.JWT_PERMISSIONS_KEY, JSON.toJSONString(permissionPerms))
                .setClaim(Constant.JWT_USER_NAME, user.getUsername())
                .generateToken();
        // ????????????????????????token?????? redis???????????????????????????????????????
        redisService.set(Constant.JWT_USER_NAME+user.getId(),accessToken);
        // ????????????????????????????????????????????????
        redisService.delete(Constant.JWT_USER_LOGIN_BLACKLIST+user.getId());
        LoginRespVo loginRespVo = new LoginRespVo();
        loginRespVo.setAccessToken(accessToken);
        loginRespVo.setId(user.getId());
        loginRespVo.setPhone(user.getPhone());
        loginRespVo.setUsername(user.getUsername());
        loginRespVo.setNickname(user.getNickName());
        loginRespVo.setMenus(permissionRespNodeVos);
        loginRespVo.setPermissions(permissionCodes);
        return Response.success(loginRespVo);
    }

    /**
     * ????????????
     * @param accessToken
     * @return
     */
    @Override
    public Response<String> logout(String accessToken) {
        if (StringUtils.isBlank(accessToken)){
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        // shiro ??????
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            subject.logout();
        }
        // ???????????? id
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        // ??????????????????????????????????????????????????????
        redisService.delete(Constant.JWT_USER_LOGIN_BLACKLIST+userId);
        /**
         * ??????????????????????????????
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
        return Response.success();
    }

    /**
     * ??????????????????????????????????????????
     * @param userPageReqVo
     * @return
     */
    @Override
    public Response<PageVo<UserTableRespVo>> pageInfo(UserPageReqVo userPageReqVo) {
        PageHelper.startPage(userPageReqVo.getPageNum(),userPageReqVo.getPageSize());
        List<UserTableRespVo> sysUsers = sysUserDao.selectAll(userPageReqVo);
        return Response.success(PageUtil.getPageVo(new PageInfo<UserTableRespVo>(sysUsers)));
    }

    /**
     * ????????????
     * @param userAddReqVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> addUser(UserAddReqVo userAddReqVo) {
        if (sysUserDao.findByUsername(userAddReqVo.getUsername()) != null){
            throw new BusinessException(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userAddReqVo,sysUser);
        String salt = BCrypt.gensalt();
        sysUser.setId(String.valueOf(idWorker.nextId()));
        sysUser.setPassword(BCrypt.hashpw(userAddReqVo.getPassword(),salt));
        sysUser.setCreateTime(new Date());
        if (sysUserDao.insertSelective(sysUser) != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ???????????????????????????????????????
     * @param userId
     * @return
     */
    @Override
    public Response<UserOwnRoleRespVo> getUserOwnRole(String userId) {
        UserOwnRoleRespVo userOwnRoleRespVo = new UserOwnRoleRespVo();
        userOwnRoleRespVo.setOwnRoleIds(userRoleService.getRoleIdsByUserId(userId));
        userOwnRoleRespVo.setAllRole(roleService.selectAll());
        return Response.success(userOwnRoleRespVo);
    }

    /**
     * ??????????????????
     * @param vo
     * @return
     */
    @Override
    public Response<String> setUserOwnRole(UserOwnRoleReqVo vo) {
        // ?????????????????????????????????
        userRoleService.addUserRoleInfo(vo);
        /**
         * ??????????????????????????????,????????????????????????????????????
         */
        redisService.set(Constant.JWT_USER_LOGIN_BLACKLIST+vo.getUserId(),vo.getUserId());
        /**
         * ??????????????????????????????
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+vo.getUserId());
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ??????token??????  ???????????? token
     * @param refreshToken
     * @return
     */
    /*public Response refreshToken(String refreshToken){
        // ?????? token???????????? ????????????????????????
        JwtTokenUtil instance = JwtTokenUtil.getInstance();
        if (!instance.checkToken(refreshToken) || redisService.hasKey(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken)){
            throw new BusinessException(ResponseCode.TOKEN_ERROR);
        }
        // ??????????????????????????????????????????????????????????????????
        // ???????????? token ??? ???????????? token
        String userId = instance.getUserId(refreshToken);
        List<String> roleIds = new ArrayList<>();
        Set<String> permissions = new HashSet<>();
        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)){
            // ??????????????????????????????
            roleIds = userRoleService.getRoleIdsByUserId(userId);
            permissions = getPermissionsByUserId(userId);
        }
        String newAccessToken = instance.setClaim(Constant.JWT_ROLES_KEY, JSON.toJSONString(roleIds))
                .setClaim(Constant.JWT_PERMISSIONS_KEY, JSON.toJSONString(permissions))
                .setExpired(tokenConfig.getAccessTokenExpireTime().toMillis())
                .generateToken();
        *//*if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)){
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION+newAccessToken,userId, redisService.getExpire(Constant.JWT_REFRESH_KEY+userId,TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);
        }*//*
        Map map = new HashMap<>();
        //map.put("authorization",newAccessToken);
        map.put("authorization",newAccessToken);
        return Response.success(map);
    }*/

    /**
     * ????????????id?????????????????????????????????
     * @param userId
     */
    /*public Set<String> getPermissionsByUserId(String userId){
        return permissionService.getPermissionByUserId(userId);
    }*/


    /**
     * ??????????????????
     * @param userUpdateReqVo
     * @param operationId ?????????
     * @return
     */
    @Override
    public Response<String> updateUserInfo(UserUpdateReqVo userUpdateReqVo, String operationId) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateReqVo,sysUser);
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        if (StringUtils.isBlank(userUpdateReqVo.getPassword())){
            sysUser.setPassword(StringUtils.EMPTY);
        }else{
            sysUser.setPassword(BCrypt.hashpw(userUpdateReqVo.getPassword(),BCrypt.gensalt()));
        }
        int result = sysUserDao.updateSelective(sysUser);
        if (result == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        if(userUpdateReqVo.getStatus() == 2){
            // ???????????????????????? redis ??????????????????
            redisService.set(Constant.ACCOUNT_LOCK_KEY+sysUser.getId(),sysUser.getId());
        }else {
            // ????????????????????????
            redisService.delete(Constant.ACCOUNT_LOCK_KEY+sysUser.getId());
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ??????/??????????????????
     * @param list
     * @param operationId ?????????
     * @return
     */
    @Override
    public Response<String> deletedUsers(List<String> list, String operationId) {
        // ???????????????
        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        int result = sysUserDao.deletedUsers(sysUser,list);
        if (result == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        for (String userId : list){
            /**
             * ??????????????????????????????,????????????????????????????????????
             */
            redisService.delete(Constant.JWT_USER_LOGIN_BLACKLIST + userId);
            /**
             * ?????? redis ??? ?????????????????????
             */
            redisService.set(Constant.DELETED_USER_KEY + userId,userId,tokenConfig.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ??????????????????????????????
     * @param userId
     * @return
     */
    @Override
    public Response<UserRespVo> detailInfo(String userId) {
        if (StringUtils.isBlank(userId)){
            return Response.error(ResponseCode.DATA_ERROR.getMessage());
        }
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(sysUser,userRespVo);
        return Response.success(userRespVo);
    }

    /**
     * ????????????????????????
     * @param updateDetailInfoReqVo
     * @param userId
     * @return
     */
    @Override
    public Response<String> userUpdateDetailInfo(UserUpdateDetailInfoReqVo updateDetailInfoReqVo, String userId) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(updateDetailInfoReqVo,sysUser);
        sysUser.setId(userId);
        sysUser.setUpdateId(userId);
        sysUser.setUpdateTime(new Date());
        int updateCount = sysUserDao.updateSelective(sysUser);
        if (updateCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ??????????????????
     * @param userId
     * @return
     */
    @Override
    public Response<String> resetUpdatePassword(String userId) {
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        if (sysUser == null){
            return Response.error(ResponseCode.ACCOUNT_ERROR.getMessage());
        }
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(BCrypt.hashpw("123456",BCrypt.gensalt()));
        int result = sysUserDao.updateSelective(user);
        if (result == 0){
            return Response.error(ResponseCode.OPERATION_ERROR.getMessage());
        }
        /**
         * ??????????????????????????????,????????????????????????????????????
         */
        redisService.set(Constant.JWT_USER_LOGIN_BLACKLIST + userId,userId);
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * ??????????????????
     * @param userUpdatePasswordReqVo
     * @param accessToken
     * @return
     */
    @Override
    public Response<String> userUpdatePassword(UserUpdatePasswordReqVo userUpdatePasswordReqVo, String accessToken) {
        JwtTokenUtil instance = JwtTokenUtil.getInstance();
        String userId = instance.getUserId(accessToken);
        if (StringUtils.isBlank(userId)){
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        if (sysUser == null){
            throw new BusinessException(ResponseCode.TOKEN_ERROR);
        }
        if (!BCrypt.checkpw(userUpdatePasswordReqVo.getOldPwd(),sysUser.getPassword())){
            throw new BusinessException(ResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateId(userId);
        sysUser.setPassword(BCrypt.hashpw(userUpdatePasswordReqVo.getNewPwd(),BCrypt.gensalt()));
        sysUser.setUpdateTime(new Date());
        int updateCount = sysUserDao.updateSelective(sysUser);
        if (updateCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        /**
         * ??????????????????????????????,????????????????????????????????????
         */
        redisService.set(Constant.JWT_USER_LOGIN_BLACKLIST+userId,userId);
        /**
         * ??????????????????????????????
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }
}

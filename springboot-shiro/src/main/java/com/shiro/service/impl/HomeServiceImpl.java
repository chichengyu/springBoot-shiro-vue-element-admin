package com.shiro.service.impl;

import com.alibaba.fastjson.JSON;
import com.shiro.dao.SysUserDao;
import com.shiro.pojo.SysPermission;
import com.shiro.pojo.SysUser;
import com.shiro.service.HomeService;
import com.shiro.service.PermissionService;
import com.shiro.utils.Response;
import com.shiro.vo.resp.HomeRespVo;
import com.shiro.vo.resp.PermissionRespNodeVo;
import com.shiro.vo.resp.UserRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PermissionService permissionService;

    /**
     * 获取首页数据（用户名称、权限菜单）
     * @param userId
     * @return
     */
    @Override
    public Response getHome(String userId) {
        //String meuns = "[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[],\"id\":\"6\",\"title\":\"五级类目5-6\",\"url\":\"string\"}],\"id\":\"5\",\"title\":\"四级类目4- 5\",\"url\":\"string\"}],\"id\":\"4\",\"title\":\"三级类目3- 4\",\"url\":\"string\"}],\"id\":\"3\",\"title\":\"二级类目2- 3\",\"url\":\"string\"}],\"id\":\"1\",\"title\":\"类目1\",\"url\":\"string\"},{\"children\": [],\"id\":\"2\",\"title\":\"类目2\",\"url\":\"string\"}]";
        //String meuns = "[{\"children\":[{\"children\":[],\"id\":\"6\",\"title\":\"五级类目5-6\",\"url\":\"/index/menus\"}],\"id\":\"1\",\"title\":\"类目1\",\"url\":\"string\"},{\"children\": [],\"id\":\"2\",\"title\":\"类目2\",\"url\":\"string\"}]";
        //List<PermissionRespNodeVo> permissionRespNodeVos = JSON.parseArray(meuns, PermissionRespNodeVo.class);
        // 权限菜单
        List<PermissionRespNodeVo> permissionRespNodeVos = permissionService.permissionTreeListByUserId(userId);
        // 用户信息
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        UserRespVo userRespVo = new UserRespVo();
        if (sysUser != null){
            BeanUtils.copyProperties(sysUser,userRespVo);
        }
        HomeRespVo homeRespVo = new HomeRespVo();
        homeRespVo.setMenus(permissionRespNodeVos);
        homeRespVo.setUserInfo(userRespVo);
        return Response.success(homeRespVo);
    }
}

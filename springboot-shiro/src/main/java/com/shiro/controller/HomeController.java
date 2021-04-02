package com.shiro.controller;


import com.shiro.aop.annotation.MyLog;
import com.shiro.constarts.Constant;
import com.shiro.service.HomeService;
import com.shiro.utils.JwtTokenUtil;
import com.shiro.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(value = "首页模块",tags = "首页相关模块")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @MyLog(title = "首页模块",action = "获取首页数据接口")
    @ApiOperation(value = "获取首页数据接口",notes = "首页数据")
    @GetMapping("/home")
    public Response getHome(HttpServletRequest request){
       /* String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        return homeService.getHome(userId);*/
       return null;
    }
}

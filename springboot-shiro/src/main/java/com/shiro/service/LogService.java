package com.shiro.service;

import com.shiro.pojo.SysLog;
import com.shiro.utils.Response;
import com.shiro.vo.req.LogPageReqVo;
import com.shiro.vo.resp.PageVo;

import java.util.List;

public interface LogService {

    /**
     * 分页与条件查询所有数据
     * @param logPageReqVo
     * @return
     */
    Response<PageVo<SysLog>> pageInfo(LogPageReqVo logPageReqVo);

    /**
     * 删除日志
     * @param logIds
     * @return
     */
    Response<String> deletedLog(List<String> logIds);
}

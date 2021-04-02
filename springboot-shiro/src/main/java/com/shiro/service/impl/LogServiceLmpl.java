package com.shiro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiro.dao.SysLogDao;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.pojo.SysLog;
import com.shiro.service.LogService;
import com.shiro.utils.PageUtil;
import com.shiro.utils.Response;
import com.shiro.vo.req.LogPageReqVo;
import com.shiro.vo.resp.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceLmpl implements LogService {

    @Autowired
    private SysLogDao sysLogDao;

    /**
     * 分页与条件查询所有数据
     * @param logPageReqVo
     * @return
     */
    @Override
    public Response<PageVo<SysLog>> pageInfo(LogPageReqVo logPageReqVo) {
        PageHelper.startPage(logPageReqVo.getPageNum(),logPageReqVo.getPageSize());
        List<SysLog> sysLogs = sysLogDao.selectAll(logPageReqVo);
        return Response.success(PageUtil.getPageVo(new PageInfo<SysLog>(sysLogs)));
    }

    /**
     * 删除日志
     * @param logIds
     * @return
     */
    @Override
    public Response<String> deletedLog(List<String> logIds) {
        int resultCount = sysLogDao.batchDeletedLog(logIds);
        if (resultCount == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }
}

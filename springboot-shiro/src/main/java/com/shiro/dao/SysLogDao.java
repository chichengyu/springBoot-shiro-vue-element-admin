package com.shiro.dao;

import com.shiro.pojo.SysLog;
import com.shiro.vo.req.LogPageReqVo;

import java.util.List;

public interface SysLogDao {
    int insert(SysLog record);

    int insertSelective(SysLog record);

    /**
     * 分页与条件查询所有数据
     * @param logPageReqVo
     * @return
     */
    List<SysLog> selectAll(LogPageReqVo logPageReqVo);

    /**
     * 删除日志（包括批量删除）
     * @param logIds
     * @return
     */
    int batchDeletedLog(List<String> logIds);
}
package com.shiro.controller;

import com.shiro.aop.annotation.MyLog;
import com.shiro.utils.FileUtil;
import com.shiro.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Api(tags = "上传文件")
@RestController
@RequestMapping("/api")
public class UploadController {

    @ApiOperation(value = "头像上传",notes = "头像上传接口")
    @MyLog(title = "上传头像",action = "上传头像的接口")
    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    public Response upload(@ApiParam(value = "头像上传",required = true) MultipartFile file) throws IOException {
        String[] type = {"jpg","jpeg","png","gif"};
        if (!Arrays.asList(type).contains(FileUtil.getFileType(file.getBytes()))){
            return Response.error("文件格式错误");
        }
        if (!FileUtil.checkFileSize(file.getSize(),30,"k")){
            return Response.error("文件不能超过30K");
        }
        String path = "D:\\upload";
        String fileName = FileUtil.upload(file, path);
        return Response.success("文件路径：" + fileName);
    }

}

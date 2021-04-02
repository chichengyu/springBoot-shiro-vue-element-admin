package com.shiro.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "登陆对象",description = "用户登陆的参数对象")
public class LoginReqVo {

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    @Length(min = 3,max = 16,message = "账号名称长度需要在3~16个字之间")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "登录类型 1：pc；2：App")
    @NotBlank(message = "用户登录类型不能为空")
    private String type;
}

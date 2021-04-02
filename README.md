# springBoot-shiro-vue-element-admin

[码云](https://gitee.com/chichengyu/springBoot-shiro-vue-element-admin)

### 后端：springboot-shiro
使用 `springBoot2.1.6 + shiro + redis + mysql` 的权限管理系统，账号：`admin`，密码：`123456`，如果使用 [spring-security](https://github.com/chichengyu/springBoot-security-vue-element-admin)


### 前端：vue-element-admin
使用的是 `vue2.6 + element-ui 2.11.1`，[vue-element-admin下载](https://github.com/chichengyu/vue-element-admin)  
注意：需要打开登录组件开启权限，默认 false 不开启，`另外 node.js　版本是 10.*`

##### 打包问题
关于前端 ` npm run build `报错 ` ERROR in xxx.js from UglifyJs ` 错误问题，这因为 ` uglifyjs-webpack-plugin版本兼容问题造成的 `，解决方法：运行命令
```
npm uni uglifyjs-webpack-plugin -D

npm i uglifyjs-webpack-plugin@1 -D
```
当前版本是 ` 2.*版本 `，降低到 `1.* 版本`就可以了，再次打包成功


架构说明
   + [Preview预览](http://xiaochiwz.github.io/thinkphp5.1-vue-ivew-admin)
   + [Element官网](https://element.eleme.cn/2.11/#/zh-CN/component/installation)
   + [Element-component插件](https://www.npmjs.com/package/element-component)

##### Getting started
```
# clone the project
git clone https://github.com/chichengyu/springBoot-shiro-vue-element-admin.git
```
##### Build
```
# install
npm install

# test
npm run build:test

# build
npm run build

# watch
npm run watch

# dev-server
npm run dev
```
##### Preview
![输入图片说明](https://images.gitee.com/uploads/images/2020/0519/161814_3e6bf8d6_1508174.png "login.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0807/151235_cc4d2d20_1508174.png "menu.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0807/151245_9278a97a_1508174.png "auth.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/0807/151256_a6605153_1508174.png "user.png")
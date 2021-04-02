//********************* 自定义模块 入口文件，需要引入 **********************
//  自己定义的 layui-ajax 模块
layui.config({
    base: '/' //你存放新模块的目录，注意，不是layui的模块目录
}).extend({
    // 加载自定义模块
    common: 'js/common',
    treetable: 'treetable-lay/treetable',
}).use(['jquery','layer']); //加载入口 , jquery 是 lay 模块核心目录下的默认 jquery 模块

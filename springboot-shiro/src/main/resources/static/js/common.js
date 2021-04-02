layui.define(['jquery','layer'], function(exports){
    var obj = {
        ajax:function (url, params, callback, method,headers, noAuthorityFt, async,contentType) {
            var roleSaveLoading = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
            layui.jquery.ajax({
                url: url,
                cache: false,
                async: async == undefined ? true : async,
                data: params,
                type: method == undefined ? "POST" : method,
                contentType: contentType == undefined ? 'application/json; charset=UTF-8': contentType ,
                dataType: "json",
                beforeSend: function(request) {
                    if(headers == undefined){

                    }else if(headers){
                        request.setRequestHeader("authorization", obj.getData("access_token"));
                        request.setRequestHeader("refresh_token", obj.getData("refresh_token"));
                    }else {
                        request.setRequestHeader("authorization", obj.getData("access_token"));
                    }
                },
                success: function (res) {
                    top.layer.close(roleSaveLoading);
                    if (typeof callback == "function") {
                        if(res.code == 1){ //凭证过期重新登录
                            callback && callback(res);
                            //layer.msg("凭证过期请重新登录");
                            // top.window.location.href="/index/login"
                        }else if(res.code == -1){//根据后端提示刷新token
                            /*记录要重复刷新的参数*/
                            var reUrl=url;
                            var reParams=params;
                            var reFt=callback;
                            var reMethod=method;
                            var reHeaders=headers;
                            var reContentType=contentType;
                            var reAsync=async;
                            var reNoAuthorityFt=noAuthorityFt;
                            /*刷新token  然后存入缓存*/
                            obj.ajax("/api/user/token",null,function (res) {
                                if(res.code == 1){
                                    obj.setData("access_token",res.data.authorization);
                                    /*刷新成功后继续重复请求*/
                                    obj.ajax(reUrl,reParams,reFt,reMethod,reHeaders,reNoAuthorityFt,reContentType,reAsync);
                                }else {
                                    layer.msg("凭证过期请重新登录");
                                    top.window.location.href="/index/login"
                                }
                            },"GET",true)
                        }/* else if(res.code == 1) {
                            callback && callback(res);
                        } */else if(res.code == 3){// 无权限
                            noAuthorityFt && noAuthorityFt(res);
                        }
                        else if (res.code == 2){
                            layer.msg(res.msg,function () {
                                top.window.location.href="/index/login";
                            });
                        }else {
                            layer.msg(res.msg);
                        }
                    }
                },error:function (XMLHttpRequest, textStatus, errorThrown) {
                    top.layer.close(roleSaveLoading);
                    if(XMLHttpRequest.status == 404){
                        top.window.location.href="/index/404";
                    }else{
                        layer.msg("服务器好像除了点问题！请稍后试试");
                    }
                }
            });
        },
        /*存入本地缓存*/
        setData: function(key, value){
            layui.sessionData('LocalData',{
                key :key,
                value: value
            })
        },
        /*从本地缓存拿数据*/
        getData: function(key){
            var localData = layui.sessionData('LocalData');
            return localData[key];
        },
        /*格式化时间格式*/
        formattime: function (val) {
            if(val == null || val == undefined){
                return "";
            }
            var date=new Date(val);
            var year=date.getFullYear();
            var month=date.getMonth()+1;
            month=month>9?month:('0'+month);
            var day=date.getDate();
            day=day>9?day:('0'+day);
            var hh=date.getHours();
            hh=hh>9?hh:('0'+hh);
            var mm=date.getMinutes();
            mm=mm>9?mm:('0'+mm);
            var ss=date.getSeconds();
            ss=ss>9?ss:('0'+ss);
            var time=year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
            return time;
        }
    };
    exports('common', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
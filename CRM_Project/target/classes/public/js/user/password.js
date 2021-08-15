//layui模块引入
layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //绑定表单提交（按钮（lay-filter属性值），函数(表单信息结果)）
    /**
     * 表单的submit监听
         form.on('submit(按钮元素的lay-filter属性值)',function (data){

        });

     */
    form.on('submit(saveBtn)',function (data){
        //所有表单元素的值data
        console.log(data.field);//控制台打印信息，F12的console

        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                oldPassword: data.field.old_password,
                newPassword: data.field.new_password,
                repeatPassword: data.field.again_password
            },
            success:function (result){
                //判断是否修改成功
                if(result.code == 200)
                {
                    //修改密码成功后，清空cookie数据，跳转到登陆页面
                    layer.msg("用户密码修改成功，系统将在3秒后退出...",function (){
                        //清空cookie,路径信息【因为百度可能有这个cookie，腾讯也可能有这个cookie】
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

                        //跳转到登陆页面【在父窗口跳转】
                        window.parent.location.href = ctx + "/index";
                    })
                }
                else
                {
                    layer.msg(result.msg,{icon:5});
                }

            }
        })
    });

})
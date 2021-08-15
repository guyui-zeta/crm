//layui模块引入
layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 从layui官网表单模块文档中获取格式
     * 表单submit提交
     * form.on('submit(按钮的lay-filter属性值)',function(data){
     *
     * });
     *
     */
    form.on('submit(login)', function(data){

        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        /* 表单非空校验【但是layui加了对应的校验，所以省略】*/
        /*发送Ajax请求给后端，传递用户姓名与密码，执行用户登陆操作*/
        $.ajax({
            type:"post",
            url:ctx + "/user/login",//ctx为公共域名，在common.ftl中定义
            data:{
                userName:data.field.username,
                userPwd:data.field.password
            },
            //回调函数会在后端验证通过后才进入
            success:function (result){//result是回调函数，用来接收后端返回的数据
                //result对应着UserController的resultInfo
                console.log(result);
                //判断是否登陆成功 如果code=200，则表示成功，否则表示失败
                if(result.code == 200)
                {
                    //登陆成功
                    /**
                     * 设置用户是登陆装备太
                     * 1.利用Session会话
                     *      保存用户信息，如果会话存在，则用户是登陆状态；否则是非登陆状态
                     *      缺点：服务器关闭，会话则会失效
                     * 2.利用cookie
                     *      保存用户信息，cookie未失效，则用户是登陆状态
                     */
                    layer.msg("登陆成功！",function (){
                        //判断用户是否选择记住密码（判断复选框是否被选中，如果选中，则设置cookie对象7天失效）
                        if($("#rememberMe").prop("checked"))
                        {
                            //选中，则设置cookie对象7天失效
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }
                        else
                        {
                            //将用户信息设置到cookie中
                            //直接存userId主键不安全，需要加密
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }


                        //登陆成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    })
                }
                else
                {
                    //登陆失败:打印异常信息
                    layer.msg(result.msg,{icon:5});//加上哭脸
                }

            }
        })
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    
    
});
layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 加载数据表格
     * var定义变量是为了方便后面的表格重载【不同的参数条件：无条件and三个条件】
     */
    var tableIns = table.render({
        //id
        id:'userTable'
        //容器元素的id属性值--
        ,elem: '#userList'//#表示绑定ID，在ftl中的table位置
        ,height: 'full-125'//容器的高度 full-差值，高度将始终铺满，无论浏览器尺寸如何
        ,cellMinWidth:95//单元格最小宽度
        //访问数据的url（对应后台的数据接口）
        ,url: ctx + '/user/list' //数据接口，数据通过访问list这个controller获取
        ,page: true //开启分页
        ,limit:10//默认每页显示的条数：10
        ,limits:[10,20,30,40,50]//每页页数的可选项
        ,toolbar:'#toolbarDemo'//开启头部工具栏区域
        //表头
        ,cols: [[
            //field：要求field属性值与返回的数据中心对应的属性字段名一致
            //title：设置的标题
            //sort：是否允许排序（默认：false）
            //fixed：固定列

            //复选框
            {type:'checkbox',fixed: 'center'}
            ,{field: 'id', title: '编号', sort: true, fixed: 'left'}
            ,{field: 'userName', title: '用户名称',align:'center'}
            ,{field: 'trueName', title: '真实姓名',align:'center'}
            ,{field: 'email', title: '用户邮箱',align:'center'}
            ,{field: 'phone', title: '用户号码',align:'center'}
            ,{field: 'createDate', title: '创建时间',align:'center'}
            ,{field: 'updateDate', title: '更新时间',align:'center'}
            ,{title: '操作',templet:'#userListBar', fixed: 'right', align: 'center', minWidth:150}

        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function (){
        /**
         * 表格重载【就是重载：相同的函数，不同的参数，此处为有三个条件的select语句】
         * 多条件查询
         */
        tableIns.reload({
            //设置需要传递给后端的参数
            where: { //设定异步数据接口的额外参数，任意设
                //通过文本框/下拉框的值，设置船体的参数
                userName: $("[name='userName']").val()//用户名称
                ,email: $("[name='email']").val()//邮箱
                ,phone: $("[name='phone']").val()//号码
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    })

    /**
     * 监听头部工具栏
     *
     */
    table.on('toolbar(users)',function (data){
        console.log(data);
        if(data.event == "add"){//添加用户
            openAddOrUpdateUserDialog();
        }else if(data.event == "del"){//删除用户
            //获取被选中的数据的信息
            var checkStatus = table.checkStatus(data.config.id);
            console.log(checkStatus);
            //删除多个用户记录
            deleteUsers(checkStatus.data);
        }
    })

    /**
     * 删除多条用户记录
     * @param userData
     */
    function deleteUsers(userData){
        //判断用户是否选择了要删除的记录
        if(userData.length == 0){
            layer.msg("请选择要删除的记录!",{icon:5});
            return ;
        }
        //询问用户是否确认删除
        layer.confirm('您确定要删除选中的记录吗？',{icon:3,title:'用户管理'},function (index){
            //如果用户点了确定,index为弹出询问框
            //关闭确认框
            layer.close(index);
            //传递的参数是数组 ids=1&ids=2&ids=3
            var ids = "ids=";
            //循环选中的行记录的数据
            for(var i =0; i < userData.length; i++){
                if(i < userData.length - 1)
                {
                    ids = ids +userData[i].id + "&ids=";
                }else{
                    ids = ids +userData[i].id;
                }
            }
            console.log(ids);
            //发送ajax请求，执行删除
            $.ajax({
                type:"post",
                url:ctx + "/user/delete",
                data:ids,//传递的参数是数组 ids=1&ids=2&ids=3
                success:function (result){
                    //判断删除结果
                    if(result.code == 200){
                        //提示成功
                        layer.msg("删除成功",{icon:6});
                        //刷新表格
                        tableIns.reload();
                    }else{
                        //提示失败
                        layer.msg(result.msg,{icon:5});
                    }
                }

            })
        })

    }

    /**
     * 监听行工具栏
     *
     */
    table.on('tool(users)',function (data){
        console.log(data);
        if(data.event == "edit"){//更新用户
            openAddOrUpdateUserDialog(data.data.id);
        }else if(data.event == "del"){//删除用户
            //删除单条用户记录
            deleteUser(data.data.id);

        }
    })

    /**
     * 删除单条用户记录
     * @param id
     */
    function deleteUser(id){
        //弹出确认框，询问用户是确认删除
        layer.confirm('确定要删除该记录吗？',{icon:3,title:"用户管理"},function (index){
            //回调函数，当按下确认之后做的事情，index就是当前弹出框的索引
            //关闭确认框
            layer.close(index);
            //发送ajax请求，删除记录
            $.ajax({
                type:"post",
                url: ctx + "/user/delete",
                data:{
                    ids:id
                },
                success:function (result){
                    //判断删除结果
                    if(result.code == 200){
                        //提示成功
                        layer.msg("删除成功",{icon:6});
                        //刷新表格
                        tableIns.reload();
                    }else{
                        //提示失败
                        layer.msg(result.msg,{icon:5});
                    }
                }
            })
        });
    }

    function openAddOrUpdateUserDialog(id){
        var title = "<h3>用户管理-添加用户</h3>>";
        var url = ctx + "/user/toAddOrUpdateUserPage";
        //判断id是否为空：如果为孔，则为添加操作，否则为修改操作
        if(id != null && id != ''){
            title = "<h3>用户管理-更新用户</h3>>";
            url+="?id="+id;//传递主键，查询数据
        }        //iframe层layui.layer.open会在小窗口中打开
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['650px', '400px'],
            //url地址
            content: url,//iframe的url
            //可以最大化与最小化
            maxmin:true
        });
    }
});
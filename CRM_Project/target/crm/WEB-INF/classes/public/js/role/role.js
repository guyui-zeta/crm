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
              id:'roleTable'
              //容器元素的id属性值--
              ,elem: '#roleList'//#表示绑定ID，在ftl中的table位置
              ,height: 'full-125'//容器的高度 full-差值，高度将始终铺满，无论浏览器尺寸如何
              ,cellMinWidth:95//单元格最小宽度
              //访问数据的url（对应后台的数据接口）
              ,url: ctx + '/role/list' //数据接口，数据通过访问list这个controller获取
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
                     ,{field: 'roleName', title: '角色名称',align:'center'}
                     ,{field: 'roleRemark', title: '角色备注',align:'center'}
                     ,{field: 'createDate', title: '创建时间',align:'center'}
                     ,{field: 'updateDate', title: '更新时间',align:'center'}
                     ,{title: '操作',templet:'#roleListBar', fixed: 'right', align: 'center', minWidth:150}

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
                            roleName: $("[name='roleName']").val()//角色名称
                     }
                     ,page: {
                            curr: 1 //重新从第 1 页开始
                     }
              });
       });


       /**
        * 监听头部工具栏
        */
       table.on('toolbar(roles)',function(data){
              //判断lay-event属性
              console.log(data);
              if(data.event == "add"){
                     //打开添加/更新角色的对话框
                     openAddOrUpdateRoleDialog();
              }else if(data.event == "grant"){
                     //获取数据表格选中的记录数据
                     var checkStatus = table.checkStatus(data.config.id);
                     //打开授权的对话框
                     openAddGrantDialog(checkStatus.data);
              }
       });
       /**
        * 监听行工具栏
        */
       table.on('tool(roles)',function (data){
              //判断lay-event属性
              if(data.event == "edit"){//修改角色
                     //打开添加/更新角色的对话框
                     openAddOrUpdateRoleDialog(data.data.id);
              }else if(data.event == "del"){//删除角色
                     deleteRole(data.data.id);
              }
       })

       /**
        * 打开添加/更新角色的对话框
        */
       function openAddOrUpdateRoleDialog(roleId){
              var title = "<h3>角色管理 - 角色添加</h3>"
              var url = ctx + "/role/toAddOrUpdateRolePage";
              //如果reoleId不为空，则表示修改角色
              if(roleId != null && roleId != ''){
                     title = "<h3>角色管理 - 角色更新</h3>";
                     url += "?roleId=" + roleId;
              }
              layui.layer.open({
                     title:title,
                     content:url,
                     area:["500px","400px"],
                     type:2,
                     maxmin:true
              });
       }

       /**
        * 删除角色
        * @param roleId
        */
       function deleteRole(roleId){
              layer.confirm('确定要删除该记录吗？',{icon:3,title:"角色管理"},function (index){
                     //回调函数，当按下确认之后做的事情，index就是当前弹出框的索引
                     //关闭确认框
                     layer.close(index);
                     //发送ajax请求，删除记录
                     $.ajax({
                            type:"post",
                            url: ctx + "/role/delete",
                            data:{
                                   roleId:roleId
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

       /**
        * 打开授权页面
        */
       function openAddGrantDialog(data){
              //判断是否选择了角色记录
              if(data.length == 0){
                     layer.msg("请选择要授权的角色！",{icon:5});
                     return;
              }
              //只支持单个角色授权
              if(data.length > 1){
                     layer.msg("暂不支持批量角色授权！",{icon:5});
                     return;
              }
              var url = ctx + "/module/toAddGrantPage?roleId=" + data[0].id;
              var title = "<h3>角色管理 - 角色授权</h3>>";
              layui.layer.open({
                     title:title,
                     content: url,
                     type:2,
                     area: ["600px","600px"],
                     maxmin:true
              });

       }

});

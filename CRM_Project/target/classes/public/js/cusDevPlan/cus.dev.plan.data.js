layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载计划项数据表格
     * var定义变量是为了方便后面的表格重载【不同的参数条件：无条件and三个条件】
     */
    var tableIns = table.render({
        //id
        id:'cusDevPlanTable'
        //容器元素的id属性值--
        ,elem: '#cusDevPlanList'//#表示绑定ID，在ftl中的table位置
        ,height: 'full-125'//容器的高度 full-差值，高度将始终铺满，无论浏览器尺寸如何
        ,cellMinWidth:95//单元格最小宽度
        //访问数据的url（对应后台的数据接口）设置flag=1表示查询的是客户开发数据
        ,url: ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val() //数据接口，数据通过访问list这个controller获取
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
            ,{field: 'planItem', title: '计划项',align:'center'}
            ,{field: 'planDate', title: '计划时间',align:'center'}
            ,{field: 'exeAffect', title: '执行效果',align:'center'}
            ,{field: 'createDate', title: '创建时间',align:'center'}
            ,{field: 'updateDate', title: '更新时间',align:'center'}
            ,{title: '操作',templet:'#cusDevPlanListBar', fixed: 'right', align: 'center', minWidth:150}

        ]]
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)',function (data){
        if(data.event == "add"){//添加操作项
            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog();

        }else if(data.event == "success"){//开发成功
            //更新营销机会的开发状态
            updateSaleChanceDevResult(2);

        }else if(data.event == "failed"){//开发失败
            updateSaleChanceDevResult(3);
        }
    })

    /**
     * 监听行工具栏
     */
    table.on('tool(cusDevPlans)',function (data){
       if(data.event == "edit"){//更新计划项
           //打开添加或修改计划项的页面
           openAddOrUpdateCusDevPlanDialog(data.data.id);
       }else if(data.event == "del") {//删除计划项
           //删除计划项
           deleteCusDevPlan(data.data.id);
       }
    });



    /**
     * 打开添加或修改计划项的页面
     */
    function openAddOrUpdateCusDevPlanDialog(id){
        var title = "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();

        //判断计划项Id是否为空（如果为空则表示添加，不为空表示修改/更新）
        if(id != null && id != ''){
            //更新计划项
            title = "计划项管理 - 更新计划项";
            url += "&id=" + id;


        }
        //iframe层layui.layer.open会在小窗口中打开
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['500px', '300px'],
            //url地址
            content: url,//iframe的url
            //可以最大化与最小化
            maxmin:true
        });
    }

    /**
     * 删除计划项
     */
    function deleteCusDevPlan(id){
        //弹出确认框，询问用户是否确认删除
        layer.confirm('您确定要删除该记录吗？',{icon:3,title:'开发项数据管理'},function(index){
            $.post(ctx + '/cus_dev_plan/delete',{id:id},function (result){
                //判断删除结果
                if(result.code == 200){
                    //提示成功
                    layer.msg('删除成功',{icon:6});
                    //刷新数据表格
                    tableIns.reload();
                }else{
                    //提示失败
                    layer.msg(result.msg,{icon:5});
                }
            })
        })

    }

    /**
     * 更新营销机会的开发状态
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult){
        //弹出确认框，询问用户是否确认删除
        layer.confirm('您确认执行该操作吗？',{icon:3,title:"营销机会管理"},function (index){
            //得到需要被更新的营销机会的ID（通过隐藏域获取）
            var sId = $("[name='id']").val();
            //发送Ajax请求，更新营销机会的开发状态
            $.post(ctx + '/sale_chance/updateSaleChanceDevResult',{id:sId,devResult:devResult},function (result){
               if(result.code == 200){
                   layer.msg('更新成功！',{icon:6});
                   //关闭窗口
                   layer.closeAll("iframe");
                   //刷新父页面
                   parent.location.reload();
               } else {
                   layer.msg(result.msg,{icon:5});
               }
            });
        })
    }

});

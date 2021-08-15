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
        id:'saleChanceTable'
        //容器元素的id属性值--
        ,elem: '#saleChanceList'//#表示绑定ID，在ftl中的table位置
        ,height: 'full-125'//容器的高度 full-差值，高度将始终铺满，无论浏览器尺寸如何
        ,cellMinWidth:95//单元格最小宽度
        //访问数据的url（对应后台的数据接口）
        ,url: ctx + '/sale_chance/list' //数据接口，数据通过访问list这个controller获取
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
            ,{field: 'chanceSource', title: '机会来源',align:'center'}
            ,{field: 'customerName', title: '客户名称',align:'center'}
            ,{field: 'cgjl', title: '成功几率',align:'center'}
            ,{field: 'overview', title: '概要',align:'center'}
            ,{field: 'linkMan', title: '联系人',align:'center'}
            ,{field: 'linkPhone', title: '联系号码',align:'center'}
            ,{field: 'description', title: '描述',align:'center'}
            ,{field: 'createMan', title: '创建人',align:'center'}
            ,{field: 'uname', title: '分配人',align:'center'}
            ,{field: 'assignTime', title: '分配时间',align:'center'}
            ,{field: 'createDate', title: '创建时间',align:'center'}
            ,{field: 'updateDate', title: '更新时间',align:'center'}
            ,{field: 'state', title: '分配状态',align:'center',templet: function (d){
                    //调用函数，返回格式化的结果,d是传入的数据
                    return formatState(d.state);
                }}
            ,{field: 'devResult', title: '开发状态',align:'center',templet: function (d){
                    //调用函数，返回格式化结果
                    return formatDevResult(d.devResult);
                }}
            ,{title: '操作',templet:'#saleChanceListBar', fixed: 'right', align: 'center', minWidth:150}

        ]]
    });

    /**
     * 格式化分配状态值
     * 0 = 未分配
     * 1 = 已分配
     * 其他 = 未知状态
     * @param state
     */
    function formatState(state)
    {
        if(state == 0)
        {
            return "<div style='color: yellowgreen'>未分配</div>"
        }
        else if(state == 1)
        {
            return "<div style='color: green'>已分配</div>"
        }
        else
        {
            return "<div style='color: red'>未知</div>"
        }
    }

    /**
     * 格式化开发状态
     *  0 = 未开发
     *  1 = 开发中
     *  2 = 开发成功
     *  3 = 开发失败
     * 其他 = 未知
     * @param devResult
     */
    function formatDevResult(devResult)
    {
        if(devResult == 0)
        {
            return "<div style='color: yellowgreen'>未开发</div>";
        }
        else if(devResult == 1)
        {
            return "<div style='color: orange'>开发中</div>";
        }
        else if(devResult == 2)
        {
            return "<div style='color: green'>开发成功</div>";
        }
        else if(devResult == 3)
        {
            return "<div style='color: red'>开发失败</div>";
        }
        else
        {
            return "<div style='color: blue'>未知</div>";
        }
    }

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
                customerName: $("[name='customerName']").val()//客户名称，通过name取值
                ,createMan: $("[name='createMan']").val()//创建人
                ,state: $("#state").val()//状态，通过id取值用#
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    })


    /**
     * 监听头部工具栏事件toolbar
     *  格式：
         table.on('toolbar(数据表格的lay-filter属性值)',function (data){
         })
     *
     */
    table.on('toolbar(saleChances)',function (data){
        //data.event：对应的按钮元素上设置的lay-event属性值
        console.log(data);
        //判断对应的时间类型
        if(data.event == "add")
        {
            //添加操作
            openSaleChanceDialog();
        }
        else if(data.event == "del")
        {
            //删除操作
            deleteSaleChance(data);
        }
    })

    /**
     *打开添加/修改营销机会数据的窗口
     *     如果营销机会ID为空，则为添加操作
     *     如果营销机会ID不会空，则为修改操作
     */

    function openSaleChanceDialog(saleChanceId){
        //弹出层的标题
        var title = "<h3>营销机会管理 - 添加营销机会</h3>";
        var url = ctx + "/sale_chance/toSaleChancePage";
        //判断营销机会ID是否为空
        //添加营销管理：直接跳转
        //更新营销管理：带参数跳转，因为需要在ftl中显示已有的数据
        if(saleChanceId != null && saleChanceId != '')
        {
            //更新操作
            title = "<h3>营销机会管理 - 更新营销机会</h3>>";
            //请求地址传递营销机会的ID
            url += '?saleChanceId=' + saleChanceId;
        }

        //iframe层layui.layer.open会在小窗口中打开
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //宽高
            area: ['500px', '620px'],
            //url地址
            content: url,//iframe的url
            //可以最大化与最小化
            maxmin:true
        });
    }

    /**
     * 删除营销机会（删除多条记录）
     * @param data
     */
    function deleteSaleChance(data){
        //获取数据表格选中的数据 table.checkStatus("数据表格的id属性");
        var checkStatus = table.checkStatus("saleChanceTable");
        console.log(checkStatus);
        //获取所有被选中的记录对应的数据
        var saleChanceData = checkStatus.data;
        //判断用户是否选择的记录（选中行的数量大于0）
        if(saleChanceData.length < 1){
            layer.msg("请选择要删除的记录！",{icon:5});
            return;
        }
        //询问用户是否确认删除
        layer.confirm('您确定要删除选中的记录吗？',{icon:3,title:'营销机会管理'},function (index){
            //如果用户点了确定,index为弹出询问框
            //关闭确认框
            layer.close(index);
            //传递的参数是数组 ids=1&ids=2&ids=3
            var ids = "ids=";
            //循环选中的行记录的数据
            for(var i =0; i < saleChanceData.length; i++){
                if(i < saleChanceData.length - 1)
                {
                    ids = ids +saleChanceData[i].id + "&ids=";
                }else{
                    ids = ids +saleChanceData[i].id;
                }
            }
            console.log(ids);
            //发送ajax请求，执行删除
            $.ajax({
                type:"post",
                url:ctx + "/sale_chance/delete",
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
     * 行工具栏监听事件tool
     table.on('tool(数据表格的lay-filter)',function (data){

    });
     */
    table.on('tool(saleChances)',function (data){
        //由于是在数据表格中，所以能获取data.field
        console.log(data);
        //判断类型
        if(data.event == "edit")//编辑操作
        {
            //得到对应的营销机会的id【console中data.data】
            var saleChanceID = data.data.id;
            //打开修改营销机会的窗口
            openSaleChanceDialog(saleChanceID);
        }
        else if(data.event == "del")//删除操作
        {
            //弹出确认框，询问用户是确认删除
            layer.confirm('确定要删除该记录吗？',{icon:3,title:"营销机会管理"},function (index){
                //回调函数，当按下确认之后做的事情，index就是当前弹出框的索引
                //关闭确认框
                layer.close(index);
                //发送ajax请求，删除记录
                $.ajax({
                    type:"post",
                    url: ctx + "/sale_chance/delete",
                    data:{
                        ids:data.data.id
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
    });





});

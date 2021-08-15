layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 表单submit的监听
     */
    form.on('submit(addOrUpdateCusDevPlan)',function (data){
        //加载层
        var index = top.layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        //得到所有的表单元素的值
        var formData = data.field;
        //请求地址
        var url = ctx + "/cus_dev_plan/add";
        //判断计划项ID是否为空（如果不为空，则代表更新）
        if($('[name="id"]').val()) {//【不能设置！=null？？未知错误】
            url = ctx + "/cus_dev_plan/update";
        }
        $.post(url,formData,function (result){
            //判断操作是否执行成功200=成功，其他=失败
            if(result.code == 200){
                //成功
                //提示成功
                top.layer.msg("操作成功",{icon:6});
                //关闭加载层
                top.layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //将数据显示出来【刷新页面父窗口,重新加载数据表格】
                parent.location.reload();
            }else{
                //失败
                layer.msg(result.msg,{icon:5});
            }
        });
        // 阻止表单提交
        return false;
    })
    /**
     *关闭当前弹出层
     */
    $("#closeBtn").click(function (){
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    })



});
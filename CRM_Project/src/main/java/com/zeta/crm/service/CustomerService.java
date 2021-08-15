package com.zeta.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zeta.crm.base.BaseService;
import com.zeta.crm.dao.CustomerMapper;
import com.zeta.crm.query.CustomerQuery;
import com.zeta.crm.query.SaleChanceQuery;
import com.zeta.crm.utils.AssertUtil;
import com.zeta.crm.utils.PhoneUtil;
import com.zeta.crm.vo.Customer;
import com.zeta.crm.vo.SaleChance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/8/2 21:17
 */
@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;

    /**
     * 多条件分页查询客户(返回的数据格式必须满足LayUI中数据表格要求的格式map)
     *
     *
     * @return
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery)
    {
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        //得到对应的分页对象,构造器中参数是待分类的对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户
     * 1.参数校验
     *      客户名称
     *          非空，名称唯一
     *      法人代表
     *          非空
     *      手机号码
     *          非空，格式正确
     * 2.设置参数的默认值
     *      是否有效：isvalid
     *      创建时间
     *      修改时间
     *      流失状态 state 0
     *          0 = 正产客户
     *          1 = 流失客户
     *      客户编号 khno
     *         系统生成且唯一（uuid|时间戳|雪花算法）
     *         格式：KH + 时间戳
     *
     *
     * 3.执行添加操作，判断受影响的行数
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        /*1.参数校验*/
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //判断客户名的唯一性
        Customer temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户名称是否存在
        AssertUtil.isTrue(temp != null,"客户名称已经存在，请重新输入！");
        /*2.设置参数的默认值*/
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setState(0);

        //客户编号
        String khno = "KH" + System.currentTimeMillis();
        customer.setKhno(khno);
        /*3.执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.insertSelective(customer) < 1,"添加客户信息失败！");
    }

    /**
     * 更新客户
     * 1.参数校验
     *      客户ID
     *          非空，数据存在
     *      客户名称
     *          非空，名称唯一
     *      法人代表
     *          非空
     *      手机号码
     *          非空，格式正确
     * 2.设置参数的默认值
     *      修改时间
     * 3.执行添加操作，判断受影响的行数
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer){
        /*1.参数校验*/
        AssertUtil.isTrue(customer.getId() == null,"待更新记录不存在!");
        //通过客户ID查询客户记录
        Customer temp = customerMapper.selectByPrimaryKey(customer.getId());
        //判断客户记录是否存在
        AssertUtil.isTrue(temp == null,"客户记录不存在！");
        //参数校验
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //通过客户名称查询客户记录
        temp = customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(customer.getId())),"客户名称已存在！");
        /*2.设置参数的默认值*/
        customer.setUpdateDate(new Date());
        /*3.执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1,"修改客户信息失败！");
    }

    /**
     * 参数校验
     * @param name
     * @param fr
     * @param phone
     */
    private void checkCustomerParams(String name, String fr, String phone) {
        //客户名称  非空
        AssertUtil.isTrue(name == null,"客户名称不能为空！");
        //法人代表  非空
        AssertUtil.isTrue(fr == null,"法人代表不能为空！");
        //手机号码phone 非空，格式正确
        AssertUtil.isTrue(phone == null ,"手机号码格式不能为空！");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号码格式不正确！");
    }

    /**
     * 删除客户信息
     *      1.参数校验
     *          id
     *              非空，数据存在
     *      2.设置参数默认值
     *          isvali 0
     *          updateDate  系统当前时间
     *      2.执行删除（更新）操作，判断受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer id) {
        //判断id是否为空，数据是否存在
        AssertUtil.isTrue(id == null,"待删除记录不存在11！");
        //通过id查询客户记录
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customer == null,"待删除记录不存在1112！" );

        //设置状态为失效
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());

        //执行删除（更新操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1,"删除客户信息失败！");
    }

    /**
     * 查询客户贡献分析
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        //得到对应的分页对象,构造器中参数是待分类的对象
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String, Object>>(customerMapper.queryCustomerContributionByParams(customerQuery));

        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 查询客户构成(折线图数据处理)
     * @return
     */
    public Map<String,Object> countCustomerMake(){
        Map<String,Object> map = new HashMap<>();
        //查询客户构成数据的类表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        //折线图X轴数据   数组
        List<String> data1 = new ArrayList<>();
        //折线图Y轴数据   数组
        List<Integer> data2 = new ArrayList<>();

        //判断数据列表，循环设置数据
        if(dataList != null && dataList.size() > 0){
            //遍历集合
            dataList.forEach(m ->{
                //获取“level”对应的数据，设置到X轴的集合中
                data1.add(m.get("level").toString());
                //获取“total”对应的数据，设置到Y轴的集合中
                data2.add(Integer.parseInt(m.get("total").toString()));
            });
            //将x轴的数据集合和y轴的数据集合设置到map中
            map.put("data1",data1);
            map.put("data2",data2);

        }
        return map;
    }

    /**
     * 查询客户构成(饼状图数据处理)
     * @return
     */
    public Map<String,Object> countCustomerMake02(){
        Map<String,Object> map = new HashMap<>();
        //查询客户构成数据的类表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        //饼状图数据   数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        //饼状图数据   数组（数组中是对象）
        List<Map<String,Object>> data2 = new ArrayList<>();

        //判断数据列表，循环设置数据
        if(dataList != null && dataList.size() > 0){
            //遍历集合
            dataList.forEach(m->{
                //饼状图数据   数组（数组中是字符串）
                data1.add(m.get("level").toString());
                //饼状图数据   数组（数组中是对象）
                Map<String ,Object> dataMap = new HashMap<>();
                dataMap.put("name",m.get("level"));
                dataMap.put("value",m.get("total"));
                data2.add(dataMap);

            });
        }

        map.put("data1",data1);
        map.put("data2",data2);
        return map;
    }

}

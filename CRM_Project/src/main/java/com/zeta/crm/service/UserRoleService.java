package com.zeta.crm.service;

import com.zeta.crm.base.BaseService;
import com.zeta.crm.dao.UserRoleMapper;
import com.zeta.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/27 9:57
 */
@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}

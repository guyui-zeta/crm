package com.zeta.crm.service;

import com.zeta.crm.base.BaseService;
import com.zeta.crm.dao.PermissionMapper;
import com.zeta.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/31 15:07
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询用户拥有的角色，角色拥有的资源，得到用户拥有的资源列表（资源权限码）
     *
     * @param userId
     * @return
     */
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}

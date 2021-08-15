package com.zeta.crm.query;

import com.zeta.crm.base.BaseQuery;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/27 12:40
 */
public class RoleQuery extends BaseQuery {
    private String roleName;//角色名称

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

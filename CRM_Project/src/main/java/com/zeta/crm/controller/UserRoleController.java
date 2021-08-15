package com.zeta.crm.controller;

import com.zeta.crm.base.BaseController;
import com.zeta.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/27 9:59
 */
@Controller
public class UserRoleController extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}

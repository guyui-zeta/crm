package com.zeta.crm.model;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/6/3 20:24
 */
public class UserModel {
    //private Integer userId;//为了加密userid，使用字符串类型
    private String userIdStr;//加密后的用户id
    private String userName;
    private String trueName;

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}

package com.zeta.crm.model;

/**
 * @author Zeta
 * @version 1.0
 * @date 2021/7/28 9:55
 */
public class TreeModel {
    private Integer id;
    private Integer pId;
    private String name;
    private boolean checked = false;//复选框是否被勾选，如果是true，则勾选；如果是false，则不勾选



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
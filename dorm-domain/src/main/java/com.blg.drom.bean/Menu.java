package com.blg.drom.domain.bean;

import java.util.Date;

public class Menu {
    /**
     *
     * 
     * 表字段 : DB_ID
     * 
     */
    private String dbId;

    /**
     *
     * è�œå�•åœ°å�€
     * 表字段 : MENU_HREF
     * 
     */
    private String menuHref;

    /**
     *
     * è�œå�•å��ç§°
     * 表字段 : MENU_NAME
     * 
     */
    private String menuName;

    /**
     *
     * 
     * 表字段 : PARENT_MENU_ID
     * 
     */
    private String parentMenuId;

    /**
     *
     * 
     * 表字段 : CREATE_TIME
     * 
     */
    private Date createTime;

    /**
     *
     * 
     * 表字段 : UPDATE_TIME
     * 
     */
    private Date updateTime;

    /**
     *
     * 
     * 表字段 : CREATE_USER
     * 
     */
    private String createUser;

    /**
     *
     * 
     * 表字段 : UPDATE_USER
     * 
     */
    private String updateUser;

    /**
     *
     * 
     * 表字段 : DEL_FLAG
     * 
     */
    private Integer delFlag;

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId == null ? null : dbId.trim();
    }

    public String getMenuHref() {
        return menuHref;
    }

    public void setMenuHref(String menuHref) {
        this.menuHref = menuHref == null ? null : menuHref.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId == null ? null : parentMenuId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dbId=").append(dbId);
        sb.append(", menuHref=").append(menuHref);
        sb.append(", menuName=").append(menuName);
        sb.append(", parentMenuId=").append(parentMenuId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}
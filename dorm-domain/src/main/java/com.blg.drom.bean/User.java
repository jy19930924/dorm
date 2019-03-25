package com.blg.drom.domain.bean;

import java.util.Date;

public class User {
    /**
     *
     * ä¸»é”®
     * 表字段 : DB_ID
     * 
     */
    private Integer dbId;

    /**
     *
     * æ˜¾ç¤ºå��
     * 表字段 : NAME
     * 
     */
    private String name;

    /**
     *
     * ç™»å½•å��
     * 表字段 : USER_NAME
     * 
     */
    private String userName;

    /**
     *
     * å¯†ç �
     * 表字段 : PASSWORD
     * 
     */
    private String password;

    /**
     *
     * æ‰‹æœºå�·
     * 表字段 : TEL
     * 
     */
    private String tel;

    /**
     *
     * ç”¨æˆ·é‚®ç®±
     * 表字段 : EMAIL
     * 
     */
    private String email;

    /**
     *
     * å�¯ç”¨çŠ¶æ€�ï¼Œå�¯ç”¨=1ï¼Œç¦�ç”¨=0
     * 表字段 : STATE
     * 
     */
    private Integer state;

    /**
     *
     * çº§åˆ«
     * 表字段 : LEVE_ID
     * 
     */
    private Integer leveId;

    /**
     *
     * ç­‰çº§å��ç§°
     * 表字段 : LEVE_NAME
     * 
     */
    private String leveName;

    /**
     *
     * è§’è‰²
     * 表字段 : ROLE_ID
     * 
     */
    private String roleId;

    /**
     *
     * è§’è‰²å��ç§°
     * 表字段 : ROLE_NAME
     * 
     */
    private String roleName;

    /**
     *
     * æœ€å�Žç™»å½•æ—¶é—´
     * 表字段 : LAST_LOGIN
     * 
     */
    private Date lastLogin;

    /**
     *
     * åˆ›å»ºæ—¶é—´
     * 表字段 : CREATE_TIME
     * 
     */
    private Date createTime;

    /**
     *
     * åˆ›å»ºäºº
     * 表字段 : CREATE_USER
     * 
     */
    private String createUser;

    /**
     *
     * ä¿®æ”¹æ—¶é—´
     * 表字段 : UPDATE_TIME
     * 
     */
    private Date updateTime;

    /**
     *
     * ä¿®æ”¹äºº
     * 表字段 : UPDATE_USER
     * 
     */
    private String updateUser;

    /**
     *
     * åˆ é™¤æ ‡å¿—
     * 表字段 : DEL_FLAG
     * 
     */
    private Integer delFlag;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getLeveId() {
        return leveId;
    }

    public void setLeveId(Integer leveId) {
        this.leveId = leveId;
    }

    public String getLeveName() {
        return leveName;
    }

    public void setLeveName(String leveName) {
        this.leveName = leveName == null ? null : leveName.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        sb.append(", name=").append(name);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", tel=").append(tel);
        sb.append(", email=").append(email);
        sb.append(", state=").append(state);
        sb.append(", leveId=").append(leveId);
        sb.append(", leveName=").append(leveName);
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", lastLogin=").append(lastLogin);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}
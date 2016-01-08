package com.pkry.db.model.DTOs;

import java.util.Date;

/**
 * AuthSessionDTO class
 */
public class AuthSessionDTO {

    Integer maxSessionTime;

    String sessionId;

    Date startTime;

    Date updateTime;

    AuthDTO authDTO;

    boolean up;

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public Integer getMaxSessionTime() {
        return maxSessionTime;
    }

    public void setMaxSessionTime(Integer maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public AuthDTO getAuthDTO() {
        return authDTO;
    }

    public void setAuthDTO(AuthDTO authDTO) {
        this.authDTO = authDTO;
    }
}

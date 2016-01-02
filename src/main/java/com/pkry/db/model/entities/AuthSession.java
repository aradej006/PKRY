package com.pkry.db.model.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arade on 29-Dec-15.
 */
@Entity
public class AuthSession {

    @Id
    @GeneratedValue
    Long id;

    Integer maxSessionTime;

    String sessionId;

    Date startTime;

    Date updateTime;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Auth auth;

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

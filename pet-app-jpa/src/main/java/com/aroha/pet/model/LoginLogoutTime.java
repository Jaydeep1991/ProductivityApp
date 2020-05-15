package com.aroha.pet.model;

import com.aroha.pet.model.audit.LoginDateAudit;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoginLogoutTime extends LoginDateAudit{

    @Id
    private Long userId;
    private String loggedInTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoggedInTime() {
        return loggedInTime;
    }

    public void setLoggedInTime(String loggedInTime) {
        this.loggedInTime = loggedInTime;
    } 
}

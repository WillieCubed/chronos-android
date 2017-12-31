package com.craft.apps.countdowns.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @since 2.0.0
 */
public class User {

    private String uid;

    private String name;

    private long joinDate;

    private String preferredEmail;

    private Map<String, Object> countdowns = new HashMap<>();

    /**
     * Empty public constructor for Firebase.
     */
    public User() {
    }

    public User(String uid, String name, long joinDate, String preferredEmail,
                Map<String, Object> countdowns) {
        this.uid = uid;
        this.name = name;
        this.joinDate = joinDate;
        this.preferredEmail = preferredEmail;
        this.countdowns = countdowns;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public String getPreferredEmail() {
        return preferredEmail;
    }

    public void setPreferredEmail(String preferredEmail) {
        this.preferredEmail = preferredEmail;
    }

    public Map<String, Object> getCountdowns() {
        return countdowns;
    }

    public void setCountdowns(Map<String, Object> countdowns) {
        this.countdowns = countdowns;
    }
}

package com.craft.apps.countdowns.common.model;

import android.support.annotation.Nullable;

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

    private String imageUrl;

    private Map<String, Object> privileges;

    private Map<String, Object> countdowns;

    private Map<String, Object> fcmTokens;

    /**
     * Empty public constructor for Firebase.
     */
    public User() {
    }

    public User(String uid, String name, long joinDate, String preferredEmail,
                Map<String, Object> countdowns, Map<String, Object> fcmTokens) {
        this.uid = uid;
        this.name = name;
        this.joinDate = joinDate;
        this.preferredEmail = preferredEmail;
        this.countdowns = countdowns;
        this.fcmTokens = fcmTokens;
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

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Nullable
    public Map<String, Object> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Map<String, Object> privileges) {
        this.privileges = privileges;
    }

    public Map<String, Object> getCountdowns() {
        return countdowns;
    }

    public void setCountdowns(Map<String, Object> countdowns) {
        this.countdowns = countdowns;
    }

    @Nullable
    public Map<String, Object> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(Map<String, Object> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }
}

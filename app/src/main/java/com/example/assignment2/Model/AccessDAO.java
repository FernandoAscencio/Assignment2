package com.example.assignment2.Model;

public class AccessDAO {
    private long id;
    private long profileId;
    private AccessType type;
    private String timestamp;

    public AccessDAO(long id, long profileId, AccessType type, String timestamp) {
        this.id = id;
        this.profileId = profileId;
        this.type = type;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public AccessType getType() {
        return type;
    }

    public void setType(AccessType type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

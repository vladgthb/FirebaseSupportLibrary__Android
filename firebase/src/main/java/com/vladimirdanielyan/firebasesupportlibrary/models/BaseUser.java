package com.vladimirdanielyan.firebasesupportlibrary.models;

/**
 * Created by vlad on 6/27/17.
 * The Class Of Basic Info Of User
 */

@SuppressWarnings("unused")
public class BaseUser {

    private String userType, userName, userId, userProfilePicture;

    public BaseUser() {}

    public BaseUser(String user_type, String user_name, String user_id, String userProfilePicture) {
        setUserType(user_type);
        setUserName(user_name);
        setUserId(user_id);
        setUserProfilePicture(userProfilePicture);
    }

    private void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return getValue(this.userType);
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return getValue(this.userName);
    }

    private void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getUserProfilePicture() {
        return getValue(this.userProfilePicture);
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return getValue(this.userId);
    }

    private String getValue(String value) {
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
}

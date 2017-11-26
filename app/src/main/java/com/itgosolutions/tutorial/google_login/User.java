package com.itgosolutions.tutorial.google_login;


class User {
    private String name;
    private String email;
    private String googleUserId;
    private String pictureURL;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getGoogleUserId() {
        return googleUserId;
    }

    void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }

    String getPictureURL() {
        return pictureURL;
    }

    void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}

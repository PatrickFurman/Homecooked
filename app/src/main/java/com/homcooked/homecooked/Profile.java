package com.homcooked.homecooked;

public class Profile {

    private String profile_name;
    private String profile_location;
    private String profile_email;
    //private String profile_description;
    //phone number int or string?
    //private int profile_number;

    public Profile() {

    }

    public Profile(String profile_name, String profile_email, String profile_location) {
        this.profile_name = profile_name;
        this.profile_email = profile_email;
        this.profile_location = profile_location;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getProfile_email() {
        return profile_email;
    }

    public void setProfile_email(String profile_email) {
        this.profile_email = profile_email;
    }

    public String getProfile_location() {
        return profile_location;
    }

    public void setProfile_location(String profile_location) {
        this.profile_location = profile_location;
    }
}
package com.homcooked.homecooked;

import android.text.Editable;

public class Profile {

    private String profile_name;
    private String profile_location;
    private String profile_email;
    private String profile_description;
    //phone number int or string?
    private String profile_phone;

    public Profile(String profile_name, String profile_email, String profile_location, Editable profile_phone, String profile_description) {

    }

    public Profile(String profile_name, String profile_email, String profile_location, String profile_phone, String profile_description) {
        this.profile_name = profile_name;
        this.profile_email = profile_email;
        this.profile_location = profile_location;
        this.profile_phone = profile_phone;
        this.profile_description = profile_description;
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

    public String getProfile_phone(){
        return profile_phone;
    }

    public void setProfile_phone(String profile_phone){
        this.profile_phone = profile_phone;
    }

    public String getProfile_description() {
        return profile_description;
    }

    public void setProfile_description(String profile_description){
        this.profile_description = profile_description;
    }
}
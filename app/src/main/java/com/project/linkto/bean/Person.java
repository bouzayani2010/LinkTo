package com.project.linkto.bean;

/**
 * Created by bbouzaiene on 20/04/2018.
 */

public class Person {
    private Long birthdate;
    private String firstname;
    private String lastname;
    private String email;
    private String coverphoto;
    private String profilephoto;
    private String key;

    public Long getBirthdate() {
        return 0 + birthdate;
    }

    public void setBirthdate(Long birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstname() {
        return "" + firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return "" + lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return "" + email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverphoto() {
        return "" + coverphoto;
    }

    public void setCoverphoto(String coverphoto) {
        this.coverphoto = coverphoto;
    }

    public String getProfilephoto() {
        return "" + profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Person{" +
                "birthdate=" + birthdate +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", coverphoto='" + coverphoto + '\'' +
                ", profilephoto='" + profilephoto + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}

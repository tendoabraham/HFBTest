package com.elmahousingfinanceug.launch.rao;

public class ugID {

    String fname,sname,lname,dob,nin,sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ugID(String fname, String sname, String lname, String dob, String nin, String sex) {
        this.fname = fname;
        this.sname = sname;
        this.lname = lname;
        this.dob = dob;
        this.nin = nin;
        this.sex = sex;
    }
    public ugID() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }
}

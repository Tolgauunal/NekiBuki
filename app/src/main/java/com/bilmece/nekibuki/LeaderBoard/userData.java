package com.bilmece.nekibuki.LeaderBoard;

public class userData {
    String email;
    long puan;

    public userData(String email, long puan) {
        this.email = email;
        this.puan = puan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPuan() {
        return puan;
    }

    public void setPuan(long puan) {
        this.puan = puan;
    }
}

package com.example.fit5046_assignment2;

import java.util.Date;

public class ReportsPK
{
    private int userId;
    private Date date;

    public ReportsPK(int userId, Date date) {
        this.userId = userId;
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

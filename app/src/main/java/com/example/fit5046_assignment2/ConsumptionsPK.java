package com.example.fit5046_assignment2;

import java.util.Date;

public class ConsumptionsPK
{
    private int userId;
    private Date date;
    private int foodId;

    public ConsumptionsPK(int userId, Date date, int foodId) {
        this.userId = userId;
        this.date = date;
        this.foodId = foodId;
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

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}

package com.example.fit5046_assignment2;

import java.math.BigDecimal;

public class Food
{
    private int foodId;
    private String foodName;
    private String foodCategory;
    private int calorie;
    private String servingUnit;
    private int fat;
    private BigDecimal servingAmount;

    public Food(int foodId, String foodName, String foodCategory, int calorie, String servingUnit, int fat, BigDecimal servingAmount)
    {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.calorie = calorie;
        this.servingUnit = servingUnit;
        this.fat = fat;
        this.servingAmount = servingAmount;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public BigDecimal getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(BigDecimal servingAmount) {
        this.servingAmount = servingAmount;
    }
}

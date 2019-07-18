package com.example.fit5046_assignment2;

public class Consumptions
{

    protected ConsumptionsPK consumptionsPK;
    private User users;
    private Foods foods;
    private Integer quantity;

    public Consumptions(ConsumptionsPK consumptionsPK, User user, Foods foods, Integer quantity) {
        this.consumptionsPK = consumptionsPK;
        this.users = user;
        this.foods = foods;
        this.quantity = quantity;
    }

    public ConsumptionsPK getConsumptionsPK() {
        return consumptionsPK;
    }

    public void setConsumptionsPK(ConsumptionsPK consumptionsPK) {
        this.consumptionsPK = consumptionsPK;
    }

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }

    public Foods getFoods() {
        return foods;
    }

    public void setFoods(Foods foods) {
        this.foods = foods;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

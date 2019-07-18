package com.example.fit5046_assignment2;

public class Reports
{
    protected ReportsPK reportsPK;
    private Integer totalCaloriesConsumed;
    private Integer totalCaloriesBurned;
    private Integer totalStepsTaken;
    private String goal;
    private User users;

    public Reports(ReportsPK reportsPK, Integer totalCaloriesConsumed, Integer totalCaloriesBurned, Integer totalStepsTaken, String goal, User users) {
        this.reportsPK = reportsPK;
        this.totalCaloriesConsumed = totalCaloriesConsumed;
        this.totalCaloriesBurned = totalCaloriesBurned;
        this.totalStepsTaken = totalStepsTaken;
        this.goal = goal;
        this.users = users;
    }

    public ReportsPK getReportsPK() {
        return reportsPK;
    }

    public void setReportsPK(ReportsPK reportsPK) {
        this.reportsPK = reportsPK;
    }

    public Integer getTotalCaloriesConsumed() {
        return totalCaloriesConsumed;
    }

    public void setTotalCaloriesConsumed(Integer totalCaloriesConsumed) {
        this.totalCaloriesConsumed = totalCaloriesConsumed;
    }

    public Integer getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }

    public void setTotalCaloriesBurned(Integer totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    public Integer getTotalStepsTaken() {
        return totalStepsTaken;
    }

    public void setTotalStepsTaken(Integer totalStepsTaken) {
        this.totalStepsTaken = totalStepsTaken;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
}

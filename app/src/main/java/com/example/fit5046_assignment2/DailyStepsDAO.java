package com.example.fit5046_assignment2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyStepsDAO
{
    @Query("SELECT * FROM dailyStepsDB")
    List<DailyStepsDB> getAll();

    @Query("SELECT * FROM dailyStepsDB WHERE user_name = :surname")
    DailyStepsDB findBySurname(String surname);

    @Query("SELECT * FROM dailyStepsDB WHERE id = :id LIMIT 1")
    DailyStepsDB findByID(int id);

    @Query("SELECT * FROM dailyStepsDB WHERE time = :time LIMIT 1")
    DailyStepsDB findByTime(String time);

    @Insert
    void insertAll(DailyStepsDB... dailyStepsDBS);

    @Insert
    long insert(DailyStepsDB dailyStepsDB);

    @Delete
    void delete(DailyStepsDB dailyStepsDB);

    @Update(onConflict = REPLACE)
    public void updateUsers(DailyStepsDB... dailyStepsDBS);

    @Query("DELETE FROM DailyStepsDB")
    void deleteAll();
}

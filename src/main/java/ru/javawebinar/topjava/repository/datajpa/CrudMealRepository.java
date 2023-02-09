package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> getAllByUserIdOrderByDateTimeDesc(int userId);

    Meal getMealByIdAndUserId(int id, int userId);

    List<Meal> getAllByDateTimeAfterAndDateTimeBeforeAndUserIdOrderByDateTimeDesc
            (LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    @Transactional
    int deleteMealByIdAndUserId(int id, int userId);

    @Transactional
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getMealWithUser(@Param("id") int id, @Param("userId") int userId);
}

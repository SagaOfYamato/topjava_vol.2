package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    Meal add(Meal meal);
    Meal findById(Integer id);
    List<Meal> findAll();
    Meal update(Meal meal);
    void delete(Integer id);


}

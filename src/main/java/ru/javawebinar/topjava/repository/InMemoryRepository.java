package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository implements MealRepository {
    public final Map<Integer, Meal> mealsWithId = new ConcurrentHashMap<>();
    public final AtomicInteger countOfMealsId = new AtomicInteger(0);

    public void init() {
        for (Meal meal : MealsUtil.meals) {
            mealsWithId.put(meal.getId(), meal);
            countOfMealsId.incrementAndGet();
        }
    }

    @Override
    public Meal add (Meal meal) {
        int id;
        try {
            id = meal.getId();
        } catch (NullPointerException e) {
            id = countOfMealsId.incrementAndGet();
            meal.setId(id);
        }
        return mealsWithId.put(id, meal);
    }

    @Override
    public Meal findById(Integer id) {
        return mealsWithId.get(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealsWithId.values());
    }

    @Override
    public Meal update(Meal meal) {
        Meal mealNew = new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return mealsWithId.replace(meal.getId(), mealNew);
    }

    @Override
    public void delete(Integer id) {
        mealsWithId.remove(id);
    }
}

package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal meal : MealsUtil.meals) {
            save(1, meal);
        }
        save(2, new Meal(LocalDateTime.of(2022, Month.JANUARY, 30, 10, 0),
                "Завтрак Админ", 500));
        save(2, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед Админ", 1000));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            mealMap = new ConcurrentHashMap<>();
            repository.put(userId, mealMap);
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        if (repository.get(userId) == null) {
            repository.put(userId, new ConcurrentHashMap<>());
        }

        return repository.get(userId).get(id);
    }

    @Override
    public List <Meal> getAll(int userId) {
        if (repository.get(userId) == null) {
            repository.put(userId, new ConcurrentHashMap<>());
        }

        return repository.get(userId).values().stream()
                .sorted((Comparator.comparing(Meal::getDate).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> filter(int userId, LocalDate startDate, LocalDate endDate) {
        if (repository.get(userId) == null) {
            repository.put(userId, new ConcurrentHashMap<>());
        }

        return repository.get(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpenDate(meal.getDate(), startDate, endDate))
                .sorted((Comparator.comparing(Meal::getDate).reversed()))
                .collect(Collectors.toList());
    }
}


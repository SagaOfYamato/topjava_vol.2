package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.util.ValidationUtil.checkUserId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create (int userId, Meal meal) {
        checkUserId(userId);

        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkUserId(userId);

        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        checkUserId(userId);

        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<Meal> getAll(int userId) {
        checkUserId(userId);

        return repository.getAll(userId);
    }

    public void update(int userId, Meal meal) {
        checkUserId(userId);

        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public List<Meal> filter(int userId, LocalDate startDate, LocalDate endDate) {
        checkUserId(userId);

        return (List<Meal>) repository.filter(userId, startDate, endDate);
    }
}
package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create (Meal meal) {
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public void delete(int id) {
        service.delete(SecurityUtil.authUserId(), id);
    }

    public Meal get(int id) {
        return service.get(SecurityUtil.authUserId(), id);
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public void update(Meal meal) {
        service.update(SecurityUtil.authUserId(), meal);
    }

    public List<MealTo> filter(String startDate, String endDate, String startTime, String endTime) {
        LocalDate startLocalDate = startDate.equals("") ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate endLocalDate = endDate.equals("") ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime startLocalTime = startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime endLocalTime = endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime);

        return MealsUtil.getFilteredTos(
                service.filter(SecurityUtil.authUserId(), startLocalDate, endLocalDate),
                SecurityUtil.authUserCaloriesPerDay(), startLocalTime, endLocalTime);
    }
}
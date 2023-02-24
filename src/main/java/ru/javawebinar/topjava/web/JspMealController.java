package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping()
    public String getFilteredMeals(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        if (startDate == null && endDate == null && startTime == null && endTime == null) {
            log.info("meals");
            model.addAttribute("meals", getAll());
        } else {
            log.info("filtered meals");
            model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        }
        return "meals";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteMeal(@PathVariable(name = "id") Integer id) {
        log.info("deleteMeal");
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping(value = "/create")
    public String showCreatePage(Model model) {
        log.info("showCreatePage");
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @GetMapping(value = "/update/{id}")
    public String showUpdatePage(Model model, @PathVariable(name = "id") Integer id) {
        log.info("showUpdatePage");
        Meal meal = get(id);
        model.addAttribute("meal", meal);
        return "/mealForm";
    }

    @PostMapping(value = "/create")
    public String createMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("createMeal");
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        create(meal);
        return "redirect:/meals";
    }

    @PostMapping(value = "/update/meals-update")
    public String updateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("updateMeal");
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(id)) {
            update(meal, Integer.parseInt(id));
        }
        return "redirect:/meals";
    }
}

package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.util.formatter.CustomDateFormatter;
import ru.javawebinar.topjava.util.formatter.CustomTimeFormatter;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    private CustomDateFormatter customDateFormatter = new CustomDateFormatter();

    private CustomTimeFormatter customTimeFormatter = new CustomTimeFormatter();

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(mealsTo));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

   /* @Test
    void getBetween() throws Exception {
        LocalDateTime startLD = LocalDateTime.of(2020, 1, 30, 0, 0, 0);
        LocalDateTime endLD = LocalDateTime.of(2020, 1, 30, 23, 59, 59);
        String start = startLD.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String end = endLD.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?start=" + start + "&end=" + end))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(mealsToBetween));
    }*/

    @Test
    void getBetween() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 1, 30);
        LocalDate endDate = LocalDate.of(2020, 1, 30);
        LocalTime startTime = LocalTime.of(7, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);
        String startDateStr = customDateFormatter.print(startDate, Locale.ENGLISH);
        String endDateStr = customDateFormatter.print(endDate, Locale.ENGLISH);
        String startTimeStr = customTimeFormatter.print(startTime, Locale.ENGLISH);
        String endTimeStr = customTimeFormatter.print(endTime, Locale.ENGLISH);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", startDateStr)
                .param("startTime", startTimeStr)
                .param("endDate", endDateStr)
                .param("endTime", endTimeStr))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(mealsToBetween));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, SecurityUtil.authUserId()));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, SecurityUtil.authUserId()), newMeal);
    }
}
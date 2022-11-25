package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_1 = START_SEQ + 3;
    public static final int MEAL_2 = START_SEQ + 4;
    public static final int MEAL_3 = START_SEQ + 5;
    public static final int MEAL_4 = START_SEQ + 6;
    public static final int MEAL_5 = START_SEQ + 7;
    public static final int MEAL_6 = START_SEQ + 8;
    public static final int MEAL_7 = START_SEQ + 9;
    public static final int MEAL_8 = START_SEQ + 10;
    public static final int MEAL_9 = START_SEQ + 11;

    public static final Meal meal1 = new Meal(MEAL_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(MEAL_3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(MEAL_4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal5 = new Meal(MEAL_5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal6 = new Meal(MEAL_6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal meal7 = new Meal(MEAL_7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal meal8 = new Meal(MEAL_8, LocalDateTime.of(2022, Month.MAY, 1, 10, 0), "Админ завтрак", 1410);
    public static final Meal meal9 = new Meal(MEAL_9, LocalDateTime.of(2022, Month.MAY, 1, 20, 0), "Админ ужин", 650);
    public static final Meal notFound = new Meal(UserTestData.NOT_FOUND, LocalDateTime.of(2022, Month.MAY, 1, 20, 0), "NotFound", 50);

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2022, Month.MAY, 5, 20, 0), "NewMeal", 650);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.of(2022, Month.SEPTEMBER, 25, 22, 0));
        updated.setDescription("UpdatedMeal");
        updated.setCalories(399);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}

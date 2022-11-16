package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.InMemoryRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    InMemoryRepository inMemoryRepository = new InMemoryRepository();

    @Override
    public void init() throws ServletException {
        super.init();
        inMemoryRepository.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Method doGet-meals was start");
        String forward;
        String action = req.getParameter("action");

        if (action == null || action.equals("meals")) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(inMemoryRepository.findAll(), LocalTime.MIN, LocalTime.MAX,
                    MealsUtil.CALORIES_PER_DAY);
            forward = "/meals.jsp";
            req.setAttribute("meals", mealsTo);
            req.getRequestDispatcher(forward).forward(req, resp);

        } else if (action.equals("delete")) {
            log.info("Method doGet-delete was start");
            int id = Integer.parseInt(req.getParameter("id"));
            inMemoryRepository.delete(id);
            resp.sendRedirect("meals");

        } else if (action.equals("add")) {
            log.info("Method doGet-add was start");
            forward = "/addOrUpdate.jsp";
            req.getRequestDispatcher(forward).forward(req, resp);

        } else if (action.equals("edit")) {
            log.info("Method doGet-edit was start");
            forward = "/addOrUpdate.jsp";
            String id = req.getParameter("id");
            Meal meal = inMemoryRepository.findById(Integer.parseInt(id));
            req.setAttribute("mealTo", meal);
            req.getRequestDispatcher(forward).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        log.info("Method doPost was start");

        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("date_time"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String id = req.getParameter("id");
        Meal mealNew = new Meal(localDateTime, description, calories);

        if (id == null || id.isEmpty()) {
            log.info("Method POST: add");
            inMemoryRepository.add(mealNew);
        } else {
            log.info("Method POST: edit");
            inMemoryRepository.update(mealNew);
        }
        req.setAttribute("mealTo", mealNew);
        resp.sendRedirect("meals");
    }
}

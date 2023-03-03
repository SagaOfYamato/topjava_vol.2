package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.formatter.CustomDateFormatter;
import ru.javawebinar.topjava.util.formatter.CustomDateTimeFormat;
import ru.javawebinar.topjava.util.formatter.CustomTimeFormatter;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    private CustomDateFormatter customDateFormatter = new CustomDateFormatter();

    private CustomTimeFormatter customTimeFormatter = new CustomTimeFormatter();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(customDateFormatter);
        binder.addCustomFormatter(customTimeFormatter);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    /*@GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                   @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return super.getBetween(start.toLocalDate(), start.toLocalTime(), end.toLocalDate(), end.toLocalTime());
    }*/

    @Override
    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam(required = false) @CustomDateTimeFormat(type = CustomDateTimeFormat.Type.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @CustomDateTimeFormat(type = CustomDateTimeFormat.Type.TIME) LocalTime startTime,
                                   @RequestParam(required = false) @CustomDateTimeFormat(type = CustomDateTimeFormat.Type.DATE) LocalDate endDate,
                                   @RequestParam(required = false) @CustomDateTimeFormat(type = CustomDateTimeFormat.Type.TIME) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
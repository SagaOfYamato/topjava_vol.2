package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Parser<?> getParser(Annotation annotation, Class fieldType) {
        return getFormatter((CustomDateTimeFormat) annotation, fieldType);
    }

    @Override
    public Printer<?> getPrinter(Annotation annotation, Class fieldType) {
        return getFormatter((CustomDateTimeFormat) annotation, fieldType);
    }

    private Formatter<?> getFormatter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        switch (annotation.type()) {
            case DATE -> {
                return new CustomDateFormatter();
            }
            case TIME -> {
                return new CustomTimeFormatter();
            }
        }
        return null;
    }
}

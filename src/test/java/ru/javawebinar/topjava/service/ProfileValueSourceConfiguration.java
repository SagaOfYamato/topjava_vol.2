package ru.javawebinar.topjava.service;

import org.springframework.test.annotation.ProfileValueSource;
import ru.javawebinar.topjava.Profiles;

public class ProfileValueSourceConfiguration implements ProfileValueSource {
    @Override
    public String get(String key) {
        return Profiles.REPOSITORY_IMPLEMENTATION;
    }
}

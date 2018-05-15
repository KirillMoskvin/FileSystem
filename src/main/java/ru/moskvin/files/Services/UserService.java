package ru.moskvin.files.Services;

import ru.moskvin.files.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}

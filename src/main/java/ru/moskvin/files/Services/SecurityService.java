package ru.moskvin.files.Services;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}

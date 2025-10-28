package com.example.smartTerrarium.exception;

public class SettingDoesNotExistException extends RuntimeException{
    public SettingDoesNotExistException(Integer id) {
        super("Setting with id " + id + " does not exist");
    }
}

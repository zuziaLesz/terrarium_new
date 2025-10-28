package com.example.smartTerrarium.exception;

public class NoCurrentSettingException extends RuntimeException{
    public NoCurrentSettingException() {
        super("You have no setting currently selected.");
    }
}

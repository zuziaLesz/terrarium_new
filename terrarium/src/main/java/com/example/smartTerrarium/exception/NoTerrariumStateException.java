package com.example.smartTerrarium.exception;

public class NoTerrariumStateException extends RuntimeException{
    public NoTerrariumStateException(){
        super("There is no terrarium state");
    }
}

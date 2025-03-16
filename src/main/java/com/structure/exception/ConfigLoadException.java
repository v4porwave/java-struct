package com.structure.exception;

public class ConfigLoadException extends RuntimeException{
    public ConfigLoadException(String message) {
        super("Load config error : %s".formatted( message ));
    }
}

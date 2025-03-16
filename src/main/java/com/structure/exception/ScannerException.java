package com.structure.exception;

public class ScannerException extends RuntimeException{
    public ScannerException(String message) {
        super("Scanner error : %s".formatted( message ));
    }
}

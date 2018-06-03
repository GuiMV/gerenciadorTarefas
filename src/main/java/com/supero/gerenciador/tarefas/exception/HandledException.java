package com.supero.gerenciador.tarefas.exception;

public class HandledException extends RuntimeException{

    public HandledException(String message) {
        super(message);
    }

    public HandledException(String message, Throwable cause) {
        super(message, cause);
    }
}

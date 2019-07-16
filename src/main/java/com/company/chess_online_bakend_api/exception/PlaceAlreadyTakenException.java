package com.company.chess_online_bakend_api.exception;

public class PlaceAlreadyTakenException extends RuntimeException {

    public PlaceAlreadyTakenException() {
    }

    public PlaceAlreadyTakenException(String message) {
        super(message);
    }

    public PlaceAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaceAlreadyTakenException(Throwable cause) {
        super(cause);
    }

    public PlaceAlreadyTakenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

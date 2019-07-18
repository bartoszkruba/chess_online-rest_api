package com.company.chess_online_bakend_api.exception;

public class AlreadyJoinedException extends RuntimeException {

    public AlreadyJoinedException() {
    }

    public AlreadyJoinedException(String message) {
        super(message);
    }

    public AlreadyJoinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyJoinedException(Throwable cause) {
        super(cause);
    }

    public AlreadyJoinedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.exception;

public class GameAlreadyStartedException extends RuntimeException {

    public GameAlreadyStartedException() {
    }

    public GameAlreadyStartedException(String message) {
        super(message);
    }

    public GameAlreadyStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameAlreadyStartedException(Throwable cause) {
        super(cause);
    }

    public GameAlreadyStartedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

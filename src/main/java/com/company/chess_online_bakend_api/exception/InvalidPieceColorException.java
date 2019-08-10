/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.exception;

public class InvalidPieceColorException extends IllegalArgumentException {

    public InvalidPieceColorException() {
    }

    public InvalidPieceColorException(String s) {
        super(s);
    }
}

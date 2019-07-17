package com.company.chess_online_bakend_api.exception;

public class InvalidPieceColorException extends IllegalArgumentException {

    public InvalidPieceColorException() {
    }

    public InvalidPieceColorException(String s) {
        super(s);
    }
}

/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model.enums;

import com.company.chess_online_bakend_api.exception.InvalidPieceColorException;

public enum PieceColor {
    BLACK, WHITE;

    public static PieceColor fromValue(String color) throws InvalidPieceColorException {

        color = color.toUpperCase();

        switch (color) {
            case "WHITE":
                return WHITE;
            case "BLACK":
                return BLACK;
            default:
                throw new InvalidPieceColorException("Valid colors are: WHITE or BLACK");
        }
    }
}

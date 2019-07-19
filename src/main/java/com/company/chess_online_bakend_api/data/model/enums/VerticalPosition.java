package com.company.chess_online_bakend_api.data.model.enums;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public enum VerticalPosition {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8");

    String positionString;

    private static List<VerticalPosition> positionList = Arrays.asList(ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT);

    VerticalPosition(String position) {
        this.positionString = position;
    }

    @Override
    public String toString() {
        return positionString;
    }


    @Nullable
    public static VerticalPosition getPosition(String positionString) {

        if (positionString == null) {
            return null;
        }

        positionString = positionString.toUpperCase();

        switch (positionString) {
            case "1":
                return ONE;
            case "2":
                return TWO;
            case "3":
                return THREE;
            case "4":
                return FOUR;
            case "5":
                return FIVE;
            case "6":
                return SIX;
            case "7":
                return SEVEN;
            case "8":
                return EIGHT;
            default:
                return null;
        }
    }

    public static ListIterator<VerticalPosition> getPositionIterator(VerticalPosition startValue) {
        var positionIterator = positionList.listIterator();

        if (startValue == null) {
            return positionIterator;
        }

        for (int i = 0; i < countNeededIteratorMoves(startValue); i++) {
            positionIterator.next();
        }

        return positionIterator;
    }

    private static int countNeededIteratorMoves(VerticalPosition position) {
        int movesNeeded = 0;
        for (VerticalPosition listPosition : positionList) {
            if (listPosition == position) {
                return movesNeeded;
            }
            movesNeeded++;
        }
        return 0;
    }
}

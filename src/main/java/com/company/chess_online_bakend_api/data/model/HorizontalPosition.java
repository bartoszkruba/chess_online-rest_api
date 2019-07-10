package com.company.chess_online_bakend_api.data.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public enum HorizontalPosition {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    H("H");

    private String positionString;

    HorizontalPosition(String position) {
        this.positionString = position;
    }

    @Override
    public String toString() {
        return positionString;
    }

    @Nullable
    public static HorizontalPosition getPosition(String positionString) {

        if (positionString == null) {
            return null;
        }

        positionString = positionString.toUpperCase();

        switch (positionString) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "D":
                return D;
            case "E":
                return E;
            case "F":
                return F;
            case "G":
                return G;
            case "H":
                return H;
            default:
                return null;
        }
    }
}

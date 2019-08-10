/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.data.model.enums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;

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
    private static List<HorizontalPosition> positionList = Arrays.asList(A, B, C, D, E, F, G, H);

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

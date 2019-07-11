package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public class PositionUtil {

    // TODO: 2019-07-11 write tests

    @Nullable
    public static String getPositionString(HorizontalPosition hPos, VerticalPosition vPos) {

        log.debug("Converting positions into string");

        if (hPos == null || vPos == null) {
            return null;
        }

        return hPos.toString() + vPos.toString();
    }

// TODO: 2019-07-11 write tests

    @Nullable
    public static HorizontalPosition getHorizontalPosition(String position) {
        if (position == null || position.length() != 2) {
            return null;
        }
        return HorizontalPosition.getPosition(position.substring(0, 1));
    }

    @Nullable
    public static VerticalPosition getVerticalPosition(String position) {
        if (position == null || position.length() != 2) {
            return null;
        }
        return VerticalPosition.getPosition(position.substring(1, 2));
    }
}

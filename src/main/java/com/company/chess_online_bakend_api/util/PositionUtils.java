package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public class PositionUtils {

    @Nullable
    public static String getPositionString(HorizontalPosition hPos, VerticalPosition vPos) {

        log.debug("Converting positions to string");

        if (hPos == null || vPos == null) {
            return null;
        }

        return hPos.toString() + vPos.toString();
    }

    @Nullable
    public static HorizontalPosition getHorizontalPosition(String position) {

        log.debug("Converting position string to HorizontalPosition");

        if (position == null || position.length() != 2) {
            return null;
        }
        return HorizontalPosition.getPosition(position.substring(0, 1));
    }

    @Nullable
    public static VerticalPosition getVerticalPosition(String position) {

        log.debug("Converting position string to VerticalPosition");

        if (position == null || position.length() != 2) {
            return null;
        }

        return VerticalPosition.getPosition(position.substring(1, 2));
    }
}

package com.company.chess_online_bakend_api.util;

import com.company.chess_online_bakend_api.data.model.enums.HorizontalPosition;
import com.company.chess_online_bakend_api.data.model.enums.VerticalPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PositionUtilsTest {

    @Test
    void testGetPositionStringHorizontalPositionNull() {
        assertNull(PositionUtils.getPositionString(null, VerticalPosition.EIGHT));
    }

    @Test
    void testGetPositionStringVerticalPositionNull() {
        assertNull(PositionUtils.getPositionString(HorizontalPosition.H, null));
    }

    @Test
    void testGetPosition() {
        String position = PositionUtils.getPositionString(HorizontalPosition.A, VerticalPosition.ONE);

        assertEquals("A1", position);
    }

    @Test
    void testGetHorizontalPositionNullValue() {
        assertNull(PositionUtils.getHorizontalPosition(null));
    }

    @Test
    void testGetHorizontalPositionTooShort() {
        assertNull(PositionUtils.getHorizontalPosition("A"));
    }

    @Test
    void testGetHorizontalPositionTooLong() {
        assertNull(PositionUtils.getHorizontalPosition("A21"));
    }

    @Test
    void testGetHorizontalPosition() {

        HorizontalPosition position = PositionUtils.getHorizontalPosition("B2");
        assertEquals(HorizontalPosition.B, position);
    }

    @Test
    void testGetHorizontalPositionInvalidValue() {
        assertNull(PositionUtils.getHorizontalPosition("I2"));
    }


    @Test
    void testGetVerticalPositionNullValue() {
        assertNull(PositionUtils.getVerticalPosition(null));
    }

    @Test
    void testGetVerticalPositionTooShort() {
        assertNull(PositionUtils.getVerticalPosition("A"));
    }

    @Test
    void testGetVerticalPositionTooLong() {
        assertNull(PositionUtils.getVerticalPosition("A21"));
    }

    @Test
    void testGetVerticalPosition() {

        VerticalPosition position = PositionUtils.getVerticalPosition("B2");
        assertEquals(VerticalPosition.TWO, position);
    }

    @Test
    void testGetVerticalPositionInvalidValue() {
        assertNull(PositionUtils.getVerticalPosition("I9"));
    }

}
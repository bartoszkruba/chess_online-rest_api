package com.company.chess_online_bakend_api.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerticalPositionTest {

    @Test
    void toStringTest() {
        assertEquals("1", VerticalPosition.ONE.toString());
    }

    @Test
    void getPositionTest() {
        assertEquals(VerticalPosition.ONE, VerticalPosition.getPosition("1"));
    }

    @Test
    void getPositionInvalidValue() {
        assertNotNull(VerticalPosition.getPosition("-1"));
    }

    @Test
    void getPositionNullValue() {
        assertNull(VerticalPosition.getPosition(null));
    }
}
package com.company.chess_online_bakend_api.data.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HorizontalPositionTest {

    @Test
    void toStringTest() {
        assertEquals("A", HorizontalPosition.A.toString());
    }

    @Test
    void getPositionTest() {
        assertEquals(HorizontalPosition.A, HorizontalPosition.getPosition("A"));
    }

    @Test
    void getPositionInvalidValue() {
        assertNull(HorizontalPosition.getPosition("z"));
    }

    @Test
    void getPositionNullValue() {
        assertNull(HorizontalPosition.getPosition(null));
    }

    @Test
    void getPositionIterator() {
        var iterator = HorizontalPosition.getPositionIterator(HorizontalPosition.B);
        assertEquals(HorizontalPosition.B, iterator.next());

        iterator = HorizontalPosition.getPositionIterator(HorizontalPosition.B);
        assertEquals(HorizontalPosition.A, iterator.previous());
    }

    @Test
    void getPositionIteratorNullStartValue() {
        var iterator = HorizontalPosition.getPositionIterator(null);
        assertEquals(HorizontalPosition.A, iterator.next());
    }
}
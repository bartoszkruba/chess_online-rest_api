/*
 * 7/26/19 7:15 PM. Created by Bartosz Kruba.
 */

/*
 * 7/26/19 7:12 PM. Created by Bartosz Kruba.
 */

package com.company.chess_online_bakend_api.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void capitalizeFirstLetter() {

        String testString = "test string";

        String capitalizedString = StringUtils.capitalizeFirstLetter(testString);

        assertEquals("T", capitalizedString.substring(0, 1));
        assertEquals("est string", capitalizedString.substring(1, 11));
    }
}
package com.econ.platformcloud.codegenerator.gradle;

import org.junit.Test;

public class AutoFillTest {
    public int add(int x, int y) {
        return x+y;
    }

    @Test
    public void testAdd() {
        int x = 0;

        int y = 0;

        add(x, y);
    }
}

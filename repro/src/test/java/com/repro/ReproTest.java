package com.repro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class ReproTest {

    @Test
    public void test() {
        Repro repro = new Repro(5);

        assertEquals(5, repro.getSomeField());

        repro.setSomeField(6);

        assertEquals(6, repro.getSomeField());
    }
}

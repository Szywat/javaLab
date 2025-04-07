package test;

import main.Stos;
import org.junit.*;
import org.junit.Before;

import java.util.Stack;

import static org.junit.Assert.*;

public class StosTest {
    Stos sut;

    @Before
    public void setUp() {
        sut = new Stos();
    }

    @Test
    public void testGet() {
        sut.sPush("value1");
        sut.sPush("value2");
        Stack a = sut.sGet();
        System.out.println(a);
    }

    @Test
    public void testPush() {
        sut.sPush("value");
        assertEquals("[value]", sut.sGet());
    }

    @Test
    public void testPushEmpty() {
        sut.sPush("");
        sut.sPush("");
        assertEquals("[]", sut.sGet());
    }

    @Test
    public void testPop() {
        sut.sPush("value");
        assertEquals("value", sut.sPop());
        assertEquals("[]", sut.sGet());
    }

    @Test
    public void testPopEmpty() {
        assertEquals("Stos jest pusty!", sut.sPop());
    }

    @Test
    public void testPeek() {
        sut.sPush("value");
        assertEquals("value", sut.sPeek());
        assertEquals("[value]", sut.sGet());
    }

}

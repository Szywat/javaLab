package test;

import main.RPN;
import main.Stos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RPNTest {
    private RPN rpn;
    private Stos stos;

    @BeforeEach
    void setUp() {
        rpn = new RPN();
        stos = new Stos();
    }

    @Test
    void testAddition() {
        stos.sPush("3");
        stos.sPush("4");
        stos.sPush("+");
        assertEquals("7", rpn.evaluate("3 4 +"));
    }

    @Test
    void testSubtraction() {
        stos.sPush("10");
        stos.sPush("5");
        stos.sPush("-");
        assertEquals("5", rpn.evaluate("10 5 -"));
    }

    @Test
    void testMultiplication() {
        stos.sPush("4");
        stos.sPush("5");
        stos.sPush("*");
        assertEquals("20", rpn.evaluate("4 5 *"));
    }
    
    @Test
    void testUnknownOperator() {
        stos.sPush("3");
        stos.sPush("4");
        stos.sPush("%");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> rpn.evaluate("3 4 %"));
        assertEquals("Nieznany operator", exception.getMessage());
    }
}

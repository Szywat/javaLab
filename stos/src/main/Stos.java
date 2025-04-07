package main;

import java.util.Stack;

public class Stos {
    private final Stack<String> s = new Stack<>();

    public void sPush(String value) {
        if (!value.isEmpty()) {
            s.push(value);
        }
    }

    public String sPop() {
        if (!s.isEmpty()) {
            return s.pop();
        } else {
            return "Stos jest pusty!";
        }
    }

    public String sPeek() {
        if (!s.isEmpty()) {
            return s.peek();
        } else {
            return "Stos jest pusty!";
        }
    }

    public Stack sGet() {
        return s;
    }

}

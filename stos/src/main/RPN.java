package main;

public class RPN {
    private final Stos stack;

    public RPN() {
        this.stack = new Stos();
    }

    public String evaluate(String expression) {
        String[] tokens = expression.split("\\s+");

        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                stack.sPush(token);
            } else if (isOperator(token)) {
                int b = Integer.parseInt(stack.sPop());
                int a = Integer.parseInt(stack.sPop());
                int result = applyOperator(a, b, token);
                stack.sPush(String.valueOf(result));
            } else {
                throw new IllegalArgumentException("Nieznany operator");
            }
        }

        return stack.sPop();
    }

    private boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token);
    }

    private int applyOperator(int a, int b, String operator) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            default -> throw new IllegalArgumentException("Nieznany operator: " + operator);
        };
    }
}
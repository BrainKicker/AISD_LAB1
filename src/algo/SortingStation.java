package algo;

import containers.ArrayList;
import containers.Stack;
import containers.UnmodifiableArrayList;

public class SortingStation {


    private static final int UNDEFINED = -1;

    private static final char PROPERTIES_DIVIDER = ':';
    private static final String PROPERTIES_DIVIDER_STR = String.valueOf(PROPERTIES_DIVIDER);
    private static final char LEFT_ASSOCIATIVE_SYMBOL = 'l';
    private static final String LEFT_ASSOCIATIVE_SYMBOL_STR = String.valueOf(LEFT_ASSOCIATIVE_SYMBOL);
    private static final char RIGHT_ASSOCIATIVE_SYMBOL = 'r';
    private static final String RIGHT_ASSOCIATIVE_SYMBOL_STR = String.valueOf(RIGHT_ASSOCIATIVE_SYMBOL);


    public static final String DEFAULT_NUMBER_CHARS = "0123456789.-+";
    public static final UnmodifiableArrayList<String>
            DEFAULT_FUNCTIONS
                    = new UnmodifiableArrayList<>("sin:1", "cos:1"),
            DEFAULT_ARGUMENT_DIVIDERS
                    = new UnmodifiableArrayList<>(","),
            DEFAULT_OPERATORS
                    = new UnmodifiableArrayList<>("+:2:l:1", "-:2:l:1", "*:2:l:2", "/:2:l:2", "^:2:l:3", "!:1:r:4"),
            DEFAULT_OPENING_BRACKETS
                    = new UnmodifiableArrayList<>("("),
            DEFAULT_CLOSING_BRACKETS
                    = new UnmodifiableArrayList<>(")");


    /**
     * Examples:
     * sin:1 - function 'sin' has 1 argument
     */
    public static String functionDescription(String name, int argumentsCount) {
        if (argumentsCount < 0)
            throw new IllegalArgumentException();
        return name + PROPERTIES_DIVIDER_STR + argumentsCount;
    }

    /**
     * Examples:
     * +:2:l:1 - operator '+' has 2 arguments, is left associative and has priority 1
     * *:2:l:2 - operator '*' has 2 arguments, is left associative and has priority 2
     * !:1:r:4 - operator '!' has 1 argument, is right associative and has priority 4
     */
    public static String operatorDescription(String name, int argumentsCount, boolean leftAssociative, int priority) {
        if (argumentsCount != 1 && argumentsCount != 2)
            throw new IllegalArgumentException();
        return
                name + PROPERTIES_DIVIDER_STR
                        + argumentsCount + PROPERTIES_DIVIDER_STR
                        + (leftAssociative ? LEFT_ASSOCIATIVE_SYMBOL_STR : RIGHT_ASSOCIATIVE_SYMBOL_STR) + PROPERTIES_DIVIDER_STR
                        + priority;
    }


    private static class Token implements Comparable<Token> {

        public enum Type {
            NUMBER,
            FUNCTION,
            ARGUMENT_DIVIDER,
            OPERATOR,
            OPENING_BRACKET,
            CLOSING_BRACKET
        }

        public Type type = null;
        public String value = null;
        public double numberValue = Double.NaN;
        public int argumentsCount = UNDEFINED;
        public boolean leftAssociative = false;
        public int priority = UNDEFINED;

        /**
         * General case.
         */
        public Token(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        /**
         * Number.
         */
        public Token(String value, double numberValue) {
            this.type = Type.NUMBER;
            this.value = value;
            this.numberValue = numberValue;
        }

        /**
         * Function.
         */
        public Token(String value, int argumentsCount) {
            this.type = Type.FUNCTION;
            this.value = value;
            this.argumentsCount = argumentsCount;
        }

        /**
         * Operator.
         */
        public Token(String value, int argumentsCount, boolean leftAssociative, int priority) {
            this.type = Type.OPERATOR;
            this.value = value;
            this.argumentsCount = argumentsCount;
            this.leftAssociative = leftAssociative;
            this.priority = priority;
        }

        @Override
        public int compareTo(Token o) {
            if (type != o.type)
                throw new RuntimeException();
            return priority - o.priority;
        }
    }


    public static String proceedExpression(String expression) {
        return proceedExpression(expression,
                DEFAULT_NUMBER_CHARS,
                DEFAULT_FUNCTIONS,
                DEFAULT_ARGUMENT_DIVIDERS,
                DEFAULT_OPERATORS,
                DEFAULT_OPENING_BRACKETS,
                DEFAULT_CLOSING_BRACKETS);
    }


    public static String proceedExpression(String expression,
                                           String numberChars,
                                           ArrayList<String> functions,
                                           ArrayList<String> argumentDividers,
                                           ArrayList<String> operators,
                                           ArrayList<String> openingBrackets,
                                           ArrayList<String> closingBrackets) {
        return new SortingStation(expression, numberChars,
                functions, argumentDividers, operators,
                openingBrackets, closingBrackets).proceedExpression();
    }


    private final String numberChars;
    private final UnmodifiableArrayList<String>
            functions,
            argumentDividers,
            operators,
            openingBrackets,
            closingBrackets;
    private final UnmodifiableArrayList<String>
            functionNames,
            operatorNames;

    private String expression;
    private int index = 0;

    private final Stack<Token> stack = new Stack<>();
    private final ArrayList<Token> allTokens = new ArrayList<>();


    private SortingStation(String expression) {
        this(expression,
                DEFAULT_NUMBER_CHARS,
                DEFAULT_FUNCTIONS,
                DEFAULT_ARGUMENT_DIVIDERS,
                DEFAULT_OPERATORS,
                DEFAULT_OPENING_BRACKETS,
                DEFAULT_CLOSING_BRACKETS);
    }

    private SortingStation(String expression,
                           String numberChars,
                           ArrayList<String> functions,
                           ArrayList<String> argumentDividers,
                           ArrayList<String> operators,
                           ArrayList<String> openingBrackets,
                           ArrayList<String> closingBrackets) {
        this(expression,
                numberChars,
                new UnmodifiableArrayList<>(functions),
                new UnmodifiableArrayList<>(argumentDividers),
                new UnmodifiableArrayList<>(operators),
                new UnmodifiableArrayList<>(openingBrackets),
                new UnmodifiableArrayList<>(closingBrackets));
    }

    private SortingStation(String expression,
                           String numberChars,
                           UnmodifiableArrayList<String> functions,
                           UnmodifiableArrayList<String> argumentDividers,
                           UnmodifiableArrayList<String> operators,
                           UnmodifiableArrayList<String> openingBrackets,
                           UnmodifiableArrayList<String> closingBrackets) {
        this.expression = expression;
        this.numberChars = numberChars;
        this.functions = functions;
        this.argumentDividers = argumentDividers;
        this.operators = operators;
        this.openingBrackets = openingBrackets;
        this.closingBrackets = closingBrackets;
        this.functionNames = new Object() {
            UnmodifiableArrayList<String> collectFunctionNames() {
                ArrayList<String> names = new ArrayList<>(functions.size());
                functions.forEach(s -> names.add(s.substring(0, s.indexOf(PROPERTIES_DIVIDER))));
                return new UnmodifiableArrayList<>(names);
            }
        }.collectFunctionNames();
        this.operatorNames = new Object() {
            UnmodifiableArrayList<String> collectOperatorNames() {
                ArrayList<String> names = new ArrayList<>(operators.size());
                operators.forEach(s -> names.add(s.substring(0, s.indexOf(PROPERTIES_DIVIDER))));
                return new UnmodifiableArrayList<>(names);
            }
        }.collectOperatorNames();
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    private String proceedExpression() {
        try {
            return proceedExpression0();
        } catch (Exception e) {
            String result = e.getMessage();
            if (result == null)
                result = "Mistake in expression";
            return result;
        }
    }

    private String proceedExpression0() {

        index = 0;
        stack.clear();
        allTokens.clear();

        StringBuilder result = new StringBuilder();

        while (hasNext()) {

            Token token = nextToken();

            switch (token.type) {
                case NUMBER -> result.append(" ").append(token.value);
                case FUNCTION -> {
                    if (token.argumentsCount == 0)
                        result.append(" ").append(token.value);
                    else
                        stack.push(token);
                }
                case ARGUMENT_DIVIDER -> {
                    while (true) {
                        if (stack.empty())
                            throw new IllegalArgumentException();
                        if (stack.top().type == Token.Type.OPENING_BRACKET)
                            break;
                        if (stack.top().type != Token.Type.OPERATOR)
                            throw new IllegalArgumentException();
                        result.append(" ").append(stack.pop().value);
                    }
                }
                case OPERATOR -> {

                    while (!stack.empty()
                            && stack.top().type == Token.Type.OPERATOR
                            && (stack.top().compareTo(token) > 0
                            || (stack.top().compareTo(token) == 0
                            && token.leftAssociative
                    )
                    )
                    ) {
                        result.append(" ").append(stack.pop().value);
                    }
                    stack.push(token);
                }
                case OPENING_BRACKET -> stack.push(token);
                case CLOSING_BRACKET -> {
                    while (true) {
                        if (!stack.empty() && stack.top().type == Token.Type.OPENING_BRACKET)
                            break;
                        if (stack.empty() || stack.top().type != Token.Type.OPERATOR)
                            throw new IllegalArgumentException();
                        result.append(" ").append(stack.pop().value);
                    }
                    stack.pop(); // pop '('
                    if (!stack.empty() && stack.top().type == Token.Type.FUNCTION)
                        result.append(" ").append(stack.pop().value);
                }
            }

            allTokens.add(token);
        }

        for (int i = 1; i < allTokens.size() - 1; ++i) {
            Token cur = allTokens.get(i);
            if (cur.type == Token.Type.OPERATOR) {
                Token.Type prevType = allTokens.get(i - 1).type;
                Token.Type nextType = allTokens.get(i + 1).type;
                if ((prevType == Token.Type.OPERATOR && allTokens.get(i - 1).argumentsCount == 2)
                        || prevType == Token.Type.FUNCTION
                        || prevType == Token.Type.ARGUMENT_DIVIDER
                        || prevType == Token.Type.OPENING_BRACKET
                        ||
                        cur.argumentsCount == 2
                                && (nextType == Token.Type.OPERATOR
                                || nextType == Token.Type.ARGUMENT_DIVIDER
                                || nextType == Token.Type.OPENING_BRACKET)
                )
                    throw new IllegalArgumentException();
            } else if (cur.type == Token.Type.FUNCTION) {
                Token.Type nextType = allTokens.get(i + 1).type;
                if (cur.argumentsCount > 0 && nextType != Token.Type.OPENING_BRACKET
                        || cur.argumentsCount == 0 && nextType == Token.Type.OPENING_BRACKET)
                    throw new IllegalArgumentException();
            }
        }

        if (!allTokens.empty()) {
            Token firstToken = allTokens.getFirst();
            if (firstToken.type == Token.Type.OPERATOR && firstToken.argumentsCount != 1 && firstToken.leftAssociative)
                throw new IllegalArgumentException();
            Token lastToken = allTokens.getLast();
            if (lastToken.type == Token.Type.FUNCTION && lastToken.argumentsCount > 0)
                throw new IllegalArgumentException();
            if (lastToken.type == Token.Type.OPERATOR && !(lastToken.argumentsCount == 1 && !lastToken.leftAssociative))
                throw new IllegalArgumentException();
        }

        while (!stack.empty()) {
            if (stack.top().type == Token.Type.OPENING_BRACKET)
                throw new IllegalArgumentException("More opening brackets than closing ones");
            result.append(" ").append(stack.pop().value);
        }

        return result.toString().trim();
    }


    private Token nextToken() {

        skipWhitespace();

        if (!hasNext())
            throw new RuntimeException();

        for (String cur = String.valueOf(nextChar()); true;) {

            if (cur.length() == 1 && numberChars.indexOf(cur.charAt(0)) != -1) {
                if (cur.charAt(0) != '+' && cur.charAt(0) != '-' && cur.charAt(0) != '.' || numberChars.indexOf(watchNextChar()) != -1) {
                    if (allTokens.empty() || (
                            allTokens.getLast().type == Token.Type.OPERATOR
                            || allTokens.getLast().type == Token.Type.ARGUMENT_DIVIDER
                            || allTokens.getLast().type == Token.Type.OPENING_BRACKET)) {
                        while (hasNext() && watchNextChar() != '+' && watchNextChar() != '-' && numberChars.indexOf(watchNextChar()) != -1)
                            cur += nextChar();
                        try {
                            double d = Double.parseDouble(cur);
                            if (d % 1 == 0)
                                cur = String.valueOf((int)d);
                            else
                                cur = String.valueOf(d);
                            // +5 -> 5
                            return new Token(cur, d);
                        } catch (NumberFormatException nfe) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }

            for (int i = 0; i < functionNames.size(); i++) {
                if (cur.equals(functionNames.get(i))) {
                    return new Token(cur,
                            Integer.parseInt(
                                    functions.get(i).substring(
                                            functions.get(i).indexOf(PROPERTIES_DIVIDER) + 1
                                    )));
                }
            }

            for (int i = 0; i < operatorNames.size(); i++) {
                if (cur.equals(operatorNames.get(i))) {
                    String operatorString = operators.get(i);
                    int firstIndex = operatorString.indexOf(':');
                    int secondIndex = operatorString.indexOf(':', firstIndex + 1);
                    int thirdIndex = operatorString.indexOf(':', secondIndex + 1);
                    return new Token(cur,
                            Integer.parseInt(operatorString.substring(firstIndex + 1, secondIndex)),
                            operatorString.substring(secondIndex + 1, thirdIndex).equals(LEFT_ASSOCIATIVE_SYMBOL_STR),
                            Integer.parseInt(operatorString.substring(thirdIndex + 1)));
                }
            }

            for (int i = 0; i < openingBrackets.size(); i++)
                if (cur.equals(openingBrackets.get(i)))
                    return new Token(Token.Type.OPENING_BRACKET, cur);
            for (int i = 0; i < closingBrackets.size(); i++)
                if (cur.equals(closingBrackets.get(i)))
                    return new Token(Token.Type.CLOSING_BRACKET, cur);
            for (int i = 0; i < argumentDividers.size(); i++)
                if (cur.equals(argumentDividers.get(i)))
                    return new Token(Token.Type.ARGUMENT_DIVIDER, cur);

            if (!hasNext() || Character.isWhitespace(watchNextChar()))
                throw new IllegalArgumentException();

            cur += nextChar();
        }
    }


    private void skipWhitespace() {
        if (index < 0 || index >= expression.length())
            return;
        while (index != expression.length() && Character.isWhitespace(expression.charAt(index)))
            ++index;
    }

    private boolean hasNext() {
        return index < expression.length();
    }

    private char nextChar() {
        char cur = expression.charAt(index);
        ++index;
        return cur;
    }

    private char watchNextChar() {
        return expression.charAt(index);
    }
}
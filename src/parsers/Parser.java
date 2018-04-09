package parsers;

import exceptions.IllegalCharacterException;

class Parser {
    String expression;
    String currentString;
    int position;

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private boolean isLowerLetter(char c) {
        return Character.isLowerCase(c);
    }

    private boolean isUpperLetter(char c) {
        return Character.isUpperCase(c);
    }

    Token nextToken() throws IllegalCharacterException {
        while (Character.isWhitespace(currentChar())) {
            position++;
        }
        if (position >= expression.length()) {
            return Token.END;
        }
        char oldChar = currentChar();
        position++;
        for (Token token : Token.values()) {
            if (token.getName().equals(Character.toString(oldChar))) {
                return token;
            }
        }
        if (isLowerLetter(oldChar)) {
            currentString = String.valueOf(oldChar);
            while (isLowerLetter(currentChar()) || isDigit(currentChar())) {
                currentString += currentChar();
                position++;
            }
            return Token.LOWER_CASE_LETTER;
        }
        if (isUpperLetter(oldChar)) {
            currentString = String.valueOf(oldChar);
            while (isUpperLetter(currentChar()) || isDigit(currentChar())) {
                currentString += currentChar();
                position++;
            }
            return Token.UPPER_CASE_LETTER;
        }
        throw new IllegalCharacterException(position);
    }

    private char currentChar() {
        return position >= expression.length() ? '$' : expression.charAt(position);
    }

    enum Token {

        OPEN_BRACKET("("),
        CLOSE_BRACKET(")"),
        LAMBDA("\\"),
        DOT("."),
        LOWER_CASE_LETTER("low"),
        UPPER_CASE_LETTER("high"),
        COMMA(","),
        EQUALS("::="),
        SINGLE_QUOTE("\'"),
        END("eof");

        private final String name;

        Token(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

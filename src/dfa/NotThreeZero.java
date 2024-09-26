package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string does not contain three consecutive {@code '0'} characters.
 */
public final class NotThreeZero {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The last character found is {@code '1'}, and no sequence of three or more consecutive {@code '0'} characters
         * has been found.
         */
        NOT_FOUND,

        /**
         * A {@code '0'} character has been found.
         */
        FOUND_ONE,

        /**
         * A sequence of two consecutive {@code '0'} characters have been found.
         */
        FOUND_TWO,

        /**
         * A sequence of three consecutive {@code '0'} characters has been found.
         */
        FOUND_THREE
    }

    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return c == '0' || c == '1';
    }

    /**
     * Scans the input string and checks whether the string is valid for the automaton.
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is valid; otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.NOT_FOUND;  // Start state
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i++);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case NOT_FOUND:
                    if (c == '0') state = States.FOUND_ONE;
                    break;
                case FOUND_ONE:
                    if (c == '0') state = States.FOUND_TWO;
                    else if (c == '1') state = States.NOT_FOUND;
                    break;
                case FOUND_TWO:
                    if (c == '0') state = States.FOUND_THREE;
                    else if (c == '1') state = States.NOT_FOUND;
                    break;
                case FOUND_THREE:
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        return state != States.FOUND_THREE;
    }

    public static void main(String[] args) {
        if (args.length < 1)
            throw new InputMismatchException("An input string must be provided");

        System.out.println("The input string is " + (scan(args[0]) ? "valid" : "not valid"));
    }
}
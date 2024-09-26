package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether the last three characters of a string contains at least an {@code 'a'} character.
 * <p>
 * The length of the string can be less than 3, but it must contain at least one {@code 'a'}.
 * The string shall also contain only {@code 'a'} and {@code 'b'} characters.
 */
public final class LastThreeA {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The string
         */
        INIT,

        /**
         * The string ends with 'a' (thus, it's valid)
         */
        A_1,

        /**
         * The string ends with 'ab' (thus, it's valid)
         */
        A_2,

        /**
         * The string ends with 'abb' (thus, it's valid)
         */
        A_3,

        /**
         * The string ends with 'bbb' (thus, it's not valid).
         */
        INVALID,
    }

    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return c == 'a' || c == 'b';
    }

    /**
     * Scans the input string and checks whether the string is valid for the automaton.
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is valid; otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.INIT;  // Start state
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case INIT:
                    if (c == 'a') state = States.A_1;
                    else state = States.INVALID;
                    break;

                case A_1:
                    if (c == 'b') state = States.A_2;
                    break;

                case A_2:
                    if (c == 'b') state = States.A_3;
                    else state = States.A_1;
                    break;

                case A_3:
                    if (c == 'b') state = States.INVALID;
                    else state = States.A_1;
                    break;

                case INVALID:
                    if (c == 'a') state = States.A_1;
                    break;

                default:
                    throw new IllegalStateException();
            }

            i++;
        }

        return state == States.A_1 || state == States.A_2 || state == States.A_3;
    }

    public static void main(String[] args) {
        if (args.length < 1)
            throw new InputMismatchException("An input string must be provided");

        try {
            System.out.println("The input string is " + (scan(args[0]) ? "valid" : "not valid"));
        } catch (InputMismatchException e) {
            System.err.println(e.getMessage());
        }
    }
}

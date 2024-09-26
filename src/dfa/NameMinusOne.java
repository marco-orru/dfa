package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is composed by a name with at most one letter replaced.
 * <p>
 * The name is case-sensitive: replacing the first character with a lowercase letter is considered one replacement.
 * Adding or removing a character will invalidate the string: only replacements are considered valid.
 */
public final class NameMinusOne {
    private enum States {
        /**
         * The string is valid.
         */
        VALID,

        /**
         * One replacement has occurred in the string (it's still valid).
         */
        REPLACEMENT_OCCURRED,

        /**
         * More than one replacement has occurred in the string (thus, it's invalid).
         */
        INVALID
    }

    /**
     * Scans the input string and checks whether the string is valid for the automaton.
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is valid; otherwise, {@code false}
     */
    private static boolean scan(String s, String name) {
        var state = States.VALID;  // Start state
        var i = 0;

        if (s.length() != name.length())
            return false;

        while (i < s.length()) {
            final var c = s.charAt(i);
            final var nc = name.charAt(i);

            switch (state) {
                case VALID:
                    if (c != nc) state = States.REPLACEMENT_OCCURRED;
                    break;

                case REPLACEMENT_OCCURRED:
                    if (c != nc) state = States.INVALID;
                    break;

                case INVALID:
                    break;

                default:
                    throw new IllegalStateException();
            }

            i++;
        }

        return state == States.VALID || state == States.REPLACEMENT_OCCURRED;
    }

    public static void main(String[] args) {
        if (args.length < 1)
            throw new InputMismatchException("An input string must be provided");

        if (args.length < 2)
            throw new InputMismatchException("A name must be provided");

        System.out.println("The input string is " + (scan(args[0], args[1]) ? "valid" : "not valid"));
    }
}

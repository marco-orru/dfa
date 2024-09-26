package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string checks whether a string does not contain any comment,
 * or contains a valid block comment.
 * <p>
 * Block comments are delimited by {@code /*} and <code>*&#47;</code>.
 * The alphabet is composed by {@code 'a'}, {@code '/'} and {@code '*'}.
 */
public final class ContainsBlockComment {
    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     *
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return c == 'a' || c == '*' || c == '/';
    }

    /**
     * Scans the input string and checks whether the string is valid for the automaton.
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is valid; otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.VALID;
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case VALID:
                    if (c == '/') state = States.SLASH;
                    break;

                case SLASH:
                    if (c == '*') state = States.IN_COMMENT;
                    else if (c == 'a') state = States.VALID;
                    break;

                case IN_COMMENT:
                    if (c == '*') state = States.END_ASTERISK;
                    break;

                case END_ASTERISK:
                    if (c == '/') state = States.VALID;
                    else if (c == 'a') state = States.IN_COMMENT;
                    break;

                default:
                    throw new IllegalStateException();
            }

            i++;
        }

        return state == States.VALID;
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

    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The last scanned character is {@code '*'}.
         */
        SLASH,

        /**
         * The string contains a non-terminated block comment.
         */
        IN_COMMENT,

        /**
         * The last scanned character is {@code *} when the previous substring was in a comment.
         */
        END_ASTERISK,

        /**
         * The string contains a valid block comment.
         */
        VALID
    }
}

package dfa;

import javax.swing.plaf.nimbus.State;
import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is a block comment without nesting
 * <p>
 * Block comments are delimited by {@code /*} and <code>*&#47;</code>.
 * Because they don't nest, there shall be only one sequence of <code>*&#47;</code> in the comment.
 * The alphabet is composed by {@code 'a'}, {@code '/'} and {@code '*'}.
 */
public final class BlockComment {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The string is yet to be scanned.
         */
        INIT,

        /**
         * The first character of the string is {@code '/'}.
         */
        SLASH,

        /**
         * The string starts with {@code /*}.
         */
        IN_COMMENT,

        /**
         * The last character is {@code *} when the previous substring was in a comment.
         */
        END_ASTERISK,

        /**
         * The string is a valid block comment.
         */
        VALID,

        /**
         * The string is invalid.
         */
        INVALID
    }

    /**
     * Checks whether the specified character belongs to the automaton alphabet.
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
        var state = States.INIT;
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case INIT:
                    if (c == '/') state = States.SLASH;
                    else state = States.INVALID;
                    break;

                case SLASH:
                    if (c == '*') state = States.IN_COMMENT;
                    else state = States.INVALID;
                    break;

                case IN_COMMENT:
                    if (c == '*') state = States.END_ASTERISK;
                    break;

                case END_ASTERISK:
                    if (c == '/') state = States.VALID;
                    else if (c == 'a') state = States.IN_COMMENT;
                    break;

                case VALID:
                    state = States.INVALID;
                    break;

                case INVALID:
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
}

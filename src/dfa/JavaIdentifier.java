package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is a valid Java identifier.
 * <p>
 * A valid java identifier is a non-empty sequence of digits, alphabetical characters and the character {@code '_'},
 * that does not start with a digit, and it's not made of just {@code '_'} characters.
 */
public final class JavaIdentifier {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The identifier is empty.
         */
        EMPTY_STRING,

        /**
         * The identifier starts with a digit (thus, it's invalid).
         */
        FIRST_DIGIT,

        /**
         * The identifier is a sequence of underscores.
         */
        UNDERSCORE_PREFIX,

        /**
         * The identifier is a valid Java identifier.
         */
        VALID_ID,
    }

    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    /**
     * Scans the input string and checks whether it is a valid Java identifier
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is a valid Java identifier;
     *         otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.EMPTY_STRING;  // Start state
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i++);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case EMPTY_STRING:
                    if (c == '_') state = States.UNDERSCORE_PREFIX;
                    else if (Character.isDigit(c)) state = States.FIRST_DIGIT;
                    else if (Character.isLetter(c)) state = States.VALID_ID;
                    break;
                case UNDERSCORE_PREFIX:
                    if (Character.isLetterOrDigit('_')) state = States.VALID_ID;
                    break;
                case FIRST_DIGIT, VALID_ID:
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        return state == States.VALID_ID;
    }
    
    public static void main(String[] args) {
        if (args.length < 1)
            throw new InputMismatchException("An input string must be provided");

        System.out.println("The input string is " + (scan(args[0]) ? "valid" : "not valid"));
    }
}

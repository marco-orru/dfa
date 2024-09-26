package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is a student identifier (a number) immediately followed by a surname.
 * <p>
 * In particolar, a string is considered valid if:
 * <ul>
 *     <li>The surname starts with a letter between {@code 'A'} and {@code 'K'} and the student id is even.</li>
 *     <li>The surname starts with a letter between {@code 'L} and {@code 'Z'} and the student identifier is odd.</li>
 * </ul>
 * <p>
 * The surname string is case-sensitive (i.e., must start with an uppercase letter).
 */
public final class StudentId {
    /**
     * Defines the states of the automaton.
     */
    enum States {
        /**
         * Initial state: the string is yet to be scanned.
         */
        INIT,

        /**
         * The last scanned character is an even digit: the student id is being scanned and (for now) it's even.
         */
        EVEN,

        /**
         * The last scanned character is an odd digit: the student id is being scanned and (for now) it's odd.
         */
        ODD,

        /**
         * The string is valid: after the student id there is a surname that starts with the correct letter.
         */
        VALID,

        /**
         * The string is not valid.
         */
        NOT_VALID
    }

    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return Character.isLetterOrDigit(c);
    }

    /**
     * Scans the input string and checks whether it is a valid Java identifier
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is a valid Java identifier;
     *         otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.INIT;  // Start state
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i++);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case INIT:
                    if (Character.isDigit(c)) {
                        // No cast to int, as ascii digits are even and odd as their numeric counterparts.
                        if (c % 2 == 0) state = States.EVEN;
                        else state = States.ODD;
                    }
                    else state = States.NOT_VALID;
                    break;
                case ODD:
                    if (Character.isDigit(c) && c % 2 == 0) state = States.EVEN;
                    else if (c >= 'A' && c <= 'K') state = States.NOT_VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.VALID;
                    else state = States.NOT_VALID;
                    break;

                case EVEN:
                    if (Character.isDigit(c) && c % 2 != 0) state = States.ODD;
                    else if (c >= 'A' && c <= 'K') state = States.VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.NOT_VALID;
                    else state = States.NOT_VALID;
                    break;

                case VALID:
                    if (Character.isDigit(c)) state = States.NOT_VALID;  // Digits after surname
                    break;

                case NOT_VALID:
                    break;

                default:
                    throw new IllegalStateException();
            }
        }

        return state == States.VALID;
    }

    public static void main(String[] args) {
        if (args.length < 1)
            throw new InputMismatchException("An input string must be provided");

        System.out.println("The input string is " + (scan(args[0]) ? "valid" : "not valid"));
    }
}

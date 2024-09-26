package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is a surname immediately followed by a valid student identifier.
 * <p>
 * In particolar, a string is considered valid if:
 * <ul>
 *     <li>The surname starts with a letter between {@code 'A'} and {@code 'K'} and the student id is even.</li>
 *     <li>The surname starts with a letter between {@code 'L} and {@code 'Z'} and the student identifier is odd.</li>
 * </ul>
 * <p>
 * The surname string is case-sensitive (i.e., must start with an uppercase letter).
 */
public final class StudentIdInv {
    /**
     * Checks whether the specified character belongs to the automaton alphabet.
     *
     * @param c The character to be checked.
     * @return {@code true} if the character is valid; otherwise, {@code false}.
     */
    private static boolean isValidCharacter(char c) {
        return Character.isLetterOrDigit(c);
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
                    if (c >= 'A' && c <= 'K') state = States.SURNAME_A;
                    else if (c >= 'L' && c <= 'Z') state = States.SURNAME_B;
                    else state = States.INVALID;
                    break;

                case SURNAME_A:
                    if (Character.isDigit(c)) {
                        // No cast to int, as ascii digits are even and odd as their numeric counterparts.
                        if (c % 2 == 0) state = States.SURNAME_A_EVEN;
                        else state = States.SURNAME_A_ODD;
                    }
                    break;

                case SURNAME_B:
                    if (Character.isDigit(c)) {
                        // No cast to int, as ascii digits are even and odd as their numeric counterparts.
                        if (c % 2 == 0) state = States.SURNAME_B_EVEN;
                        else state = States.SURNAME_B_ODD;
                    }
                    break;

                case SURNAME_A_ODD:
                    if (Character.isDigit(c) && c % 2 == 0) state = States.SURNAME_A_EVEN;
                    else if (Character.isLetter(c)) state = States.INVALID;
                    break;

                case SURNAME_A_EVEN:
                    if (Character.isDigit(c) && c % 2 != 0) state = States.SURNAME_A_ODD;
                    else if (Character.isLetter(c)) state = States.INVALID;
                    break;

                case SURNAME_B_ODD:
                    if (Character.isDigit(c) && c % 2 == 0) state = States.SURNAME_B_EVEN;
                    else if (Character.isLetter(c)) state = States.INVALID;
                    break;

                case SURNAME_B_EVEN:
                    if (Character.isDigit(c) && c % 2 != 0) state = States.SURNAME_B_ODD;
                    else if (Character.isLetter(c)) state = States.INVALID;
                    break;

                case INVALID:
                    break;

                default:
                    throw new IllegalStateException();
            }

            i++;
        }

        return state == States.SURNAME_A_EVEN || state == States.SURNAME_B_ODD;
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
         * Initial state: the string is yet to be scanned.
         */
        INIT,

        /**
         * The string starts with a surname whose first letter is between {@code 'A'} and {@code 'K'}.
         */
        SURNAME_A,

        /**
         * The string starts with a surname whose first letter is between {@code 'L'} and {@code 'Z'}.
         */
        SURNAME_B,

        /**
         * The last scanned substring is an even student id after a surname whose first letter is between {@code 'A'} and {@code 'K'}.
         */
        SURNAME_A_EVEN,

        /**
         * The last scanned substring is an odd student id after a surname whose first letter is between {@code 'A'} and {@code 'K'}.
         */
        SURNAME_A_ODD,

        /**
         * The last scanned substring is an even student id after a surname whose first letter is between {@code 'L'} and {@code 'Z'}.
         */
        SURNAME_B_EVEN,

        /**
         * The last scanned substring is an odd student id after a surname whose first letter is between {@code 'L'} and {@code 'Z'}.
         */
        SURNAME_B_ODD,

        /**
         * The string is invalid.
         */
        INVALID
    }
}

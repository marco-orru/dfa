package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is a student identifier (a number) followed by a surname, with optional
 * whitespaces before, after and/or in-between.
 * <p>
 * In particolar, a string is considered valid if:
 * <ul>
 *     <li>The surname starts with a letter between {@code 'A'} and {@code 'K'} and the student id is even.</li>
 *     <li>The surname starts with a letter between {@code 'L} and {@code 'Z'} and the student identifier is odd.</li>
 * </ul>
 * <p>
 * The surname string is case-sensitive (i.e., must start with an uppercase letter).
 * Moreover, surnames composed by two or more words (e.g., Van Dyk) can be accepted as long as the first letter of each word
 * is uppercase; the letter that determines the validity of the string is still the first one of the first word.
 */
public final class StudentIdSpace {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * Initial state: the string is yet to be scanned or starts with a space.
         */
        INIT_OR_SPACE,

        /**
         * The last scanned character is an even digit: the student id is being scanned and (for now) it's even.
         */
        EVEN,

        /**
         * The last scanned character is an odd digit: the student id is being scanned and (for now) it's odd.
         */
        ODD,

        /**
         * The last scanned substring is a sequence of whitespace characters following an even student id.
         */
        SPACE_AFTER_EVEN_ID,

        /**
         * The last scanned substring is a sequence of whitespace characters following an odd student id.
         */
        SPACE_AFTER_ODD_ID,

        /**
         * The string is valid: after the student id there is a surname that starts with the correct letter.
         */
        VALID,

        /**
         * The last scanned substring is a sequence of whitespace characters following the surname.
         */
        TRAILING_SPACE,

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
        return Character.isLetterOrDigit(c) || c == ' ';
    }

    /**
     * Scans the input string and checks whether the string is valid for the automaton.
     *
     * @param s the input string to be scanned
     * @return {@code true} if the string is valid; otherwise, {@code false}
     */
    private static boolean scan(String s) {
        var state = States.INIT_OR_SPACE;  // Start state
        var i = 0;

        while (i < s.length()) {
            final var c = s.charAt(i);

            if (!isValidCharacter(c))
                throw new InputMismatchException("Invalid character at index %d in input string %s".formatted(i, s));

            switch (state) {
                case INIT_OR_SPACE:
                    if (Character.isDigit(c)) {
                        // No cast to int, as ascii digits are even and odd as their numeric counterparts.
                        if (c % 2 == 0) state = States.EVEN;
                        else state = States.ODD;
                    }
                    else if (Character.isLetter(c)) {
                        state = States.NOT_VALID;
                    }
                    break;

                case ODD:
                    if (Character.isDigit(c) && c % 2 == 0) state = States.EVEN;
                    else if (c >= 'A' && c <= 'K') state = States.NOT_VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.VALID;
                    else if (c == ' ') state = States.SPACE_AFTER_ODD_ID;
                    else state = States.NOT_VALID;
                    break;

                case EVEN:
                    if (Character.isDigit(c) && c % 2 != 0) state = States.ODD;
                    else if (c >= 'A' && c <= 'K') state = States.VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.NOT_VALID;
                    else if (c == ' ') state = States.SPACE_AFTER_EVEN_ID;
                    else state = States.NOT_VALID;
                    break;

                case SPACE_AFTER_ODD_ID:
                    if (Character.isDigit(c)) state = States.NOT_VALID;
                    else if (c >= 'A' && c <= 'K') state = States.NOT_VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.VALID;
                    else if (Character.isLetter(c)) state = States.NOT_VALID;
                    break;

                case SPACE_AFTER_EVEN_ID:
                    if (Character.isDigit(c)) state = States.NOT_VALID;
                    else if (c >= 'A' && c <= 'K') state = States.VALID;
                    else if (c >= 'L' && c <= 'Z') state = States.NOT_VALID;
                    else if (Character.isLetter(c)) state = States.NOT_VALID;
                    break;

                case VALID:
                    if (Character.isDigit(c)) state = States.NOT_VALID;  // Digits after surname
                    else if (c == ' ') state = States.TRAILING_SPACE;
                    break;

                case TRAILING_SPACE:
                    if (Character.isLetter(c) && Character.isLowerCase(c)) state = States.NOT_VALID;
                    else if (Character.isLetter(c) && Character.isUpperCase(c)) state = States.VALID;
                    else if (Character.isDigit(c)) state = States.NOT_VALID;
                    break;

                case NOT_VALID:
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
        }    }
}

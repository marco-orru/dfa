package dfa;

import java.util.InputMismatchException;

/**
 * Implements a DFA that checks whether a string is represents a floating point constant, with optional exponent and sign.
 * <p>
 * The alphabet contains digits, the symbols {@code '+'} and {@code '-'} for signs, the letter {@code 'e'} for
 * exponentiation and the symbol {@code '.'} as decimal point. A floating point constant is composed by two segments:
 * <ol>
 *     <li>
 *         The first segment is a sequence of digits that:
 *         <ul>
 *             <li>can be preceded by a sign</li>,
 *             <li>can be followed by a dot, followed by at least one more digit</li>
 *         </ul>
 *     </li>
 *     <li>
 *         The second segment is optional and starts with the letter {@code 'e'}, followed by a sequence of digits
 *         that has the same properties as the first segment.
 *     </li>
 * </ol>
 * In both segments, it's not necessary that a decimal point is preceded by a non-empty sequence of digits.
 */
public final class FloatingPoint {
    /**
     * Defines the states of the automaton.
     */
    private enum States {
        /**
         * The string is yet to be scanned
         */
        INIT,

        /**
         * The string starts with a sign,
         */
        SIGN,

        /**
         * The last substring scanned is the integral part of the number.
         */
        INTEGRAL_PART,

        /**
         * The last character scanned is a decimal point.
         */
        DECIMAL_POINT,

        /**
         * The last substring scanned is the decimal part of the number.
         */
        DECIMAL_PART,

        /**
         * The last character scanned is the {@code 'e'} symbol, which follows a valid floating point number.
         */
        EXP,

        /**
         * The last character scanned is the sign of the exponent.
         */
        EXP_SIGN,

        /**
         * The last substring scanned is the integral part of the exponent.
         */
        EXP_INTEGRAL_PART,

        /**
         * The last character scanned is a decimal point of the exponent.
         */
        EXP_DECIMAL_POINT,

        /**
         * The last substring scanned is the decimal part of the exponent.
         */
        EXP_DECIMAL_PART,

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
        return Character.isDigit(c) || c == '.' || c == '+' || c == '-' || c == 'e';
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
                    if (c == '+' || c == '-') state = States.SIGN;
                    else if (Character.isDigit(c)) state = States.INTEGRAL_PART;
                    else if (c == '.') state = States.DECIMAL_POINT;
                    else state = States.INVALID;
                    break;

                case SIGN:
                    if (Character.isDigit(c)) state = States.INTEGRAL_PART;
                    else if (c == '.') state = States.DECIMAL_POINT;
                    else state = States.INVALID;
                    break;

                case INTEGRAL_PART:
                    if (c == '.') state = States.DECIMAL_POINT;
                    else if (c == 'e') state = States.EXP;
                    else if (c == '+' || c == '-') state = States.INVALID;
                    break;

                case DECIMAL_POINT:
                    if (Character.isDigit(c)) state = States.DECIMAL_PART;
                    else state = States.INVALID;
                    break;

                case DECIMAL_PART:
                    if (c == 'e') state = States.EXP;
                    else if (c == '+' || c == '-' || c == '.') state = States.INVALID;
                    break;

                case EXP:
                    if (c == '+' || c == '-') state = States.EXP_SIGN;
                    else if (Character.isDigit(c)) state = States.EXP_INTEGRAL_PART;
                    else if (c == '.') state = States.EXP_DECIMAL_POINT;
                    else state = States.INVALID;
                    break;

                case EXP_SIGN:
                    if (Character.isDigit(c)) state = States.EXP_INTEGRAL_PART;
                    else if (c == '.') state = States.EXP_DECIMAL_POINT;
                    else state = States.INVALID;
                    break;

                case EXP_INTEGRAL_PART:
                    if (c == '.') state = States.EXP_DECIMAL_POINT;
                    else if (c == '+' || c == '-' || c == 'e') state = States.INVALID;
                    break;

                case EXP_DECIMAL_POINT:
                    if (Character.isDigit(c)) state = States.EXP_DECIMAL_PART;
                    else state = States.INVALID;
                    break;

                case EXP_DECIMAL_PART:
                    if (c == '+' || c == '-' || c == '.' || c == 'e') state = States.INVALID;
                    break;

                case INVALID:
                    break;

                default:
                    throw new IllegalStateException();
            }

            i++;
        }

        return state == States.INTEGRAL_PART ||
                state ==  States.DECIMAL_PART ||
                state == States.EXP_INTEGRAL_PART ||
                state == States.EXP_DECIMAL_PART;
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

/*
Soroush Rouhani Esfahani
CSCB07H3
September 24th, 2023
Lab 1 - Polynomial.java

Polynomial class developed as per lab instructions.
*/

public class Polynomial {

    // Fields
    double [] coefficients;

    // Constructor with no input sets coefficients to [0]
    Polynomial() {
        coefficients = new double[1];
    }

    // Constructor receiving array of coefficient values. Copy them to the field
    Polynomial(double[] coefficientVals) {
        /* If the parameter is not null or empty, copy each value into the
        coefficients' field */
        if (coefficientVals != null && coefficientVals.length > 0) {
            coefficients = new double[coefficientVals.length];
            for (int i = 0; i < coefficients.length; i++) {
                coefficients[i] = coefficientVals[i];
            }
        } else {
            // If the parameter is null or empty, set the coefficients to [0]
            coefficients = new double[1];
        }
    }

    // Add this and other Polynomial's coefficients, return resulting Polynomial
    public Polynomial add(Polynomial other) {
        // If both Polynomials' coefficients are null, return [0]
        if (coefficients == null && other.coefficients == null) {
            return new Polynomial();
        /* If just one Polynomial has null coefficients, return a copy of the
        one that is not null */
        } else if (coefficients == null) {
            return new Polynomial(other.coefficients);
        } else if (other.coefficients == null) {
            return new Polynomial(coefficients);
        }

        /* Both coefficients are not null. Copy the one with the highest degree,
        respectively add the lesser/equal degree's coefficients */
        Polynomial sum;
        int minLength;
        if (other.coefficients.length < coefficients.length) {
            sum = new Polynomial(coefficients);
            for (int i = 0; i < other.coefficients.length; i++) {
                sum.coefficients[i] += other.coefficients[i];
            }
        } else {
            sum = new Polynomial(other.coefficients);
            for (int i = 0; i < coefficients.length; i++) {
                sum.coefficients[i] += coefficients[i];
            }
        }

        return sum;
    }

    // Evaluate and return the represented polynomial at given x
    public double evaluate(double x) {
        // Return the sum of all represented terms of the polynomial
        double result = 0;
        double power;
        for (int k = 0; k < coefficients.length; k++) {
            // Set power to x^k
            power = 1;
            for (int i = 0; i < k; i++) {
                power *= x;
            }
            // Add coefficients[k] * x^k to sum
            result += coefficients[k] * power;
        }
        return result;
    }

    // Return true if and only if x is a root of the polynomial
    public boolean hasRoot(double x) {
        return Double.compare(evaluate(x), 0) == 0;
        // Note: Used class's compare method to avoid floating-point error
    }
}

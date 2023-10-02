/*
Soroush Rouhani Esfahani
CSCB07H3
October 1st, 2023
Lab 2 - Polynomial.java

Polynomial class developed as per lab instructions.
See info file for alternate method descriptions.

Note: Here I define a null array as either null or with length 0
*/

import java.io.*;

public class Polynomial {

    // Fields
    double [] coefficients;
    int [] exponents;

    /* Helper function for comparing two doubles. Uses class's method to address
    with floating point issues, but also d1 == d2 to catch 0 == -0 */
    boolean doublesEqual(double d1, double d2) {
        return (Double.compare(d1, d2) == 0) || d1 == d2;
    }

    // Constructor with no input sets coefficients and exponents to null
    Polynomial() {
    }

    /* Constructor receiving array of coefficient and exponent values. Copy them
    to the field unless null. */
    Polynomial(double[] coefficientVals, int[] exponentVals) {
        // If any array is null, set both fields to [0] then return
        if (coefficientVals == null || exponentVals == null || coefficientVals.length == 0 || exponentVals.length == 0) {
            return;
        }

        // Get the length of the shortest array
        int minLength = coefficientVals.length;
        if (exponentVals.length < minLength) {
            minLength = exponentVals.length;
        }

        // Copy coefficients and exponents up to its length
        coefficients = new double[minLength];
        exponents = new int[minLength];

        for (int i = 0; i < minLength; i++) {
            coefficients[i] = coefficientVals[i];
            exponents[i] = exponentVals[i];
        }
    }

    /* Constructor reading a polynomial from a file as per assignment handout */
    Polynomial(File file) {
        try {
            // Read and store file contents
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String input = reader.readLine();
            reader.close();
            // Split input on +s & -s. Regex below provided in tutorial.
            String [] terms = input.split("(\\+)|((?=\\-))");
            
            // Prepare temporary arrays to store coefficients & exponents
            int numTerms = terms.length;
            double [] tempCoefficients = new double[numTerms];
            int [] tempExponents = new int[numTerms];

            // Get coefficients & exponents from split strings
            int coefficientEnd, nonzeroTerms = 0, xPos;
            for (int i = 0; i < numTerms; i++) {
                xPos = terms[i].indexOf("x");
                if (xPos > 0) {
                    // x is past the first character. Split again around x
                    String [] parts = terms[i].split("x");
                    if (parts[0].equals("+")) {
                        // Before "x" is just +. Set current coeff. to 1
                        tempCoefficients[nonzeroTerms] = 1;
                    } else if (parts[0].equals("-")) {
                        // Before "x" is just -. Set current coeff. to -1
                        tempCoefficients[nonzeroTerms] = -1;
                    } else {
                        // There is a number before x. Parse it and set current coeff.
                        tempCoefficients[nonzeroTerms] = Double.parseDouble(parts[0]);
                    }
                    // Set the exponent to the string after x, parsed
                    tempExponents[nonzeroTerms] = Integer.parseInt(parts[1]);
                } else if (xPos == 0) {
                    // x is the first character
                    tempCoefficients[nonzeroTerms] = 1;
                    tempExponents[nonzeroTerms] = Integer.parseInt(terms[i].substring(1));
                } else {
                    // The string has no x, and is a constant
                    tempCoefficients[nonzeroTerms] = Double.parseDouble(terms[i]);
                    tempExponents[nonzeroTerms] = 0;
                }
                // Unless the currently read coefficient was 0, go to next coeff. & exponent in the temporary arrays
                if (!doublesEqual(tempCoefficients[nonzeroTerms], 0)) {
                    nonzeroTerms++;
                }
            }

            // Copy the non-zero terms into the fields
            if (nonzeroTerms == 0) {
                return;
            }
            coefficients = new double[nonzeroTerms];
            exponents = new int[nonzeroTerms];
            for (int i = 0; i < nonzeroTerms; i++) {
                coefficients[i] = tempCoefficients[i];
                exponents[i] = tempExponents[i];
            }
        } catch (Exception error) {
            // In case of an error, set fields to the 0 polynomial
        }
    }

    /* Return a new Polynomial representing the sum of this Polynomial and
    another Polynomial */
    public Polynomial add(Polynomial other) {
        // If both Polynomials' coefficients are null, return [0]
        if ((coefficients == null || coefficients.length == 0) && (other.coefficients == null || other.coefficients.length == 0)) {
            return new Polynomial();
        /* If just one Polynomial has null coefficients, return a copy of the
        one that is not null */
        } else if (coefficients == null || coefficients.length == 0) {
            return new Polynomial(other.coefficients, other.exponents);
        } else if (other.coefficients == null || other.coefficients.length == 0) {
            return new Polynomial(coefficients, exponents);
        }
        
        // Create temporary arrays for longest polynomial possible
        int maxLength = exponents.length + other.exponents.length;
        double[] tempCoefficients = new double[maxLength];
        int[] tempExponents = new int[maxLength];

        /* Temporarily set the start of the temporary arrays to represent the
        other polynomial */
        for (int i = 0; i < other.coefficients.length; i++) {
            tempCoefficients[i] = other.coefficients[i];
            tempExponents[i] = other.exponents[i];
        }
        int nonzeroTerms = other.coefficients.length;

        // Traverse this Polynomial's terms, add them to the temp arrays
        boolean degreeExists;
        for (int i = 0; i < coefficients.length; i++) {
            // Check if there is a term in the temporary arrays with same degree
            degreeExists = false;
            for (int j = 0; j < nonzeroTerms; j++) {
                if (exponents[i] == tempExponents[j]) {
                    degreeExists = true;
                    tempCoefficients[j] += coefficients[i];
                    // If the sum is zero, "delete" the term from temp arrays
                    if (doublesEqual(tempCoefficients[j], 0)) {
                        for (int k = j + 1; k < nonzeroTerms; k++) {
                            tempCoefficients[k - 1] = tempCoefficients[k];
                            tempExponents[k - 1] = tempExponents[k];
                        }
                        nonzeroTerms--;
                    }
                }
            }
            // If there was no term with the same degree, append this term
            if (!degreeExists) {
                tempCoefficients[nonzeroTerms] = coefficients[i];
                tempExponents[nonzeroTerms] = exponents[i];
                nonzeroTerms++;
            }
        }

        // Copy the non-zero terms into arrays of correct size
        if (nonzeroTerms == 0) {
            return new Polynomial();
        }
        double[] finalCoefficients = new double[nonzeroTerms];
        int[] finalExponents = new int[nonzeroTerms];
        for (int i = 0; i < nonzeroTerms; i++) {
            finalCoefficients[i] = tempCoefficients[i];
            finalExponents[i] = tempExponents[i];
        }

        // Return a polynomial with the final arrays
        return new Polynomial(finalCoefficients, finalExponents);
    }

    /* Return a new Polynomial representing the product of this Polynomial and
    another Polynomial */
    public Polynomial multiply(Polynomial other) {
        // If any of the coefficients are null, return a 0 polynomial
        if (coefficients == null || exponents == null || coefficients.length == 0 || exponents.length == 0
            || other.coefficients == null || other.exponents == null || other.coefficients.length == 0 || other.exponents.length == 0) {
            return new Polynomial();
        }

        // Temporarily set the result to the polynomial 0
        Polynomial result = new Polynomial();

        /* For each term in this polynomial, get the polynomial obtained by
        multiplying the term with the other polynomial, and add it to the
        result */
        double [] tempCoefficients = new double[coefficients.length];
        int [] tempExponents = new int[exponents.length];
        for (int i = 0; i < other.exponents.length; i++) {
            for (int j = 0; j < coefficients.length; j++) {
                // Store terms of this polynomial * current term from other poly
                tempCoefficients[j] = other.coefficients[i] * coefficients[j];
                tempExponents[j] = other.exponents[i] + exponents[j];
            }
            // Add the product to the result
            Polynomial toAdd = new Polynomial(tempCoefficients, tempExponents);
            result = result.add(toAdd);
        }

        return result;
    }

    /* Return a double representing the value of this Polynomial at the
    given x value */
    public double evaluate(double x) {
        double result = 0.0;

        for (int i = 0; i < coefficients.length; i++) {
            // Calculate x raised to the power of current exponent
            double termValue = 1.0;
            for (int j = 0; j < exponents[i]; j++) {
                termValue *= x;
            }

            // Add the evaluation of this term to result
            result += coefficients[i] * termValue;
        }

        return result;
    }

    // Return true if and only if x is a root of the polynomial
    public boolean hasRoot(double x) {
        return doublesEqual(evaluate(x), 0);
    }

    /* Save the Polynomial to a file according to the format in the assignment
    handout */
    public void saveToFile(String filename) {
        try {
            // Prepare the output object
            FileWriter output = new FileWriter(filename);
            // If saving the 0 polynomial, simply write 0 and return
            if (coefficients == null || exponents == null || coefficients.length == 0 || exponents.length == 0) {
                output.write("0");
                output.close();
                return;
            }

            // Write each term individually
            for (int i = 0; i < coefficients.length; i++) {
                // Write the coefficient first
                if (i > 0) {
                    // Not the first term
                    if (coefficients[i] < 0) {
                        /* If the coefficient is negative, write "-" followed by
                        the absolute value of the coefficient */
                        output.write(" - ");
                        output.write(Double.toString(-1 * coefficients[i]));
                    } else {
                        // Otherwise, use "+" and the coefficient as is
                        output.write(" + ");
                        output.write(Double.toString(coefficients[i]));
                    }
                } else {
                    // First term: write the coefficient as is
                    output.write(Double.toString(coefficients[i]));
                }

                // Unless the exponent is 0, write x then the exponent
                if (exponents[i] > 0) {
                    output.write("x");
                    output.write(Integer.toString(exponents[i]));
                }
            }
            // Close the file being written to
            output.close();
        } catch (Exception error) {
        }
    }

    /* Function used in Driver.java to print the Polynomial. Mimics the logic in
    saveToFile */
    public void print() {
        if (coefficients == null || exponents == null || coefficients.length == 0 || exponents.length == 0) {
            System.out.println("0");
            return;
        }
        for (int i = 0; i < coefficients.length; i++) {
            if (i > 0) {
                if (coefficients[i] < 0) {
                    System.out.print(" - " + (-1 * coefficients[i]));
                } else {
                    System.out.print(" + " + coefficients[i]);
                }
            } else {
                System.out.print(coefficients[i]);
            }
            if (exponents[i] > 0) {
                System.out.print("x" + exponents[i]);
            }
        }
        System.out.println();
    }
}

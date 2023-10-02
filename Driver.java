/*
Soroush Rouhani Esfahani
CSCB07H3
October 1st, 2023
Lab 2 - Driver.java

Runs some tests for Polynomial.java
*/

import java.io.*;

public class Driver {
    public static void main(String[] args) {
        // Create two polynomials
        System.out.println("\nTesting polynomial creation:");
        double[] coefficients1 = {1, 1};
        int[] exponents1 = {1, 0};
        Polynomial poly1 = new Polynomial(coefficients1, exponents1);
        System.out.print("poly1 = ");
        poly1.print();
        System.out.println("Should be equivalent to x + 1");

        double[] coefficients2 = {1, -1};
        int[] exponents2 = {1, 0};
        Polynomial poly2 = new Polynomial(coefficients2, exponents2);
        System.out.print("poly2 = ");
        poly2.print();
        System.out.println("Should be equivalent to x - 1");

        Polynomial poly3 = new Polynomial();
        System.out.print("poly3 = ");
        poly3.print();
        System.out.println("Should be equivalent to 0");

        // Test addition
        System.out.println("\nTesting addition:");
        Polynomial sum1 = poly1.add(poly2);
        System.out.print("poly1 + poly2 = ");
        sum1.print();
        Polynomial negPoly1 = new Polynomial(poly1.coefficients, poly1.exponents);
        negPoly1.coefficients[0] *= -1;
        negPoly1.coefficients[1] *= -1;
        Polynomial sum2 = poly1.add(negPoly1);
        System.out.print("poly1 - poly1 = ");
        sum2.print();

        // Test multiplication
        System.out.println("\nTesting multiplication:");
        Polynomial product = poly1.multiply(poly2);
        System.out.print("poly1 * poly2 = ");
        product.print();

        // Test evaluation
        System.out.println("\nTesting evaluation:");
        double result = poly1.evaluate(2.0);
        System.out.println("Evaluation of poly1 at x = 2.0: " + result);

        // Test root
        System.out.println("\nTesting root checking:");
        System.out.println("Is -1 a root of poly1? " + poly1.hasRoot(-1));
        System.out.println("Is 1 a root of poly1? " + poly1.hasRoot(1));

        // Test file methods
        System.out.println("\nTesting file read & write:");
        File file = new File("polyTest.txt");
        Polynomial filePoly = new Polynomial(file);
        System.out.print("File polynomial: ");
        filePoly.print();
        filePoly.saveToFile("output.txt");
        System.out.println("Attempted to read polynomial from file polyTest.txt and save it to output.txt.");
    }
}

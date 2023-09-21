public class Polynomial {





    // Test for NULLs







    double [] coefficients;

    Polynomial() {
        coefficients = new double[1];
        coefficients[0] = 0;
    }

    Polynomial(double[] coefficientVals) {
        if (coefficientVals.length > 0) {
            coefficients = new double[coefficientVals.length];
            for (int i = 0; i < coefficients.length; i++) {
                coefficients[i] = coefficientVals[i];
            }
        } else {
            coefficients = new double[1];
            coefficients[0] = 0;
        }
    }

    public Polynomial add(Polynomial other) {
        if (other.coefficients.length + coefficients.length == 0) {
            return new Polynomial();
        }

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

    public double evaluate(double x) {
        double result = 0;
        double power;
        for (int i = 0; i < coefficients.length; i++) {
            power = 1;
            for (int exponent = 0; exponent < i; exponent++) {
                power *= x;
            }
            result += coefficients[i] * power;
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return Double.compare(evaluate(x), 0) == 0;
    }
}

package resolver;

import java.util.Scanner;

public class Resolver {
    public final int nEquations;
    private final float[][] Matrix;   // Will receive the system
    private final float[] solutions;  // Solutions
    private boolean solved; // If already solved, no need to solve again
    
    // Constructor
    public Resolver(int nEquations) {
        // Initialize the Matrix with the given number of equation
        // N.B.: We have a n * (n+1) Matrix (Augmented matrix)
        
        // Properties instanciations
        this.Matrix= new float[nEquations][nEquations + 1];
        this.nEquations= nEquations;
        this.solutions= new float[nEquations];
        this.solved= false;
        
        // Reading the element of Matrix
        Scanner sc= new Scanner(System.in);
        for (int i= 0; i < nEquations; i++) {
            System.out.println("Equation " + (i + 1));
            for (int j= 0; j < nEquations + 1; j++) {
                if (j != nEquations)
                    System.out.print("x" + (j+1) + " coefficient: ");
                else
                    System.out.print("Solution of the equation: ");
                // Reading
                Matrix[i][j]= sc.nextFloat();
            }
            System.out.println();
        }
    }
    
    public Resolver() {
        // If nEquations isn't given, we need to ask it
        Scanner sc= new Scanner(System.in);
        System.out.print("Please, enter the number of equations: ");
        int nEquations= sc.nextInt();
        
        // Properties instanciations
        // Properties instanciations
        this.Matrix= new float[nEquations][nEquations + 1];
        this.nEquations= nEquations;
        this.solutions= new float[nEquations];
        this.solved= false;
        
        // Reading the elements of Matrix
        for (int i= 0; i < nEquations; i++) {
            System.out.println("Equation " + (i + 1));
            for (int j= 0; j < nEquations + 1; j++) {
                if (j != nEquations)
                    System.out.print("x" + (j+1) + " coefficient: ");
                else
                    System.out.print("Solution of the equation: ");
                // Reading
                Matrix[i][j]= sc.nextFloat();
            }
            System.out.print("\n");
        }
    }
    
    public Resolver(float[][] Matrix) {
        // Constructor with a predefined Matrix
        this.nEquations= Matrix.length;
        this.solved= false;
        this.solutions= new float[nEquations];
        this.Matrix= new float[nEquations][nEquations + 1];
        // Copy
        for (int i= 0; i < nEquations; i++) {
            for (int j= 0; j < nEquations + 1; j++)
                this.Matrix[i][j]= Matrix[i][j];
        }
    }
    
    private static float[] multiply(float[][] matr, int lignOfMatrix, float number) {
        // Multiply the lign of the Matrix with a number and yield the solution
        float[] solution= new float[matr[lignOfMatrix].length];
        
        // Treatment
        for (int i= 0; i < matr[lignOfMatrix].length; i++) {
            solution[i]= matr[lignOfMatrix][i] * number;
        }
        
        return solution;
    }
    
    public void getSystem() {
        Resolver.showMatrix(Matrix);
    }
    
    private static void addTo(float[][] matr, int lignOfMatrix, float[] lign) {
        // Change a lign of the Matrix by adding the numbers in lign to its own numbers
        //System.out.print("The array is: ");
        //Resolver.showArray(lign);
        for (int i= 0; i < matr[lignOfMatrix].length; i++) {
            matr[lignOfMatrix][i]+= lign[i];
            //System.out.println("Iteration " + i + ":");
            //Resolver.showMatrix(matr);
        }
        //System.out.println("");
    }
    
    private float[][] copyMatrix() {
        // return a copy of Matrix to keep its datas safe
        float[][] copy= new float[nEquations][Matrix[0].length];
        
        // The copy
        for (int i= 0; i < copy.length; i++) {
            System.arraycopy(Matrix[i], 0, copy[i], 0, copy[i].length); // System.arraycopy(src, srcPos, dest, destPos, length)
        }
        
        return copy;
    }
    
    private static void swap(float[][] A, int lign1, int lign2) {
        // Swap lign1 and lign2
        float tmp;
        
        for (int i= 0; i < A[lign1].length; i++) {
            tmp= A[lign1][i];
            A[lign1][i]= A[lign2][i];
            A[lign2][i]= tmp;
        }
    }
    
    private static void getEquation(float[] arr) {
        // Show the equations
        for (int i= 0; i < arr.length-1; i++) {
            System.out.print("(" + arr[i] + ")" + " * x" + (i+1) + (i < arr.length - 2? " + ": " = "));
        }
        System.out.println(arr[arr.length - 1]);
    }
    
    private static void showMatrix(float[][] matr) {
        // List the elements of a matrix
        for (float[] m: matr) {
            Resolver.getEquation(m);
            System.out.print("\n");
        }
    }
    
    private float[][] gauss() {
        // Using Gauss algorithm on the n * n+1 Matrix
        float[][] solution= copyMatrix();
        int i;  // To follow lign1
        
        for (int lign1= 0; lign1 < solution.length - 1; lign1++) {
            i= lign1 + 1;
            // Adjust the lign of the equation so that the diagonal doesn't contain 0
            while (solution[lign1][lign1] == 0) {
                if (i == solution.length) {
                    // Error
                    return null;    // We have an error
                }
                swap(solution, lign1, i);
            }
            for (int lign2= lign1 + 1; lign2 < solution.length; lign2++) {
                /*
                    GAUSS Algorithm consist of tranforming the matrix into a triangle one
                    so that we can later perform retrosubstitution
                    Algo:
                        For Ei (i= 0 and i < matrix.length - 1):
                            For Ej (j= i+1 and j < matrix.length):
                                Ej <- Ej - (a[j][i]/a[i][i])Ei
                            
                */
                Resolver.addTo(solution, lign2, Resolver.multiply(solution, lign1, (-1 * solution[lign2][lign1]/solution[lign1][lign1])));
                //System.out.println("Solution: ");
                //Resolver.showMatrix(solution);
            }
        }
        
        //System.out.println("Yielding");
        return solution;
    }
    
    public float[] getSolutions() {
        // Gauss + retrosubstitution
        /*
            Algo:
            (n is the number of Equation, we start counting at 0
            ===> n-1 is the max index of lign and n is the max index for row)
            for i= n-1 downto 0:
                xi= (a[i][n+1] - (sumOf(a[i][j] * xj) for j= i+1 to n)) / a[i][i]
        */
        if (!solved) {
            // Solve if not solved yed
            float[][] triangle= this.gauss();
            //float[] solutions= new float[triangle.length];  // To keep track of the solution
            int n= triangle.length - 1; // To simplify, n is the max index for the given number of equations

            if (triangle == null) {
                System.out.println("Impossible to solve the equation using the GAUSS + Retrosubstitution method");
                return null;
            }

            for (int i= n; i >= 0; i--) {
                // Ligns from the back (retrosubstitution)
                solutions[i]= triangle[i][n+1];
                //System.out.print("x" + (i+1) + "= " + solutions[i]);
                for (int j= i+1; j <= n; j++) { // <= because n is the number of equation so also the number of variables
                    // For the other values already found
                    //System.out.print(" + (" + solutions[j] + " * " + triangle[i][j] + ")");
                    solutions[i]-= solutions[j] * triangle[i][j];
                }
                // Get the solution
                solutions[i]/=triangle[i][i];
                //System.out.println("/" + triangle[i][i] +"= " + solutions[i]);
            }
            // Set solved to true
            solved= true;
        }
        
        return solutions;
    }
    public static void main(String[] args) {
        Resolver resolver= new Resolver();
        System.out.println("The system:");
        
        // Read the system
        resolver.getSystem();
        
        System.out.print("\n\nSolutions:");
        for (int i= 0; i < resolver.nEquations; i++)
            System.out.println("x" + (i + 1) + " = " + resolver.getSolutions()[i]); // Show the solutions
    }
}

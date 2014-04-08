package com.svm4j.optimization;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

import com.svm4j.optimization.Optimizer;
import com.svm4j.optimization.TwiceDifferentiableFunction;

import org.junit.Assert;
import org.junit.Test;

public class OptimizerTest
{
    private static final double EPS = 0.0001;
    private static final int MAX_ITERATIONS = 100;
    private static final double OFFSET = 1.0; 
    
    /**
     * Test ineuality constraint
     * 
     * @author twizansky
     *
     */
    static class MyConstraint extends TwiceDifferentiableFunction
    {
        public MyConstraint(int n)
        {
            super(n);
        }
        
        public Double evaluate(RealMatrix x)
        {
            return x.getEntry(0, 0) - 1.0;
        }  
    
        public double evaluateGradient(RealMatrix x, int i)
        {
            return (i==0) ? 1.0 : 0.0;
        }
   
        public double evaluateHessian(RealMatrix x, int i, int j)
        {
            return 0;
        }
    }
    
    /**
     * Test objective function.
     * 
     * @author twizansky
     *
     */
    static class MyFunction extends TwiceDifferentiableFunction
    {
        public MyFunction(int n)
        {
            super(n);
        }
        
        public Double evaluate(RealMatrix x)
        {
            double result = 0.0;
            for (int i = 0; i<x.getRowDimension(); i++)
            {
                result += ((x.getEntry(i,0)-OFFSET)*(x.getEntry(i,0)-OFFSET));
            }
            return result;
        }

        public double evaluateGradient(RealMatrix x, int i)
        {
            return 2.0 * (x.getEntry(i, 0) - OFFSET);
        }

            
        public double evaluateHessian(RealMatrix x, int i, int j)
        {
            return (i == j) ? 2.0 : 0.0;
        }
    }
    
    /**
     * Test an optimization without any constraints
     */
    @Test
    public void testUnconstrainedOptimization()
    {   
        // Build a starting point and the expected solution.
        final double[][] START = new double[][] {{1.0}, {2.0}, {3.0}};
        final double[][] EXPECTED = new double[][] {{1.0}, {1.0}, {1.0}};
        
        // Run the optimization and check against expected result.
        int n = START.length;
        RealMatrix startMatrix = new Array2DRowRealMatrix(START);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(EXPECTED);
        Optimizer optimizer = new Optimizer(new MyFunction(n), null);
        RealMatrix solution = optimizer.run(startMatrix, EPS, MAX_ITERATIONS);
        Assert.assertEquals(solution, expectedMatrix);
    }
    
    
    /**
     * Test an optimization with linear equality constraints.
     */
    @Test
    public void testEqualityConstrainedOptimization()
    {   
        // Build a starting point and the expected solution.
        final double[][] START = new double[][] {{1.0}, {2.0}};
        final double[][] EXPECTED = new double[][] {{1.5}, {1.5}};
        final double[][] CONSTRAINT = new double[][] {{1.0, 1.0}};
      
        // Run the optimization and check against expected result.
        int n = START.length;
        RealMatrix startMatrix = new Array2DRowRealMatrix(START);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(EXPECTED);
        RealMatrix constraintMatrix = new Array2DRowRealMatrix(CONSTRAINT);   
        Optimizer optimizer = new Optimizer(new MyFunction(n), constraintMatrix);
        RealMatrix solution = optimizer.run(startMatrix, EPS, MAX_ITERATIONS);
        Assert.assertEquals(solution, expectedMatrix);
    }
    
    /**
     * Test an optimization with linear equality constraints and an inequality constraint.
     */
    @Test
    public void testInequalityConstrainedOptimization()
    {   
        // Build a starting point and the expected solution.
        final double[][] START = new double[][] {{0.5}, {2.5}};
        final double[][] EXPECTED = new double[][] {{1.0}, {2.0}};
        final double[][] CONSTRAINT = new double[][] {{1.0, 1.0}};
        final double TOLERANCE = 0.01;
      
        // Run the optimization and check against expected result.
        int n = START.length;
        RealMatrix startMatrix = new Array2DRowRealMatrix(START);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(EXPECTED);
        RealMatrix constraintMatrix = new Array2DRowRealMatrix(CONSTRAINT);
        Optimizer optimizer = new Optimizer(new MyFunction(n), constraintMatrix);
        optimizer.addInequalityConstraint(new MyConstraint(n));
        RealMatrix solution = optimizer.run(startMatrix, EPS, MAX_ITERATIONS);
        Assert.assertEquals(solution.getEntry(0, 0),  expectedMatrix.getEntry(0, 0), TOLERANCE);
        Assert.assertEquals(solution.getEntry(1, 0),  expectedMatrix.getEntry(1, 0), TOLERANCE);
    }
}

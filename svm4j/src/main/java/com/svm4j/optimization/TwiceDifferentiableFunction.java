package com.svm4j.optimization;

import org.apache.commons.math.linear.RealMatrix;

/**
 * Represents a function that has a valid gradient and Hessian at every point.
 * 
 * @author twizansky
 *
 */
public abstract class TwiceDifferentiableFunction implements IRealFunction
{
    private MatrixFunction gradient;
    private MatrixFunction hessian;
    protected int n;
    
    public TwiceDifferentiableFunction(int n)
    {
        // Build the gradient object.
        this.n = n;
        gradient = new MatrixFunction(n, 1)
        {
            public double evaluate(RealMatrix x, int i, int j)
            {
                return evaluateGradient(x, i);
            }
        };
        
        // Build the Hessian object.
        hessian = new MatrixFunction(n, n)
        {
            public double evaluate(RealMatrix x, int i, int j)
            {
                return evaluateHessian(x, i, j);
            }
        };
    }
    
    /**
     * Evaluate the gradient of a function at a point.
     * 
     * @param x
     * @param i
     *      The element in the gradient to return
     * @return
     */
    protected abstract double evaluateGradient(RealMatrix x, int i);
    
    /**
     * Evaluate the Hessian of a function at a point. 
     * @param x
     * @param i
     *      The return row.
     * @param
     *      The return column
     * @return
     */
    public abstract double evaluateHessian(RealMatrix x, int i, int j);
    
    /**
     * Get the gradient {@link MatrixFunction} object
     * @return
     */
    public MatrixFunction getGradient()
    {
        return this.gradient;
    }
    
    /**
     * Get the Hessian {@link MatrixFunction} object
     * @return
     */
    public MatrixFunction getHessian()
    {
        return this.hessian;
    }
}

package com.svm4j;


import org.apache.commons.math.linear.RealMatrix;

import com.svm4j.optimization.TwiceDifferentiableFunction;


/**
 * Represents an upper bound constraint on an element in a vector.
 * 
 * @author twizansky
 *
 */
public class UpperBoundConstraint extends TwiceDifferentiableFunction
{
    int index;
    double max;
    
    public UpperBoundConstraint(int n, int index, double max)
    {
        super(n);
        this.index = index;
        this.max = max;
    }

    @Override
    protected double evaluateGradient(RealMatrix x, int i)
    {
        if (i == index)
        {
            return 1.0;
        }
        return 0;
    }

    @Override
    public double evaluateHessian(RealMatrix x, int i, int j)
    {
        return 0;
    }

    /**
     * Constrain the element given by "index" of alpha to be 
     * smaller than the specified max.
     */
    public Double evaluate(RealMatrix alpha)
    {
        return alpha.getEntry(index, 0) - max;
    }
    
}

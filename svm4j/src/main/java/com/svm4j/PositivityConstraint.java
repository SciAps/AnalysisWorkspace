package com.svm4j;


import org.apache.commons.math.linear.RealMatrix;

import com.svm4j.optimization.TwiceDifferentiableFunction;


/**
 * Represents a constraint that a given variable is positive.
 * 
 * @author twizansky
 *
 */
public class PositivityConstraint extends TwiceDifferentiableFunction
{
    int index;
    
    public PositivityConstraint(int n, int index)
    {
        super(n);
        this.index = index;
    }

    @Override
    protected double evaluateGradient(RealMatrix x, int i)
    {
        if (i == index)
        {
            return -1.0;
        }
        return 0;
    }

    @Override
    public double evaluateHessian(RealMatrix x, int i, int j)
    {
        return 0;
    }

    /**
     * Constrain the element given by "index" of alpha to be larger 
     * than zero.
     */
    public Double evaluate(RealMatrix alpha)
    {
        return -alpha.getEntry(index, 0);
    }
    
}

package com.svm4j.optimization;

import org.apache.commons.math.linear.RealMatrix;


/**
 * Represents an objective function for a convex optimization problem
 * @author twizansky
 *
 */
public interface IRealFunction
{
    public Double evaluate(RealMatrix x);   
}

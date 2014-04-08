package com.svm4j.optimization;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

/**
 * Represents a multidimmensional function f: R^n --> R^m.
 * @author twizansky
 *
 */
public abstract class MatrixFunction
{
    protected int numRows;
    protected int numCols;
    
    public MatrixFunction(int numRows, int numCols)
    {
        this.numRows = numRows;
        this.numCols = numCols;
    }
    
    public abstract double evaluate(RealMatrix x, int i, int j);
    
    public RealMatrix evaluate(RealMatrix x)
    {
        double [][] temp = new double[numRows][numCols];
        for (int i = 0; i< numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                temp[i][j] = evaluate(x, i, j);
            }
        }
        RealMatrix result = new Array2DRowRealMatrix(temp);
        return result;
    }
    
    
}

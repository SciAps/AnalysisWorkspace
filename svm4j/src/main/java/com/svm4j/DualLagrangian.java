package com.svm4j;

import java.util.List;


import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import com.svm4j.optimization.TwiceDifferentiableFunction;


public class DualLagrangian extends TwiceDifferentiableFunction
{
    /**
     * A column vector of classification values of the points contained
     * in x.
     */
    RealMatrix y;
    
    /**
     * A matrix of representing the training set.  Each column corresponds to 
     * a point and the number of columns is the size of the training set.
     */
    RealMatrix xDotProducts;
 
    public DualLagrangian(RealMatrix x, RealMatrix y)
    {
        super(y.getRowDimension());
        this.xDotProducts = x.transpose().multiply(x);
        this.y = y;
    }
    
    public DualLagrangian (List<double[]> x, List<Double> y)
    {
        super(x.size());
        
        // Build the dot products of the training set points.
        buildDotProducts(x, x.size());
        
        // Build the matrix of classifications
        buildClassifications(y);
    }
    
    /**
     * Evaluate the Lagrangian for a given set of alpha values.
     */
    public Double evaluate(RealMatrix alpha)
    {
        int nPoints = getNumPoints();
        
        // Sum the elements of alpha (1st term in the Lagrangian)
        double term1 = 0.0;
        for (int i = 0; i < nPoints; i++)
        {
            term1 += alpha.getEntry(i, 0);
        }

        // Calculate the second term.
        double term2 = 0.0;
        for (int i = 0; i < nPoints; i++)
        {
            for (int j = 0; j < nPoints; j++)
            {
                term2 += alpha.getEntry(i, 0) * alpha.getEntry(j, 0) 
                    * y.getEntry(i, 0) * y.getEntry(j, 0) 
                    * xDotProducts.getEntry(i, j);
            }
        }
        
        return -(term1 - 0.5 * term2);
    }

    @Override
    protected double evaluateGradient(RealMatrix alpha, int i)
    {
        double sum = 0.0;
        for (int j = 0; j < getNumPoints(); j++)
        {
            sum += alpha.getEntry(j, 0) * y.getEntry(j, 0) * xDotProducts.getEntry(i, j);
        }
        return -(1.0 - y.getEntry(i, 0) * sum);
    }

    @Override
    public double evaluateHessian(RealMatrix alpha, int i, int j)
    {
        return y.getEntry(i, 0) * y.getEntry(j, 0) * xDotProducts.getEntry(i, j);
    }
    
    /**
     * Get the number of training point.
     * @return
     */
    private int getNumPoints()
    {
        return y.getRowDimension();
    }
    
    /**
     * Build the dot products of the training set points.
     * 
     * @param x
     * @param nPoints
     */
    private void buildDotProducts(List<double[]> x, int nPoints)
    {
        xDotProducts = new Array2DRowRealMatrix(nPoints, nPoints);
        for (int i = 0; i < nPoints; i++)
        {
            RealMatrix m1 = new Array2DRowRealMatrix(x.get(i));
            for (int j = 0; j < nPoints; j++)
            {
                RealMatrix m2 = new Array2DRowRealMatrix(x.get(j));
                xDotProducts.setEntry(
                        i, j, m2.transpose().multiply(m1).getEntry(0, 0));
            }
        }   
    }
    
    /**
     * Build the matrix of classifications
     * 
     * @param yList
     */
    private void buildClassifications(List<Double> yList)
    {
        y = new Array2DRowRealMatrix(yList.size(), 1);
        for (int i = 0; i < yList.size(); i++)
        {
            y.setEntry(i, 0, yList.get(i));
        }
    }
    
    // MAIN
    
//    public static void main(String[] args)
//    {
//        List<double[]> trainingSet = new ArrayList<double[]>();
//        List<Double> classifications = new ArrayList<Double>();
//        trainingSet.add(new double[]{0.0, 1.0});
//        trainingSet.add(new double[]{0.0, 0.0});
//        trainingSet.add(new double[]{0.0, -1.0});
//        trainingSet.add(new double[]{0.0, -2.0});
//        classifications.add(1.0);
//        classifications.add(1.0);
//        classifications.add(-1.0);
//        classifications.add(-1.0);
//        DualLagrangian lagrangian = new DualLagrangian(trainingSet, classifications);
//        RealMatrix x = new Array2DRowRealMatrix(new double[][] {{0.0}, {2.0}, {2.0}, {0.0}});
//        RealMatrix grad = lagrangian.getGradient().evaluate(x);
//    }
    
    
    
}

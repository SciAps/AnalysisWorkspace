package com.svm4j;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import com.svm4j.DualLagrangian;

import org.junit.*;
import org.junit.Assert;

public class DualLagrangianTest
{
    /*
    RealMatrix alpha;
    DualLagrangian lagrangian1;
    DualLagrangian lagrangian2;
    
    @BeforeClass
    public void setup()
    {
        List<double[]> trainingSet = buildTrainingSet();
        List<Double> classifications = buildClassifications();
        RealMatrix trainingSetMatrix = new Array2DRowRealMatrix(
                new double[][]{{1.0, 3.0}, {2.0, 4.0}});
        RealMatrix classificationsMatrix = new Array2DRowRealMatrix(
                new double[][]{{1.0}, {-1.0}});
        lagrangian1 = new DualLagrangian(trainingSet, classifications);
        lagrangian2 = new DualLagrangian(trainingSetMatrix, classificationsMatrix);
        alpha = new Array2DRowRealMatrix(new double[]{5.0, 6.0});
    }
    
    @DataProvider(name = "dataProvider")
    public Object[][] dataProvider()
    {
        return new Object[][] {
                {lagrangian1},
                {lagrangian2}
        };
    }
    
    @Test(dataProvider = "dataProvider")
    public void testEvaluate(DualLagrangian lagrangian)
    {
        final double EXPECTED = -171.5;
        
        Assert.assertEquals(lagrangian.evaluate(alpha), EXPECTED);
    }
    
    @Test(dataProvider = "dataProvider")
    public void testEvaluateGradient(DualLagrangian lagrangian)
    {
        final double EXPECTED0 = 42;
        final double EXPECTED1 = -94;
        
        Assert.assertEquals(lagrangian.evaluateGradient(alpha, 0), EXPECTED0);
        Assert.assertEquals(lagrangian.evaluateGradient(alpha, 1), EXPECTED1);
    }
    
    @Test(dataProvider = "dataProvider")
    public void testEvaluateHessian(DualLagrangian lagrangian)
    {
        final RealMatrix EXPECTED = new Array2DRowRealMatrix(
                new double[][]{{5.0, -11.0}, 
                               {-11.0, 25.0}}
                );
        
        Assert.assertEquals(lagrangian.evaluateHessian(alpha, 0, 0), EXPECTED.getEntry(0, 0));
        Assert.assertEquals(lagrangian.evaluateHessian(alpha, 0, 1), EXPECTED.getEntry(0, 1));
        Assert.assertEquals(lagrangian.evaluateHessian(alpha, 1, 0), EXPECTED.getEntry(1, 0));
        Assert.assertEquals(lagrangian.evaluateHessian(alpha, 1, 1), EXPECTED.getEntry(1, 1));
    }
    
    private List<double[]> buildTrainingSet()
    {
        List<double[]> result = new ArrayList<double[]>();
        result.add(new double[]{1.0, 2.0});
        result.add(new double[]{3.0, 4.0});
        return result;
    }
    
    private List<Double> buildClassifications()
    {
        List<Double> result = new ArrayList<Double>();
        result.add(1.0);
        result.add(-1.0);
        return result;
    }
    */
}

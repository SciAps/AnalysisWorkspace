package com.svm4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.svm4j.Hyperplane;
import com.svm4j.SupportVectorMachine;

import org.junit.*;

public class SupportVectorMachineTest
{
    @Test
    public void testRun()
    {
        final double EXPECTED_RATIO = 0.5;
        
        List<double[]> x = new ArrayList<double[]>();
        List<Double> y = new ArrayList<Double>();
        x.add(new double[]{1.0, 1.0});
        x.add(new double[]{0.5, 0.0});
        y.add(1.0);
        y.add(-1.0);
        
        SupportVectorMachine svm = new SupportVectorMachine();
        Hyperplane solution = svm.run(x, y);
        double[] w = solution.getW();
        Assert.assertEquals(w[0] / w[1], EXPECTED_RATIO, svm.getErr());
        System.out.println(Arrays.toString(solution.getW()));
        System.out.println(solution.getB());
    }
}

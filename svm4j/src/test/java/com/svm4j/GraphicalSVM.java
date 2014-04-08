package com.svm4j;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JApplet;

import org.apache.log4j.Logger;

import com.svm4j.Hyperplane;
import com.svm4j.SupportVectorMachine;

public class GraphicalSVM extends JApplet
{
	private static Logger LOG = Logger.getLogger(GraphicalSVM.class);
	
    private static final long serialVersionUID = 1L;
    double scaleX;
    double scaleY;
    boolean initialized = false;
    SupportVectorMachine svm;
    Hyperplane solution;
    double[] w;
    double b;
    List<double[]> trainingSet = new ArrayList<double[]>();
    List<Double> classifications = new ArrayList<Double>();
    
    @Override
    public void init()
    {   
        buildTrainingSet();
        this.setSize(500, 500);
        svm = new SupportVectorMachine();
        solution = svm.run(trainingSet, classifications);
        LOG.info("w = " + Arrays.toString(solution.getW()));
        LOG.info("b = " + solution.getB());
    }
    
    @Override
    public void paint(Graphics g) 
    {  
       g.clearRect(0, 0, this.getWidth(), this.getHeight());
       calculateScales(trainingSet);
       drawTrainingSet(g, trainingSet, classifications);
       drawSeparator(g, solution.getW(), solution.getB());
    }
    
    private void calculateScales(List<double[]> points)
    {
        double max = 0;
        for (double[] point : points)
        {
            double temp = Math.max(Math.abs(point[0]), Math.abs(point[1]));   
            if (temp > max)
            {
                max = temp;
            }
        }
        scaleX = 0.75 * this.getWidth() / 2 / max;
        scaleY = 0.75 * this.getHeight() / 2 / max;
    }
    
    private void drawTrainingSet(Graphics g, List<double[]> trainingSet, List<Double> classifications)
    {
        for (int i = 0; i < trainingSet.size(); i++)
        {
            double[] point = trainingSet.get(i);
            double cls = classifications.get(i);
            if (cls > 0)
            {
                g.setColor(Color.red);
            }
            else
            {
                g.setColor(Color.blue);
            }
            int x = displayX(point[0]) - 5;
            int y = displayY(point[1]) - 5;
            g.fillOval(x, y, 10, 10);
        }
    }
    
    private void drawSeparator(Graphics g, double[] w, double b)
    {
        double x1 = 100.0;
        double x2 = -100.0;
        double y1 = (-b - x1 * w[0]) / w[1];
        double y2 = (-b - x2 * w[0]) / w[1];
        g.setColor(Color.black);
        g.drawLine(displayX(x1), displayY(y1), displayX(x2), displayY(y2));
    }
    
    private int displayX(double x)
    {
        return (int) (this.getWidth() / 2 + x * scaleX); 
    }
    
    private int displayY(double y)
    {
        return (int) (this.getHeight() / 2 - y * scaleY); 
    }
    
    private void buildTrainingSet()
    {
        final int N_POINTS = 10;
        final float ERROR_PROB = 0.1f;
        
        trainingSet = new ArrayList<double[]>();
        classifications = new ArrayList<Double>();
        Random generator = new Random();
        w = generateRandomW();
        b = generateRandomB();
        for (int i = 0; i < N_POINTS; i++)
        {
        	boolean isError = generator.nextFloat() < ERROR_PROB;
            double x = generator.nextDouble()*2 - 1;
            double y = generator.nextDouble() + 
            	(isError ? -0.1 : 0.1 - b - x*w[0]) / w[1];
            trainingSet.add(new double[]{x, y});
            classifications.add(1.0);
        }
        for (int i = 0; i < N_POINTS; i++)
        {
        	boolean isError = generator.nextFloat() < ERROR_PROB;
            double x = generator.nextDouble()*2 - 1;
            double y = (isError ? 0.1 : -0.1 - b - x*w[0]) / w[1] - generator.nextDouble();
            trainingSet.add(new double[]{x, y});
            classifications.add(-1.0);
        }
    }
    
    private double[] generateRandomW()
    {
        Random generator = new Random();
        double w1 = generator.nextDouble();
        double w2 = generator.nextDouble();
        return new double[]{w1, w2};
    }
    
    private double generateRandomB()
    {
        Random generator = new Random();
        return generator.nextDouble();
    }
}

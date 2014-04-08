package com.svm4j;

/**
 * Represents hyperplane in a multidimensional space. The points on the
 * hyperplane satisfy: w . x + b = 0
 * 
 * @author Tommer
 * 
 */
public class Hyperplane {
	double[] w; // The vector perpendicular to the hyperplane.
	double b; // The offset parameter

	public Hyperplane() {
		super();
	}

	public Hyperplane(double[] w, double b) {
		this.w = w;
		this.b = b;
	}

	/**
	 * Classify a point based on the model. The point is assigned +1 if
	 * x * w + b > 0 and -1 if x * w + b < 0
	 * 
	 * @param x
	 * @return
	 */
	public double classify(double[] x) {
		if (x.length != w.length) {
			throw new RuntimeException("The given point is of the wrong dimension");
		}
		double result  = b;
		for (int i = 0; i < w.length; i++) {
			result += w[i] * x[i];
		}
		return result > 0 ? 1.0 : -1.0;	
	}

	public double[] getW() {
		return w;
	}

	public void setW(double[] w) {
		this.w = w;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}
}

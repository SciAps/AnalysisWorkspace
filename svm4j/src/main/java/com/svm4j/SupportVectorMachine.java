package com.svm4j;

import java.util.List;


import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import com.svm4j.optimization.Optimizer;

/**
 * Implements a linear, soft-margin, support vector machine algorithm. Based on
 * http://www.umiacs.umd.edu/~joseph/support-vector-machines4.pdf.
 * 
 * @author twizansky
 * 
 */

public class SupportVectorMachine {
	private static final double DEFAULT_ERR = 0.00001;
	private static final double DEFAULT_C = 10.0;

	// The soft-margin parameter.
	private final double c;

	// The error tolerance.
	private final double err;

	public SupportVectorMachine() {
		this(DEFAULT_C, DEFAULT_ERR);
	}

	/**
	 * @param c
	 *            The soft-margin parameter.
	 * @param err
	 *            The error tolerance for the optimization process.
	 */
	public SupportVectorMachine(double c, double err) {
		super();
		this.c = c;
		this.err = err;
	}

	/**
	 * Run the optimization.
	 * 
	 * @param x
	 *            The training set points.
	 * @param y
	 *            The training set classifications.
	 * @return The best-fit separating hyperplane in the input space.
	 */
	public Hyperplane run(List<double[]> x, List<Double> y) {
		DualLagrangian lagrangian = new DualLagrangian(x, y);
		Optimizer optimizer = new Optimizer(lagrangian,
				buildClassificationsMatrix(y).transpose());
		addPositivityConstraints(optimizer, y);
		RealMatrix solution = optimizer.run(getStart(y), err, 100);
		return buildSolution(solution, x, y);
	}

	/**
	 * Build {@link RealMatrix} object to hold the classifications.
	 * 
	 * @return
	 */
	private RealMatrix buildClassificationsMatrix(List<Double> y) {
		int numPoints = y.size();
		double[] result = new double[numPoints];
		for (int i = 0; i < numPoints; i++) {
			result[i] = y.get(i);
		}
		return new Array2DRowRealMatrix(result);
	}

	/**
	 * Add the 0 < alpha < C constraints to the optimization.
	 * 
	 * @param optimizer
	 */
	private void addPositivityConstraints(Optimizer optimizer, List<Double> y) {
		int numPoints = y.size();
		for (int i = 0; i < numPoints; i++) {
			optimizer.addInequalityConstraint(new PositivityConstraint(
					numPoints, i));
			optimizer.addInequalityConstraint(new UpperBoundConstraint(
					numPoints, i, c));
		}
	}

	/**
	 * Get a strictly feasible point to start off the optimization.
	 * 
	 * @return
	 */
	private RealMatrix getStart(List<Double> y) {
		int nPoints = y.size();
		double[] result = new double[nPoints];
		int numNegative = 0;
		for (int i = 0; i < nPoints; i++) {
			if (y.get(i) < 0) {
				numNegative++;
			}
		}
		for (int i = 0; i < nPoints; i++) {
			if (y.get(i) < 0) {
				result[i] = 0.5 * c / numNegative;
			} else {
				result[i] = 0.5 * c / (nPoints - numNegative);
			}
		}
		return new Array2DRowRealMatrix(result);
	}

	/**
	 * Build the separating hyper plane in terms using the orthogonal vector w
	 * and offset.
	 * 
	 * @param alpha
	 */
	private Hyperplane buildSolution(RealMatrix alpha, List<double[]> x,
			List<Double> y) {
		double[] w;
		double b;
		int n = x.get(0).length;

		// Build w.
		RealMatrix wMatrix = new Array2DRowRealMatrix(n, 1);
		for (int i = 0; i < x.size(); i++) {
			wMatrix = wMatrix.add(new Array2DRowRealMatrix(x.get(i))
					.scalarMultiply(alpha.getEntry(i, 0) * y.get(i)));
		}
		w = new double[n];
		for (int i = 0; i < n; i++) {
			w[i] = wMatrix.getEntry(i, 0);
		}

		// Build b.
		double maxDiff = 0.0;
		b = 0.0;
		for (int i = 0; i < x.size(); i++) {
			double diff = Math.min(alpha.getEntry(i, 0),
					c - alpha.getEntry(i, 0));

			if (diff > maxDiff) {
				b = (1.0 / y.get(i) - wMatrix.transpose()
						.multiply(new Array2DRowRealMatrix(x.get(i)))
						.getEntry(0, 0));
				maxDiff = diff;
			}
		}
		return new Hyperplane(w, b);
	}

	// GETTERS AND SETTERS

	public double getC() {
		return c;
	}

	public double getErr() {
		return err;
	}

}

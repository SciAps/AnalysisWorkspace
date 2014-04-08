package com.svm4j.optimization;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;

/**
 * Implements the newton method to minimize a multidimensional function
 * 
 * @author twizansky
 * 
 */
public class Optimizer {
	private static Logger LOG = Logger.getLogger(Optimizer.class);
	
	private TwiceDifferentiableFunction objective;
	private RealMatrix linearEqualityConstraints;
	private List<TwiceDifferentiableFunction> inequalityConstraints;
	private double eps;

	private static final double mu = 0.5;

	// Backtracking line search constants
	private static final double alpha = 0.1;
	private static final double beta = 0.5;

	/**
	 * @param objective
	 *            A function representing the objective of the optimization.
	 */
	public Optimizer(TwiceDifferentiableFunction objective) {
		this.objective = objective;
		inequalityConstraints = new ArrayList<TwiceDifferentiableFunction>();
	}

	/**
	 * 
	 * @param objective
	 *            A function representing the objective of the optimization.
	 * @param linearEqualityConstraints
	 *            A matrix representing a set of linear constraints on the
	 *            parameters.
	 */
	public Optimizer(TwiceDifferentiableFunction objective,
			RealMatrix linearEqualityConstraints) {
		this.objective = objective;
		this.linearEqualityConstraints = linearEqualityConstraints;
		inequalityConstraints = new ArrayList<TwiceDifferentiableFunction>();
	}

	/**
	 * Run the optimization. The calculation proceeds until either the required
	 * tolerance is reached or the maximum number of iterations completerd.
	 * 
	 * @param start
	 *            The optimization starting point.
	 * @param eps
	 *            The tolerance
	 * @param maxIterations
	 *            The maximum number of iterations to run.
	 * @return
	 */
	public RealMatrix run(RealMatrix start, double eps, int maxIterations) {
		// Check that the starting point is feasible.
		if (!isFeasible(start)) {
			throw new IllegalArgumentException(
					"The starting point is infeasible.");
		}

		// Iterate untill the error is bellow the tolerance.
		Double tempEps = 1.0;
		start = findRoot(start, tempEps, maxIterations);
		while (tempEps > eps) {
			tempEps *= mu;
			LOG.debug("intermediate solution: " + start);
			start = findRoot(start, tempEps, maxIterations);
		}
		return start;
	}

	/**
	 * Add an inequality constraint to the optimization. The inequelity is
	 * represented as f > 0 where f is a supplied function.
	 * 
	 * @param f The inequality function.
	 */
	public void addInequalityConstraint(TwiceDifferentiableFunction f) {
		inequalityConstraints.add(f);
	}

	/**
	 * Find the root of the equation f(x)=0.
	 * 
	 * @param function
	 *            Function to be solved for, as ObjectiveFunction
	 * @return The root of the function, as a RealMatrix
	 */
	public RealMatrix findRoot(RealMatrix start, double eps, int maxIterations) {
		RealMatrix x = start;
		this.eps = eps;
		double sqrNewtonDecrement = 0.0;
		int iteration = 1;

		// Iterate until the Newton decrement falls bellow the desired accuracy
		// or the maximum allowed iterations have been completed.
		do {
			RealMatrix newtonStep = calculateNewtonStep(x);
			double t = backtrackingLineSearch(x, newtonStep);
			sqrNewtonDecrement = calculateSquareNewtonDecrement(newtonStep, x);
			x = x.add(newtonStep.scalarMultiply(t));
			iteration++;
		} while (sqrNewtonDecrement > 2.0 * eps && iteration < maxIterations);

		if (iteration == maxIterations && sqrNewtonDecrement > 2.0 * eps) {
			LOG.error("Maximum iterations have been reached and the Newton method failed to "
							+ "converge to the desired accuracy.");
		}

		return x;
	}

	/**
	 * Perform a backtracking line search to minimize the full objective
	 * function (objective + barrier) along a given direction.
	 * 
	 * @param x
	 * @param newtonStep
	 * @return
	 */
	private double backtrackingLineSearch(RealMatrix x, RealMatrix newtonStep) {
		double t = 1.0;
		RealMatrix newX = x.add(newtonStep.scalarMultiply(t));
		double lhs = fullObjectiveFunction(newX);
		double rhs = fullObjectiveFunction(x)
				+ alpha
				* t
				* fullGradient(x).transpose().multiply(newtonStep)
						.getEntry(0, 0);
		while (lhs > rhs) {
			t *= beta;
			newX = x.add(newtonStep.scalarMultiply(t));
			lhs = fullObjectiveFunction(newX);
			rhs = fullObjectiveFunction(x)
					+ alpha
					* t
					* fullGradient(x).transpose().multiply(newtonStep)
							.getEntry(0, 0);
		}
		return t;
	}

	/**
	 * Returns the value of the objective + barrier function, at a given point.
	 * 
	 * @param x
	 * @return
	 */
	private double fullObjectiveFunction(RealMatrix x) {
		double barrier = 0;
		for (int i = 0; i < inequalityConstraints.size(); i++) {
			IRealFunction constraint = inequalityConstraints.get(i);
			Double constraintVal = constraint.evaluate(x);
			if (constraintVal >= 0) {
				return Double.MAX_VALUE;
			}
			barrier += -Math.log(-constraintVal);
		}
		return barrier * eps + objective.evaluate(x);
	}

	/**
	 * calculate the gradient of the barrier function.
	 * 
	 * @param x
	 * @return
	 */
	private RealMatrix fullGradient(RealMatrix x) {
		RealMatrix barrier = new Array2DRowRealMatrix(x.getRowDimension(), 1);
		for (int i = 0; i < inequalityConstraints.size(); i++) {
			IRealFunction constraint = inequalityConstraints.get(i);
			MatrixFunction gradient = inequalityConstraints.get(i)
					.getGradient();
			Double constraintVal = constraint.evaluate(x);
			RealMatrix gradVal = gradient.evaluate(x);
			barrier = barrier.add(gradVal.scalarMultiply(-1.0 / constraintVal));
		}
		return barrier.scalarMultiply(eps).add(
				objective.getGradient().evaluate(x));
	}

	/**
	 * Calculate the Hessian of the barrier function.
	 * 
	 * @param x
	 * @return
	 */
	private RealMatrix fullHessian(RealMatrix x) {
		RealMatrix barrier = new Array2DRowRealMatrix(x.getRowDimension(),
				x.getRowDimension());
		for (int i = 0; i < inequalityConstraints.size(); i++) {
			IRealFunction constraint = inequalityConstraints.get(i);
			MatrixFunction constGradient = inequalityConstraints.get(i)
					.getGradient();
			MatrixFunction constHessian = inequalityConstraints.get(i)
					.getHessian();
			Double constraintVal = constraint.evaluate(x);
			RealMatrix gradVal = constGradient.evaluate(x);
			RealMatrix hessVal = constHessian.evaluate(x);
			RealMatrix temp1 = gradVal.multiply(gradVal.transpose())
					.scalarMultiply(1.0 / (constraintVal * constraintVal));
			RealMatrix temp2 = hessVal.scalarMultiply(-1.0 / constraintVal);
			barrier = barrier.add(temp1.add(temp2));
		}
		return barrier.scalarMultiply(eps).add(
				objective.getHessian().evaluate(x));
	}

	/**
	 * Calculate the Newton step associated with a function
	 * 
	 * @param function
	 */
	private RealMatrix calculateNewtonStep(RealMatrix x) {
		RealMatrix hessianMatrix = fullHessian(x);
		RealMatrix gradMatrix = fullGradient(x);

		// Create matrices that encode equations to be solved:
		//
		// ( del^2(f) A^T ) ( delX ) = ( -del(f) )
		// ( A 0 ) ( w ) ( 0 )
		//
		// Where A is the matrix of linear constraints and delX is the solution
		// we seek.
		// Create a new equations matrix that incorporates the Hessian and the
		// linear equality constraints.

		int numCostraints = (linearEqualityConstraints == null) ? 0
				: linearEqualityConstraints.getRowDimension();
		int numVariables = x.getRowDimension();
		int dimension = numVariables + numCostraints;
		double[][] lhs = new double[dimension][dimension];
		double[][] rhs = new double[dimension][1];

		// First we build the right hand side, which incorporates the Hessian
		// and the linear
		// equality constrains.

		// Enter Hessian values.
		for (int i = 0; i < numVariables; i++) {
			for (int j = 0; j < numVariables; j++) {
				lhs[i][j] = hessianMatrix.getEntry(i, j);
			}
		}

		// Enter constraint values.
		for (int i = 0; i < numCostraints; i++) {
			for (int j = 0; j < numVariables; j++) {
				lhs[i + numVariables][j] = linearEqualityConstraints.getEntry(
						i, j);
				lhs[j][i + numVariables] = linearEqualityConstraints.getEntry(
						i, j);
			}
		}

		// Now build the right hand side
		for (int i = 0; i < numVariables; i++) {
			rhs[i][0] = -gradMatrix.getEntry(i, 0);
		}

		// Create the RealMatrix objects
		RealMatrix lhsMatrix = new Array2DRowRealMatrix(lhs);
		RealMatrix rhsMatrix = new Array2DRowRealMatrix(rhs);

		// Solve the equations and return the answer
		RealMatrix solution = new LUDecompositionImpl(lhsMatrix).getSolver()
				.getInverse().multiply(rhsMatrix);
		return solution.getSubMatrix(0, numVariables - 1, 0, 0);
	}

	/**
	 * Calculate the square of the Newton decrement associated with a given
	 * point and a given Newton step.
	 * 
	 * @param newtonStep
	 *            The Newton step, as RealMatrix
	 * @param x
	 *            The evaluation point, as RealMatrix
	 * @return the Newton decrement as double.
	 */
	private double calculateSquareNewtonDecrement(RealMatrix newtonStep,
			RealMatrix x) {
		RealMatrix hessianMatrix = fullHessian(x);
		return newtonStep.transpose().multiply(hessianMatrix)
				.multiply(newtonStep).getTrace();
	}

	/**
	 * Checks whether a point is feasible by checking whether all the inequality
	 * constraints are satisfied.
	 * 
	 * @param x
	 * 		The evaluation point
	 * @return
	 */
	private boolean isFeasible(RealMatrix x) {
		for (IRealFunction constraint : inequalityConstraints) {
			if (constraint.evaluate(x) > 0) {
				return false;
			}
		}
		return true;
	}
}

package com.svm4j.optimization;

public class ConstraintHandler
{
    //  /**
    //  * Covert the equality constraints to a linearly independent set.  In the process, build the 
    //  * U and V matrices that are used to convert the rest of the vectors in the optimization
    //  * to the correct basis.
    //  * @param constraints
    //  */
    // private void buildLinearlyIndpendentConstraints()
    // {
    //     // Convert the RealMatrix object of the linear constraints to a Jama
    //     // matrix.  This is because apache.commons.math does not seem to 
    //     // implement the SingularValueDecomposition interface.
    //     int nRows = linearEqualityConstraints.getRowDimension();
    //     int nColumns = linearEqualityConstraints.getColumnDimension();
    //     double[][] constraints = new double[nRows][nColumns];
    //     for (int i = 0; i < nRows; i++)
    //     {
    //         for (int j = 0; j < nColumns; j++)
    //         {
    //             constraints[i][j] = linearEqualityConstraints.getEntry(i, j);
    //         }
    //     }
    //     
    ////     double[] D = new double [Math.min(nRows, nColumns)];
    ////     double[] E = new double [Math.min(nRows, nColumns)-1];
    ////     double[] TAUQ = new double [Math.min(nRows, nColumns)];
    ////     double[] TAUP = new double [Math.min(nRows, nColumns)];
    ////     double[] work = new double [Math.max(nRows, nColumns)];
    ////     int lwork = Math.max(nRows, nColumns);
    ////     intW info = null;
    ////     DGEBRD.DGEBRD(nRows, nColumns, constraints, D, E, TAUQ, TAUP, work, lwork, info);
    //     
    //     // Perform the singular value decomposition using the FPL linear_algebra
    //     // library.
    //     double[] s = new double[Math.min(nRows+1, nColumns)];
    //     double[] e = new double[nColumns];
    //     double[][] u = new double[nRows][nColumns];
    //     double[][] v = new double[nColumns][nColumns];
    //     double[] work = new double[nRows];
    //     int job = 21;
    //     try
    //     {
    //         SVDC_j.dsvdc_j(constraints, nRows, nColumns, s, e, u, v, work, job);
    //     }
    //     catch (SVDCException err)
    //     {
    //         throw new IllegalStateException(err.getMessage());
    //     }
    //     double[][] newS = new double[nColumns][nColumns];
    //     
    //     // Scrub from S, numbers that should be zero but are 
    //     // returned as a small number.
    //     
    //     int rank = 0;
    //     for (int i = 0; i < Math.min(nRows+1, nColumns); i++)
    //     {
    //         rank += (Math.abs(s[i]) < SMALL) ? 0 : 1;
    //         newS[i][i] = s[i];
    //     } 
    //     
    //     // Switch rows
    //     for (int i = 0; i < nColumns; i++)
    //     {
    //         if (newS[i][i] == 0)
    //         {
    //             double temp = newS[i][i];
    //             
    //         }
    //     }
    //     
    //     RealMatrix S = new RealMatrixImpl(newS);
    //     
    //    
    //     
    ////     U = new RealMatrixImpl(u);
    ////     V = new RealMatrixImpl(v);
    ////     Vt = V.transpose();
    //     
    ////     
    ////     // Decomposition with Jama
    ////     Matrix consts = new Matrix(constraints);
    ////     SingularValueDecomposition svd = new SingularValueDecomposition(consts);
    ////     System.out.println("s from Jama: ");
    ////     svd.getS().print(5, 3);
    ////     System.out.println("s from FPL: ");
    ////     printMatrix(S);
    ////     
    //     linearEqualityConstraints = 
    //         S.getSubMatrix(0, rank-1, 0, nColumns-1);
    //
    //     System.out.println("reconstructed constraints: ");
    ////     printMatrix(U.multiply(S).multiply(V.transpose()));
    //     
    // }
}

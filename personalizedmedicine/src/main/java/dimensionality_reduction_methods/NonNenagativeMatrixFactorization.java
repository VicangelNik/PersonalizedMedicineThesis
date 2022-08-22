package dimensionality_reduction_methods;

/*
 * Class for Non Nenagative Matrix Factorization implementation.
 * Non Negative Matrix Factorization Class which consists of different implementations
 * https://github.com/du-lab/nonnegative_matrix_factorization
 * https://du-lab.github.io/nonnegative_matrix_factorization/
 */
import org.dulab.javanmf.algorithms.MatrixFactorization;
import org.dulab.javanmf.algorithms.MatrixRegression;
import org.dulab.javanmf.algorithms.SingularValueDecomposition;
import org.dulab.javanmf.updaterules.FGDKLUpdateRule;
import org.dulab.javanmf.updaterules.FGDMUpdateRule;
import org.dulab.javanmf.updaterules.MKLUpdateRule;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.UpdateRule;
import org.jblas.DoubleMatrix;

/**
 * The Class NonNenagativeMatrixFactorization.
 */
public class NonNenagativeMatrixFactorization {

	/** The matrix X. */
	private DoubleMatrix matrixX;

	/** The matrix W. */
	private DoubleMatrix matrixW;

	/** The matrix H. */
	private DoubleMatrix matrixH;

	/**
	 * Instantiates a new non nenagative matrix factorization.
	 *
	 * @param matrixW the matrix W
	 * @param matrixH the matrix H
	 * @param matrixX the matrix X
	 */
	public NonNenagativeMatrixFactorization(DoubleMatrix matrixW, DoubleMatrix matrixH, DoubleMatrix matrixX) {
		this.matrixW = matrixW;
		this.matrixH = matrixH;
		this.matrixX = matrixX;
		// Initialize matrices W and H by the NNDSVD-method
		new SingularValueDecomposition(matrixX).decompose(matrixW, matrixH);
	}

	/**
	 * Non negative matrix factorization implementation.
	 *
	 * @param rule the rule
	 * @return the double matrix[]
	 */
	public DoubleMatrix[] nonNegativeMatrixFactorizationImp(UpdateRule rule) {
		UpdateRule updateRuleW;
		UpdateRule updateRuleH;
		// Choose update rules for matrices W and H:
		if (rule instanceof MUpdateRule) {
			// Multiplicative update rule for the euclidean distance with l1-regularization
			updateRuleW = new MUpdateRule(1.0, 0.0);
			// Multiplicative update rule for the euclidean distance with l2-regularization
			updateRuleH = new MUpdateRule(0.0, 1.0);
		} else if (rule instanceof FGDMUpdateRule) {
			// Performs fast-gradient-descent update for the euclidean distance with
			// l1-regularization
			updateRuleW = new FGDMUpdateRule(1.0, 0.0);
			// Performs fast-gradient-descent update for the euclidean distance with
			// l2-regularization
			updateRuleH = new FGDMUpdateRule(0.0, 1.0);
		} else if (rule instanceof FGDKLUpdateRule) {
			// Performs fast-gradient-descent update for the generalized Kullback-Leibler
			// divergence with regularization
			updateRuleW = new FGDKLUpdateRule(1.0, 0.0);
			// Performs fast-gradient-descent update for the generalized Kullback-Leibler
			// divergence with regularization
			updateRuleH = new FGDKLUpdateRule(0.0, 1.0);
		} else {
			// Performs multiplicative update for the generalized Kullback-Leibler
			// divergence with regularization
			updateRuleW = new MKLUpdateRule(1.0, 0.0);
			// Performs multiplicative update for the generalized Kullback-Leibler
			// divergence with regularization
			updateRuleH = new MKLUpdateRule(0.0, 1.0);
		}
		// Perform factorization
		new MatrixFactorization(updateRuleW, updateRuleH, 1e-4, 10000).execute(matrixX, matrixW, matrixH);
		// The return object array
		DoubleMatrix[] matrices = new DoubleMatrix[3];
		matrices[0] = matrixX;
		matrices[1] = matrixW;
		matrices[2] = matrixH;
		return matrices;
	}

	/**
	 * Performs non-negative matrix regression.
	 *
	 * @param limit         the limit
	 * @param tolerance     the tolerance
	 * @param maxIterations the max iterations
	 * @param updateRule    the update rule H
	 * @param verbose       the verbose
	 * @return the double matrix
	 */
	public DoubleMatrix regression(DoubleMatrix limit, double tolerance, int maxIterations, UpdateRule updateRule,
			boolean verbose) {
		// Creates an instance of MatrixRegression
		MatrixRegression regression = new MatrixRegression(updateRule, tolerance, maxIterations);
		// Performs non-negative matrix regression
		if (limit != null) {
			// matrixX, matrixW, matrixH, limit, verbose
			return regression.solve(matrixX, matrixW, matrixH, limit, verbose);
		}
		// matrixX, matrixW, matrixH, verbose
		return regression.solve(matrixX, matrixW, matrixH, verbose);
	}

	/**
	 * Gets the matrix X.
	 *
	 * @return the matrix X
	 */
	public DoubleMatrix getMatrixX() {
		return matrixX;
	}

	/**
	 * Sets the matrix X.
	 *
	 * @param matrixX the new matrix X
	 */
	public void setMatrixX(DoubleMatrix matrixX) {
		this.matrixX = matrixX;
	}

	/**
	 * Gets the matrix W.
	 *
	 * @return the matrix W
	 */
	public DoubleMatrix getMatrixW() {
		return matrixW;
	}

	/**
	 * Sets the matrix W.
	 *
	 * @param matrixW the new matrix W
	 */
	public void setMatrixW(DoubleMatrix matrixW) {
		this.matrixW = matrixW;
	}

	/**
	 * Gets the matrix H.
	 *
	 * @return the matrix H
	 */
	public DoubleMatrix getMatrixH() {
		return matrixH;
	}

	/**
	 * Sets the matrix H.
	 *
	 * @param matrixH the new matrix H
	 */
	public void setMatrixH(DoubleMatrix matrixH) {
		this.matrixH = matrixH;
	}

}

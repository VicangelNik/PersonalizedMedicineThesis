package personalizedmedicine.dimensionality_reduction_methods;

/*
 * Class for Non Nenagative Matrix Factorization implementation.
 * Non Negative Matrix Factorization Class which consists of different implementations
 * https://github.com/du-lab/nonnegative_matrix_factorization
 * https://du-lab.github.io/nonnegative_matrix_factorization/
 */

import lombok.Getter;
import lombok.Setter;
import org.dulab.javanmf.algorithms.MatrixFactorization;
import org.dulab.javanmf.algorithms.MatrixRegression;
import org.dulab.javanmf.algorithms.SingularValueDecomposition;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.UpdateRule;
import org.ejml.data.DMatrixRMaj;

@Getter
@Setter
public class NonNegativeMatrixFactorization {

    private DMatrixRMaj matrixX;
    private DMatrixRMaj matrixW;
    private DMatrixRMaj matrixH;

    public NonNegativeMatrixFactorization(DMatrixRMaj matrixW, DMatrixRMaj matrixH, DMatrixRMaj matrixX) {
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
    public DMatrixRMaj[] nonNegativeMatrixFactorizationImp(UpdateRule rule) {
        UpdateRule updateRuleW = null;
        UpdateRule updateRuleH = null;
        // Choose update rules for matrices W and H:
        if (rule instanceof MUpdateRule) {
            // Multiplicative update rule for the euclidean distance with l1-regularization
            updateRuleW = new MUpdateRule(1.0, 0.0);
            // Multiplicative update rule for the euclidean distance with l2-regularization
            updateRuleH = new MUpdateRule(0.0, 1.0);
        }
        // Perform factorization
        new MatrixFactorization(updateRuleW, updateRuleH, 1e-4, 10000).execute(matrixX, matrixW, matrixH);
        // The return object array
        DMatrixRMaj[] matrices = new DMatrixRMaj[3];
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
    public DMatrixRMaj regression(DMatrixRMaj limit, double tolerance, int maxIterations, UpdateRule updateRule,
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
}

package personalizedmedicine;

import lombok.val;
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.KLDivergence;
import org.dulab.javanmf.measures.Measure;
import org.dulab.javanmf.updaterules.*;
import org.jblas.DoubleMatrix;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.NonNenagativeMatrixFactorization;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.Utils;

public class TestNonNegativeMatrixFactorizationTestCase {
    private static double doubleArray[][];
    private static DoubleMatrix matrixX;
    private static DoubleMatrix matrixW;
    private static DoubleMatrix matrixH;
    private static int num_components = 2;

    @BeforeEach
    public void initializeMatrix() {
        // initialize array
        doubleArray = new double[400][4];
        // set numerical parameters to the array
        Utils.setParametersToArray(doubleArray);
        // initialize matrixX
        matrixX = new DoubleMatrix(doubleArray);
        // write data with the class attribute into file
        Utils.writeMatrixToFile(Utils.FILE_DATA_PATH, matrixX);
    }

    @AfterAll
    public static void clearResources() {
        // remove all the files of the directory but not the files in subdirectories
        Utils.removeFiles(Constants.SRC_TEST_RESOURCES_PATH);
    }

    @Test
    void testEuclideanDistanceWithFGDMUpdateRuleTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new FGDMUpdateRule(1.0, 1.0);
        val matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // Calculates the distance between matrices X and WH using the euclidean
        // distance || X - WH || ^2
        val measure = new EuclideanDistance();
        final double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(new Object() {}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices,
                           measureResult);
    }

    @Test
    void testEuclideanDistanceWithMUpdateRuleTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new MUpdateRule(1.0, 1.0);
        Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // Calculates the distance between matrices X and WH using the euclidean
        // distance || X - WH || ^2
        val measure = new EuclideanDistance();
        final double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(new Object() {}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices,
                           measureResult);
    }

    @Test
    void testKullbackLeiblerWithFGDKUpdateRuleTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        UpdateRule rule = new FGDKLUpdateRule(1.0, 1.0);
        Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // Calculates the distance between matrices X and WH using the generalized
        // Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
        Measure measure = new KLDivergence();
        double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(new Object() {}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices,
                           measureResult);
    }

    @Test
    void testKullbackLeiblerWithMKLUpdateRuleTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new MKLUpdateRule(1.0, 1.0);
        val matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // Calculates the distance between matrices X and WH using the generalized
        // Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
        val measure = new KLDivergence();
        final double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(new Object() {}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices,
                           measureResult);
    }
}

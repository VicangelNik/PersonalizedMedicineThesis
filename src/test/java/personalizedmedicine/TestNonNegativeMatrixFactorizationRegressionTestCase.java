package personalizedmedicine;

import lombok.val;
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.KLDivergence;
import org.dulab.javanmf.updaterules.FGDKLUpdateRule;
import org.dulab.javanmf.updaterules.FGDMUpdateRule;
import org.dulab.javanmf.updaterules.MKLUpdateRule;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.jblas.DoubleMatrix;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import personalizedmedicine.dimensionality_reduction_methods.NonNenagativeMatrixFactorization;
import personalizedmedicine.utilpackage.Utils;

import static personalizedmedicine.helpful_classes.Constants.SRC_TEST_RESOURCES_PATH;
import static personalizedmedicine.utilpackage.Utils.FILE_DATA_PATH;
import static personalizedmedicine.utilpackage.Utils.TXT_SUFFIX;

class TestNonNegativeMatrixFactorizationRegressionTestCase {
    private static DoubleMatrix matrixX;
    private static DoubleMatrix matrixW;
    private static DoubleMatrix matrixH;
    private static final int num_components = 2;
    private static final double tolerance = 1e-6;
    private static final int maxIterations = 10000;
    private TestInfo testInfo;

    @BeforeEach
    public void initializeMatrix(final TestInfo testInfo) {
        this.testInfo = testInfo;
        // initialize array
        final double[][] doubleArray = new double[400][4];
        // set numerical parameters to the array
        Utils.setParametersToArray(doubleArray);
        // initialize matrixX
        matrixX = new DoubleMatrix(doubleArray);
        // write data with the class attribute into file
        Utils.writeMatrixToFile(FILE_DATA_PATH, matrixX);
    }

    @AfterAll
    public static void clearResources() {
        // remove all the files of the directory but not the files in subdirectories
        Utils.removeFiles(SRC_TEST_RESOURCES_PATH);
    }

    @Test
    void testEuclideanDistanceWithFGDMUpdateRuleWithRegressionTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new FGDMUpdateRule(0.0, 1.0);
        final DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // copies the results to a new matrix with more allocated space.
        val matrices = new DoubleMatrix[4];
        System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
        // Calculates the distance between matrices X and WH using the euclidean
        // distance || X - WH || ^2
        val measure = new EuclideanDistance();
        double measureResult = measure.get(matrixX, matrixW, matrixH);
        // Do regression
        final DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
        // add to matrices the regression matrix H
        matrices[3] = matrixRegressionH;
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
        // Calculate new measure after regression
        measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
        // write new measure after regression
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, null, measureResult);
    }

    @Test
    void testEuclideanDistanceWithMUpdateRuleTestCaseWithRegressionTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new MUpdateRule(0.0, 1.0);
        final DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // copies the results to a new matrix with more allocated space.
        val matrices = new DoubleMatrix[4];
        System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
        // Calculates the distance between matrices X and WH using the euclidean
        // distance || X - WH || ^2
        val measure = new EuclideanDistance();
        double measureResult = measure.get(matrixX, matrixW, matrixH);
        // Do regression
        final DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
        // add to matrices the regression matrix H
        matrices[3] = matrixRegressionH;
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
        // Calculate new measure after regression
        measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
        // write new measure after regression
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, null, measureResult);
    }

    @Test
    void testKullbackLeiblerWithFGDKUpdateRuleWithRegressionTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new FGDKLUpdateRule(0.0, 1.0);
        val returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // copies the results to a new matrix with more allocated space.
        val matrices = new DoubleMatrix[4];
        System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
        // Calculates the distance between matrices X and WH using the generalized
        // Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
        val measure = new KLDivergence();
        double measureResult = measure.get(matrixX, matrixW, matrixH);
        // Do regression
        DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
        // add to matrices the regression matrix H
        matrices[3] = matrixRegressionH;
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
        // Calculate new measure after regression
        measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
        // write new measure after regression
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, null, measureResult);
    }

    @Test
    void testKullbackLeiblerWithMKLUpdateRuleWithRegressionTestCase() {
        // Create matrices W and H
        matrixW = new DoubleMatrix(matrixX.rows, num_components);
        matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val rule = new MKLUpdateRule(0.0, 1.0);
        DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
        // copies the results to a new matrix with more allocated space.
        val matrices = new DoubleMatrix[4];
        System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
        // Calculates the distance between matrices X and WH using the generalized
        // Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
        val measure = new KLDivergence();
        double measureResult = measure.get(matrixX, matrixW, matrixH);
        // Do regression
        DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
        // add to matrices the regression matrix H
        matrices[3] = matrixRegressionH;
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
        // Calculate new measure after regression
        measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
        // write new measure after regression
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, null, measureResult);
    }
}

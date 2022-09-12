package personalizedmedicine;

import lombok.val;
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.KLDivergence;
import org.dulab.javanmf.measures.Measure;
import org.dulab.javanmf.updaterules.FGDMUpdateRule;
import org.dulab.javanmf.updaterules.MKLUpdateRule;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.RegularizationUpdateRule;
import org.jblas.DoubleMatrix;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import personalizedmedicine.dimensionality_reduction_methods.NonNenagativeMatrixFactorization;
import personalizedmedicine.utilpackage.Utils;

import java.util.stream.Stream;

import static personalizedmedicine.helpful_classes.Constants.SRC_TEST_RESOURCES_PATH;
import static personalizedmedicine.utilpackage.Utils.FILE_DATA_PATH;
import static personalizedmedicine.utilpackage.Utils.TXT_SUFFIX;

public class TestNonNegativeMatrixFactorizationTestCase {
    private static DoubleMatrix matrixX;
    private static final int num_components = 2;
    private TestInfo testInfo;

    @BeforeEach
    public void initializeMatrix() {
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

    @ParameterizedTest
    @MethodSource
    void testEuclideanDistanceWithFGDMUpdateRuleTestCase(final RegularizationUpdateRule rule, final Measure measure) {
        // Create matrices W and H
        val matrixW = new DoubleMatrix(matrixX.rows, num_components);
        val matrixH = new DoubleMatrix(num_components, matrixX.columns);
        // do factorization
        val nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
        val matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);

        final double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
    }

    private static Stream<Arguments> testEuclideanDistanceWithFGDMUpdateRuleTestCase() {
        val fgmdUpdateRule = new FGDMUpdateRule(1.0, 1.0);
        val mUpdateRule = new MUpdateRule(1.0, 1.0);
        val mklUpdateRule = new MKLUpdateRule(1.0, 1.0);
        // Calculates the distance between matrices X and WH using the euclidean distance || X - WH || ^2
        val euclideanDistanceMeasure = new EuclideanDistance();
        // Calculates the distance between matrices X and WH using the generalized Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
        val klDivergenceMeasure = new KLDivergence();

        return Stream.of(Arguments.of(fgmdUpdateRule, euclideanDistanceMeasure),
                         Arguments.of(mUpdateRule, euclideanDistanceMeasure),
                         Arguments.of(fgmdUpdateRule, klDivergenceMeasure),
                         Arguments.of(mklUpdateRule, klDivergenceMeasure));
    }
}

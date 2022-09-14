package personalizedmedicine;

import lombok.val;
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.Measure;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.RegularizationUpdateRule;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import personalizedmedicine.dimensionality_reduction_methods.NonNegativeMatrixFactorization;
import personalizedmedicine.utilpackage.Utils;

import java.util.stream.Stream;

import static personalizedmedicine.helpful_classes.Constants.SRC_TEST_RESOURCES_PATH;
import static personalizedmedicine.utilpackage.Utils.FILE_DATA_PATH;
import static personalizedmedicine.utilpackage.Utils.TXT_SUFFIX;

public class TestNonNegativeMatrixFactorizationTestCase {
    private static DMatrixRMaj matrixX = new DMatrixRMaj();
    private static final int num_components = 2;
    private TestInfo testInfo;

    @BeforeEach
    public void initializeMatrix(final TestInfo testInfo) {
        this.testInfo = testInfo;
        // initialize array
        final double[][] doubleArray = new double[400][4];
        // set numerical parameters to the array
        Utils.setParametersToArray(doubleArray);
        // initialize matrixX
        matrixX = new DMatrixRMaj(doubleArray);
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
    void testEuclideanDistanceUpdateRuleTestCase(final RegularizationUpdateRule rule, final Measure measure) {
        // Create matrices W and H
        val matrixW = new DMatrixRMaj(matrixX.numRows, num_components);
        val matrixH = new DMatrixRMaj(num_components, matrixX.numCols);
        // do factorization
        val nnmf = new NonNegativeMatrixFactorization(matrixW, matrixH, matrixX);
        val matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);

        final double measureResult = measure.get(matrixX, matrixW, matrixH);
        // write results to files
        Utils.writeResults(testInfo.getDisplayName(), TXT_SUFFIX, matrices, measureResult);
    }

    private static Stream<Arguments> testEuclideanDistanceUpdateRuleTestCase() {
        val mUpdateRule = new MUpdateRule(1.0, 1.0);
        // Calculates the distance between matrices X and WH using the euclidean distance || X - WH || ^2
        val euclideanDistanceMeasure = new EuclideanDistance();
        return Stream.of(Arguments.of(mUpdateRule, euclideanDistanceMeasure));
    }
}

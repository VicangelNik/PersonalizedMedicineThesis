package personalizedmedicine;
/*
 *
 */
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.KLDivergence;
import org.dulab.javanmf.measures.Measure;
import org.dulab.javanmf.updaterules.FGDKLUpdateRule;
import org.dulab.javanmf.updaterules.FGDMUpdateRule;
import org.dulab.javanmf.updaterules.MKLUpdateRule;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.UpdateRule;
import org.jblas.DoubleMatrix;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import personalizedmedicine.dimensionality_reduction_methods.NonNenagativeMatrixFactorization;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TestNonNegativeMatrixFactorizationRegressionTestCase.
 */
public class TestNonNegativeMatrixFactorizationRegressionTestCase {

	/** The double array. */
	private static double doubleArray[][];

	/** The matrix X. */
	private static DoubleMatrix matrixX;

	/** The matrix W. */
	private static DoubleMatrix matrixW;

	/** The matrix H. */
	private static DoubleMatrix matrixH;

	/** The num components. */
	private static int num_components = 2;

	/** The tolerance. */
	private static double tolerance = 1e-6;

	/** The max iterations. */
	private static int maxIterations = 10000;

	/**
	 * Clear resources.
	 */
	@AfterClass
	public static void clearResources() {
		// remove all the files of the directory but not the files in subdirectories
		Utils.removeFiles(Constants.SRC_TEST_RESOURCES_PATH);
	}

	/**
	 * Generate random numbers to fill the matrixX.
	 */
	@Before
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

	/**
	 * Test euclidean distance with FGDM update rule with regression test case.
	 */
	@Test
	public void testEuclideanDistanceWithFGDMUpdateRuleWithRegressionTestCase() {
		System.out.println("EuclideanDistanceFGDMUpdateRuleWithRegression");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		// do factorization
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new FGDMUpdateRule(0.0, 1.0);
		DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// copies the results to a new matrix with more allocated space.
		DoubleMatrix[] matrices = new DoubleMatrix[4];
		System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
		// Calculates the distance between matrices X and WH using the euclidean
		// distance || X - WH || ^2
		Measure measure = new EuclideanDistance();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// Do regression
		DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
		// add to matrices the regression matrix H
		matrices[3] = matrixRegressionH;
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices, measureResult);
		// Calculate new measure after regression
		measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
		// write new measure after regression
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, null, measureResult);
	}

	/**
	 * Test euclidean distance with M update rule test case with regression test
	 * case.
	 */
	@Test
	public void testEuclideanDistanceWithMUpdateRuleTestCaseWithRegressionTestCase() {
		System.out.println("EuclideanDistanceWithMUpdateRuleWithRegressionTestCase");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		// do factorization
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new MUpdateRule(0.0, 1.0);
		DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// copies the results to a new matrix with more allocated space.
		DoubleMatrix[] matrices = new DoubleMatrix[4];
		System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
		// Calculates the distance between matrices X and WH using the euclidean
		// distance || X - WH || ^2
		Measure measure = new EuclideanDistance();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// Do regression
		DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
		// add to matrices the regression matrix H
		matrices[3] = matrixRegressionH;
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices, measureResult);
		// Calculate new measure after regression
		measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
		// write new measure after regression
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, null, measureResult);
	}

	/**
	 * Test kullback leibler with FGDK update rule with regression test case.
	 */
	@Test
	public void testKullbackLeiblerWithFGDKUpdateRuleWithRegressionTestCase() {
		System.out.println("KullbackLeiblerFGDKUpdateRuleWithRegression");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		// do factorization
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new FGDKLUpdateRule(0.0, 1.0);
		DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// copies the results to a new matrix with more allocated space.
		DoubleMatrix[] matrices = new DoubleMatrix[4];
		System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
		// Calculates the distance between matrices X and WH using the generalized
		// Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
		Measure measure = new KLDivergence();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// Do regression
		DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
		// add to matrices the regression matrix H
		matrices[3] = matrixRegressionH;
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices, measureResult);
		// Calculate new measure after regression
		measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
		// write new measure after regression
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, null, measureResult);
	}

	/**
	 * Test kullback leibler with MKL update rule with regression test case.
	 */
	@Test
	public void testKullbackLeiblerWithMKLUpdateRuleWithRegressionTestCase() {
		System.out.println("KullbackLeiblerMKLUpdateRuleWithRegression");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		// do factorization
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new MKLUpdateRule(0.0, 1.0);
		DoubleMatrix[] returnedMatrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// copies the results to a new matrix with more allocated space.
		DoubleMatrix[] matrices = new DoubleMatrix[4];
		System.arraycopy(returnedMatrices, 0, matrices, 0, 3);
		// Calculates the distance between matrices X and WH using the generalized
		// Kullback-Leibler divergence Σ ( X log ( X / WH ) - X + WH )
		Measure measure = new KLDivergence();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// Do regression
		DoubleMatrix matrixRegressionH = nnmf.regression(null, tolerance, maxIterations, rule, true);
		// add to matrices the regression matrix H
		matrices[3] = matrixRegressionH;
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, matrices, measureResult);
		// Calculate new measure after regression
		measureResult = measure.get(matrixX, matrixW, matrixRegressionH);
		// write new measure after regression
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.TXT_SUFFIX, null, measureResult);
	}
}


/*
 *
 */
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dulab.javanmf.measures.EuclideanDistance;
import org.dulab.javanmf.measures.KLDivergence;
import org.dulab.javanmf.measures.Measure;
import org.dulab.javanmf.updaterules.FGDKLUpdateRule;
import org.dulab.javanmf.updaterules.FGDMUpdateRule;
import org.dulab.javanmf.updaterules.MKLUpdateRule;
import org.dulab.javanmf.updaterules.MUpdateRule;
import org.dulab.javanmf.updaterules.UpdateRule;
import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

import dimensionality_reduction_methods.NonNenagativeMatrixFactorization;
import utilpackage.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TestNonNegativeMatrixFactorizationTestCase.
 */
public class TestNonNegativeMatrixFactorizationTestCase {

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

	/**
	 * Clear resources.
	 */
	// @AfterClass
	public static void clearResources() {
		// remove all the contents of the directory
		File file = new File(Utils.RELATIVE_PATH);
		try {
			FileUtils.cleanDirectory(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize matrix.
	 */
	// Generate random numbers to fill the matrixX
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
	 * Test euclidean distance with fast-gradient-descent update rule test case.
	 */
	@Test
	public void testEuclideanDistanceWithFGDMUpdateRuleTestCase() {
		System.out.println("EuclideanDistanceFGDMUpdateRule");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		// do factorization
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new FGDMUpdateRule(1.0, 1.0);
		Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// Calculates the distance between matrices X and WH using the euclidean
		// distance || X − WH || ^2
		Measure measure = new EuclideanDistance();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.FILE_SUFFIX, matrices, measureResult);
	}

	/**
	 * Test euclidean distance with multiplicative update rule test case.
	 */
	@Test
	public void testEuclideanDistanceWithMUpdateRuleTestCase() {
		System.out.println("EuclideanDistanceWithMUpdateRule");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new MUpdateRule(1.0, 1.0);
		Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// Calculates the distance between matrices X and WH using the euclidean
		// distance || X − WH || ^2
		Measure measure = new EuclideanDistance();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.FILE_SUFFIX, matrices, measureResult);
	}

	/**
	 * Test kullback leibler with fast-gradient-descent update rule test case.
	 */
	@Test
	public void testKullbackLeiblerWithFGDKUpdateRuleTestCase() {
		System.out.println("KullbackLeiblerFGDKUpdateRule");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new FGDKLUpdateRule(1.0, 1.0);
		Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// Calculates the distance between matrices X and WH using the generalized
		// Kullback-Leibler divergence ∑ ( X log ( X / WH ) − X + WH )
		Measure measure = new KLDivergence();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.FILE_SUFFIX, matrices, measureResult);
	}

	/**
	 * Test kullback leibler with multiplicative update rule test case.
	 */
	@Test
	public void testKullbackLeiblerWithMKLUpdateRuleTestCase() {
		System.out.println("KullbackLeiblerMKLUpdateRule");
		// Create matrices W and H
		matrixW = new DoubleMatrix(matrixX.rows, num_components);
		matrixH = new DoubleMatrix(num_components, matrixX.columns);
		NonNenagativeMatrixFactorization nnmf = new NonNenagativeMatrixFactorization(matrixW, matrixH, matrixX);
		UpdateRule rule = new MKLUpdateRule(1.0, 1.0);
		Object[] matrices = nnmf.nonNegativeMatrixFactorizationImp(rule);
		// Calculates the distance between matrices X and WH using the generalized
		// Kullback-Leibler divergence ∑ ( X log ( X / WH ) − X + WH )
		Measure measure = new KLDivergence();
		double measureResult = measure.get(matrixX, matrixW, matrixH);
		// write results to files
		Utils.writeResults(new Object() {
		}.getClass().getEnclosingMethod().getName(), Utils.FILE_SUFFIX, matrices, measureResult);
	}

}

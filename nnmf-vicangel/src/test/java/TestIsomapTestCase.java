
/*
 *
 */
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jblas.DoubleMatrix;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import dimensionality_reduction_methods.IsoMapVic;
import smile.graph.Graph;
import utilpackage.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TestIsomapTestCase.
 */
public class TestIsomapTestCase {

	/** The double array. */
	private static double data[][];

	/** The dimension. */
	private final int dimension = 2;

	/** The k nearest. */
	private final int kNearest = 2;

	/**
	 * Clear resources.
	 */
	// @AfterClass
	public static void clearResources() {
		// remove all the contents of the directory
		File file = new File(Utils.SRC_TEST_RESOURCES_PATH);
		try {
			FileUtils.cleanDirectory(file); // should be removed
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize matrix.
	 */
	//@Before
	public void initializeMatrix() {
		// initialize array
		data = new double[400][4];
		// set numerical parameters to the array
		Utils.setParametersToArray(data);
		// initialize matrixX
		DoubleMatrix matrixX = new DoubleMatrix(data);
		// write data with the class attribute into file
		Utils.writeMatrixToFile(Utils.FILE_DATA_PATH, matrixX);
	}

	/**
	 * Test isomap tet case.
	 *
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void TestIsomapTetCase() throws IOException {
		IsoMapVic myIsomap = new IsoMapVic(data, dimension, kNearest, false);
		double[][] coordinates = myIsomap.getCoordinates();
		int[] index = utiltestpackage.Utils.readIndexForIsomapTest("src\\test\\resources\\isomapTests\\index.txt");
		Graph graph = myIsomap.getNearestNeighborGraph();
		Assert.assertArrayEquals(index, myIsomap.getIndex());
		Assert.assertArrayEquals("The dataset should be the same ", data, myIsomap.getDataset());
		Assert.assertEquals("The dimension of the manifold should be equal to 2", 2, myIsomap.getD());
		Assert.assertEquals("The k-nearest neighbour should be equal to 2", 2, myIsomap.getK());
	}

	/**
	 * Test C isomap tet case.
	 */
	@Ignore
	@Test
	public void TestCIsomapTetCase() {
		IsoMapVic myIsomap = new IsoMapVic(data, dimension, kNearest, true);
		double[][] coordinates = myIsomap.getCoordinates();
		myIsomap.getIndex();
		Assert.assertArrayEquals("The dataset should be the same ", data, myIsomap.getDataset());
		Assert.assertEquals("The dimension of the manifold should be equal to 2", 2, myIsomap.getD());
		Assert.assertEquals("The k-nearest neighbour should be equal to 2", 2, myIsomap.getK());
	}

}

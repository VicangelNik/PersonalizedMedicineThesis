
/*
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.Constants;
import helpful_classes.NaiveBayesImplementation;
import smile.manifold.IsoMap;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestIsomapTestCase.
 */
public class TestIsomapTestCase {

	@Test
	public void TestIsomapTetCase() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, "SampleStatus");
		int dimensions = 10;
		int kNearest = 5;
		boolean cIsomap = true;
		double data[][] = TransformToFromWeka.transformWekaToIsomap(originalDataset);
		Assert.assertTrue("Data should not be null or empty",data!=null && data.length>0);
		IsoMap myIsomap = new IsoMap(data, dimensions, kNearest, cIsomap);
		double[][] coordinates = myIsomap.getCoordinates();
		Assert.assertTrue("Isomap results should not be null or empty",coordinates!=null && coordinates.length>0);
		Instances reData = TransformToFromWeka.isomapToWeka(coordinates, "isomapDataset", WekaUtils.getDatasetClassValues(originalDataset),
				"class");
		// CROSS VALIDATION
				AbstractClassifier abstractClassifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, reData);
				new NaiveBayesImplementation().crossValidationEvaluation(abstractClassifier, reData, 10,
						new Random(1));
			//	new NaiveBayesImplementation().classify(abstractClassifier, originalDataset);
//		Graph graph = myIsomap.getNearestNeighborGraph();
//		int[] indexes = myIsomap.getIndex();
//		Assert.assertArrayEquals("The dataset should be the same ", data, myIsomap.getDataset());
//		Assert.assertEquals("The dimension of the manifold should be equal to 2", 2, myIsomap.getD());
//		Assert.assertEquals("The k-nearest neighbour should be equal to 2", 2, myIsomap.getK());
	}
}

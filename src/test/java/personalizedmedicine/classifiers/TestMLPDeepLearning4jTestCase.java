package personalizedmedicine.classifiers;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.activations.ActivationReLU;
import weka.dl4j.activations.ActivationSoftmax;
import weka.dl4j.layers.DenseLayer;
import weka.dl4j.layers.OutputLayer;
import weka.dl4j.lossfunctions.LossMSE;
import weka.dl4j.updater.Adam;

/**
 * The Class TestPartTestCase.
 */
public class TestMLPDeepLearning4jTestCase {

	/** The class name. */
	private final String className = Constants.classRealName;

	/** The dataset file name. */
	private final String datasetFileName = Constants.completeFileName;

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/**
	 * Inits the.
	 *
	 * @param testInfo the test info
	 */
	@BeforeEach
	void init(TestInfo testInfo) {
//		Constants.logger.getLogger().log(Level.INFO, "START TEST");
//		Constants.logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " + datasetFileName);
//		Constants.logger.getLogger().log(Level.INFO, "SAVE DISPLAY NAME: " + testInfo.getDisplayName());
	}

	/**
	 * On End.
	 */
	@AfterEach
	void onEnd() {
		// Constants.logger.getLogger().log(Level.INFO, "END TEST");
	}

	/**
	 * Test part default.
	 */
	@Test
	@DisplayName("Multilayer Perceptron Default")
	public void testNeuralDefault() {
		// for (String datasetFileName : fileNames) {
		System.out.println(datasetFileName.toUpperCase());
		try {
			File level2File = new File(datasetFileName);
			Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
			AbstractClassifier classifier = WekaUtils.getClassifier(Constants.DEEPLEARNING4J, originalDataset,
					new String[] { "1", "true", "10" });
			WekaUtils.crossValidationAction(Constants.DEEPLEARNING4J, classifier, numFolds, random);
		} catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
		// }
	}

	/**
	 * Test part default.
	 * 
	 * @throws Exception
	 */
	@Test
	@DisplayName("Multilayer Perceptron")
	public void testNeuralClassifier() throws Exception {

		File level2File = new File(datasetFileName);
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);

		// Create a new Multi-Layer-Perceptron classifier
		Dl4jMlpClassifier clf = new Dl4jMlpClassifier();
		clf.setNumEpochs(50);

		DenseLayer denseLayer = new DenseLayer();
		denseLayer.setNOut(256);
		denseLayer.setActivationFunction(new ActivationReLU());

		DenseLayer denseLayer1 = new DenseLayer();
		denseLayer1.setNOut(256);
		denseLayer1.setActivationFunction(new ActivationReLU());

		DenseLayer denseLayer2 = new DenseLayer();
		denseLayer2.setNOut(256);
		denseLayer2.setActivationFunction(new ActivationReLU());

		// Define the output layer
		OutputLayer outputLayer = new OutputLayer();
		outputLayer.setActivationFunction(new ActivationSoftmax());
		outputLayer.setLossFn(new LossMSE());

		NeuralNetConfiguration nnc = new NeuralNetConfiguration();
		nnc.setUpdater(new Adam());
		nnc.setOptimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
		clf.setNeuralNetConfiguration(nnc);
		// Add the layers to the classifier
		clf.setLayers(denseLayer, denseLayer1, outputLayer);

		clf.buildClassifier(originalDataset);
		
		Evaluation eval = new Evaluation(originalDataset);
		eval.crossValidateModel(clf, originalDataset, numFolds, new Random(random));
		System.out.println(eval.toSummaryString("Evaluation results:\n", true));
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());

	}

}

package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.Constants;
import interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.layers.Layer;
import weka.dl4j.layers.OutputLayer;

/**
 * The Class Deeplearning4jWeka.
 */
public class Deeplearning4jWeka extends Dl4jMlpClassifier implements IAppClassifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4079049290038571519L;

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new deeplearning 4 j weka.
	 *
	 * @param options     the options
	 * @param outputLayer the output layer
	 * @param nnc         the nnc
	 * @param instances   the instances
	 */
	Deeplearning4jWeka(String[] options, OutputLayer outputLayer, NeuralNetConfiguration nnc, Instances instances) {
		// first option will be the seed
		// second option will be the debug
		// Create a new Multi-Layer-Perceptron classifier
		// Set a seed for reproducable results
		int seed = Integer.parseInt(options[0]);
		this.setSeed(seed);
		this.setDebug(Boolean.getBoolean(options[1]));
		this.setNumEpochs(Integer.parseInt(options[2]));
		// Add the layers to the classifier
		this.setLayers(new Layer[] { outputLayer });
		this.setNeuralNetConfiguration(nnc);
		this.instances = instances;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.IAppClassifier#crossValidationEvaluation(int,
	 * java.util.Random)
	 */
	@Override
	public void crossValidationEvaluation(int numFolds, Random random) {
		Evaluation eval;
		try {
			eval = new Evaluation(instances);
			eval.crossValidateModel(this, instances, numFolds, random);
			System.out.println(eval.toSummaryString("Evaluation results:\n", true));
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			Constants.logger.getLogger().log(Level.INFO, "{0}", eval.toSummaryString("Evaluation results:\n", true));
			Constants.logger.getLogger().log(Level.INFO, "{0}", eval.toClassDetailsString());
			Constants.logger.getLogger().log(Level.INFO, "{0}", eval.toMatrixString());
			// printCrossValidationResults(eval, data.classIndex());
		} catch (Exception e) {
			e.printStackTrace();
			Constants.logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
	}

	/**
	 * Gets the instances.
	 *
	 * @return the instances
	 */
	public Instances getInstances() {
		return instances;
	}

	/**
	 * Sets the instances.
	 *
	 * @param instances the new instances
	 */
	public void setInstances(Instances instances) {
		this.instances = instances;
	}
}

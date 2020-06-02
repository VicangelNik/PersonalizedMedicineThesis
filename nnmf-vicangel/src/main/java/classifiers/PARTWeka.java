package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.PART;
import weka.core.Instances;

/**
 * The Class PARTWeka.
 */
public class PARTWeka extends PART implements IAppClassifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4284871743715337165L;

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new PART weka.
	 *
	 * @param instances the instances
	 * @param options   the options
	 * @throws Exception the exception
	 */
	PARTWeka(Instances instances, String[] options) throws Exception {
		super();
		this.setInstances(instances);
		this.setOptions(options);
		logger.getLogger().log(Level.INFO, "Options: {0}", this.getOptions());
		this.buildClassifier(instances);
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
			logger.getLogger().log(Level.INFO, "{0}", eval.toSummaryString("Evaluation results:\n", true));
			logger.getLogger().log(Level.INFO, "{0}", eval.toClassDetailsString());
			logger.getLogger().log(Level.INFO, "{0}", eval.toMatrixString());
			// printCrossValidationResults(eval, data.classIndex());
		} catch (Exception e) {
			e.printStackTrace();
			logger.getLogger().log(Level.SEVERE, "{0}", e);
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

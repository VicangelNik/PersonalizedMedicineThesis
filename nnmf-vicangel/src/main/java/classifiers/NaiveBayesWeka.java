package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import interfaces.IAppClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * The Class NaiveBayesWeka.
 */
public class NaiveBayesWeka extends NaiveBayes implements IAppClassifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5425046691121634979L;

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new naive bayes weka.
	 *
	 * @param instances the instances
	 * @param options   the options
	 * @throws Exception the exception
	 */
	NaiveBayesWeka(Instances instances, String[] options) throws Exception {
		super();
		this.instances = instances;
		this.setOptions(options);
		logger.getLogger().log(Level.INFO, "Options: {0}", this.getOptions());
		// build classifier
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
}

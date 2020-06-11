package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.Constants;
import interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;

/**
 * The Class ZeroRWeka.
 */
public class ZeroRWeka extends ZeroR implements IAppClassifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1208983054963268481L;

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new zero R weka.
	 *
	 * @param instances the instances
	 * @param options   the options
	 * @throws Exception the exception
	 */
	ZeroRWeka(Instances instances, String[] options) throws Exception {
		super();
		this.instances = instances;
		this.setOptions(options);
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

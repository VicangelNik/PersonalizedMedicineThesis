package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.Constants;
import interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

/**
 * The Class IBkWeka.
 */
public class IBkWeka extends IBk implements IAppClassifier {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4417280640405983280L;

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new i bk weka.
	 *
	 * @param data    the data
	 * @param options the options
	 * @throws Exception the exception
	 */
	IBkWeka(Instances data, String[] options) throws Exception {
		super();
		instances = data;
		this.setOptions(options);
		this.buildClassifier(data);
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
			// Constants.logger.getLogger().log(Level.INFO, "{0}",
			// eval.toSummaryString("Evaluation results:\n", true));
			Constants.logger.getLogger().log(Level.INFO, "{0}", eval.toClassDetailsString());
			// Constants.logger.getLogger().log(Level.INFO, "{0}", eval.toMatrixString());
			// ClassifierChooser.printCrossValidationResults(eval, instances.classIndex());
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
	 * @param data the new instances
	 */
	public void setInstances(Instances data) {
		instances = data;
	}
}

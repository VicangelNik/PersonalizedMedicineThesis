package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
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

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/** The instances. */
	private Instances instances;

	/**
	 * Instantiates a new i bk weka.
	 *
	 * @param data    the data
	 * @param options the options
	 * @param debug   the debug
	 * @throws Exception the exception
	 */
	IBkWeka(Instances data, String[] options, boolean debug) throws Exception {
		super();
		instances = data;
		this.setOptions(options);
		this.buildClassifier(data);
		this.setDebug(debug);
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
	 * @param data the new instances
	 */
	public void setInstances(Instances data) {
		instances = data;
	}
}

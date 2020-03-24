package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import interfaces.AppClassifier;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * The Class PARTWeka.
 */
public class PARTWeka implements AppClassifier {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.AppClassifier#classify(weka.classifiers.AbstractClassifier,
	 * weka.core.Instances)
	 */
	@Override
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, Instances data) {
		try {
			abstractClassifier.buildClassifier(data);
			System.out.println(abstractClassifier);
			logger.getLogger().log(Level.INFO, "{0}", abstractClassifier);
			return abstractClassifier;
		} catch (Exception e) {
			e.printStackTrace();
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.AppClassifier#crossValidationEvaluation(weka.classifiers.
	 * AbstractClassifier, weka.core.Instances, int, java.util.Random)
	 */
	@Override
	public void crossValidationEvaluation(AbstractClassifier abstractClassifier, Instances data, int numFolds,
			Random random) {
		Evaluation eval;
		try {
			eval = new Evaluation(data);
			eval.crossValidateModel(abstractClassifier, data, numFolds, random);
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

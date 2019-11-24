package helpful_classes;

import java.util.Random;
import java.util.logging.Level;

import interfaces.AppClassifier;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;

public class ZeroRImplementation implements AppClassifier {

	/** The logger. */
	private static AppLogger logger = new AppLogger("classifier log", "classifiers\\classifiers.log");

	@Override
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, AbstractFileLoader loader) {
		System.out.println(abstractClassifier);
		logger.getLogger().log(Level.INFO, "{0}", abstractClassifier);
		return abstractClassifier;
	}

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
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}

	}

}

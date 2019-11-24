package helpful_classes;

import java.util.Random;
import java.util.logging.Level;

import interfaces.AppClassifier;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class NaiveBayesImplementation.
 */
public class NaiveBayesImplementation implements AppClassifier {

	/** The logger. */
	private static AppLogger logger = new AppLogger("classifier log", "classifiers\\classifiers.log");

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * interfaces.ClassifierSelection#classify(weka.classifiers.AbstractClassifier,
	 * weka.core.converters.AbstractFileLoader)
	 */
	@Override
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, AbstractFileLoader loader) {
		// train NaiveBayes
		try {
			Instance current;
			while ((current = loader.getNextInstance(loader.getStructure())) != null) {
				((NaiveBayes) abstractClassifier).updateClassifier(current);
			}
			System.out.println(abstractClassifier);
			logger.getLogger().log(Level.INFO, "{0}", abstractClassifier);
			return abstractClassifier;
		} catch (Exception e) {
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * interfaces.ClassifierSelection#crossValidationEvaluation(weka.classifiers.
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
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
	}

}

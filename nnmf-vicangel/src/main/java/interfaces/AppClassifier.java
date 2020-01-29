package interfaces;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Interface AppClassifier.
 */
public interface AppClassifier {

	/**
	 * Classify.
	 *
	 * @param abstractClassifier the abstract classifier
	 * @param data               the data
	 * @return the abstract classifier
	 */
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, Instances data);

	/**
	 * Cross validation evaluation.
	 *
	 * @param abstractClassifier the abstract classifier
	 * @param data               the data
	 * @param numFolds           the num folds
	 * @param random             the random
	 */
	public void crossValidationEvaluation(AbstractClassifier abstractClassifier, Instances data, int numFolds,
			Random random);
}

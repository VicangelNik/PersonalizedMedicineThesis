package interfaces;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;

public interface AppClassifier{

	/**
	 * Classify.
	 *
	 * @param abstractClassifier the abstract classifier
	 * @param loader             the loader
	 * @return the abstract classifier
	 */
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, AbstractFileLoader loader);

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

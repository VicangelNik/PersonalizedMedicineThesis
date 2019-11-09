package interfaces;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;

// TODO: Auto-generated Javadoc
/**
 * The Interface ClassifierSelection.
 */
public interface ClassifierSelection {

	/**
	 * Select classifier.
	 *
	 * @param selection the selection
	 * @param loader    the loader
	 * @return the abstract classifier
	 * @throws Exception the exception
	 */
	public AbstractClassifier selectClassifier(String selection, AbstractFileLoader loader) throws Exception;

	/**
	 * Classify.
	 *
	 * @param abstractClassifier the abstract classifier
	 * @param loader             the loader
	 * @return the abstract classifier
	 * @throws Exception the exception
	 */
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, AbstractFileLoader loader)
			throws Exception;

	/**
	 * Cross validation evaluation.
	 *
	 * @param abstractClassifier the abstract classifier
	 * @param data               the data
	 * @param numFolds           the num folds
	 * @param random             the random
	 * @throws Exception the exception
	 */
	public void crossValidationEvaluation(AbstractClassifier abstractClassifier, Instances data, int numFolds,
			Random random) throws Exception;
}

package java.personalizedmedicine.interfaces;

import java.util.Random;

/**
 * The Interface IAppClassifier.
 */
public interface IAppClassifier {

	/**
	 * Cross validation evaluation.
	 *
	 * @param numFolds the num folds
	 * @param random   the random
	 */
	public void crossValidationEvaluation(int numFolds, Random random);
}

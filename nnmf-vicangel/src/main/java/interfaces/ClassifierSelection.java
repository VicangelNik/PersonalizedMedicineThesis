package interfaces;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Interface ClassifierSelection.
 */
public interface ClassifierSelection {

	/**
	 * Select classifier.
	 *
	 * @param selection the selection
	 * @param instances the instances
	 * @param options   the options
	 * @return the abstract classifier
	 */
	public AbstractClassifier selectClassifier(String selection, Instances instances, String[] options);
}

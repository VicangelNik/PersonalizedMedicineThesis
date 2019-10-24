package interfaces;

import weka.classifiers.AbstractClassifier;
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

}

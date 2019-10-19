package interfaces;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Interface ClassifierSelection.
 */
public interface ClassifierSelection {

	/**
	 * Select classifier.
	 * 
	 * @return
	 */
	public AbstractClassifier selectClassifier(String selection, Instances instances);

}

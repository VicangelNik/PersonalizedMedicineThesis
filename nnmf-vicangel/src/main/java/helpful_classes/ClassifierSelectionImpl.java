package helpful_classes;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.converters.AbstractFileLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassifierSelectionImpl.
 */
public class ClassifierSelectionImpl implements interfaces.ClassifierSelection {

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.ClassifierSelection#selectClassifier(java.lang.String,
	 * weka.core.converters.AbstractFileLoader)
	 */
	@Override
	public AbstractClassifier selectClassifier(String selection, AbstractFileLoader loader) throws Exception {
		AbstractClassifier abstractClassifier = null;
		switch (selection) {
		case Constants.NAIVE_BAYES: {
			abstractClassifier = new NaiveBayesUpdateable();
			break;
		}
		default: {
			throw new IllegalArgumentException("Select a classifier");
		}
		}
		System.out.println(abstractClassifier.getCapabilities());
		// train NaiveBayes
		abstractClassifier.buildClassifier(loader.getStructure());
		Instance current;
		while ((current = loader.getNextInstance(loader.getStructure())) != null) {
			((NaiveBayes) abstractClassifier).updateClassifier(current);
		}
		return abstractClassifier;
	}
}

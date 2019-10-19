package helpful_classes;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class ClassifierSelectionImpl implements interfaces.ClassifierSelection {

	@Override
	public AbstractClassifier selectClassifier(String selection, Instances instances) {
		AbstractClassifier abstractClassifier = null;
		switch (selection) {
		case Constants.NAIVE_BAYES: {
			abstractClassifier = new NaiveBayes();
			break;
		}
		default: {
			throw new IllegalArgumentException("Select a classifier");
		}
		}
		return abstractClassifier;
	}
}

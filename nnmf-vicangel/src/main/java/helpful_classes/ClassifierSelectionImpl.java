package helpful_classes;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

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
		Instances data = loader.getDataSet();
		switch (selection) {
		case Constants.NAIVE_BAYES: {
			abstractClassifier = new NaiveBayesUpdateable();
			StringToNominal stringToNominal = new StringToNominal();
			String[] options = new String[2];
			options[0] = "-R"; // "range"
			options[1] = Integer.toString(73663);
			stringToNominal.setOptions(options);
			stringToNominal.setInputFormat(data);
			data = Filter.useFilter(data, stringToNominal);
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

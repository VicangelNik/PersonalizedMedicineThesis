package helpful_classes;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
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

		return abstractClassifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.ClassifierSelection#classify(weka.classifiers.AbstractClassifier,
	 * weka.core.converters.AbstractFileLoader)
	 */
	@Override
	public AbstractClassifier classify(AbstractClassifier abstractClassifier, AbstractFileLoader loader)
			throws Exception {
		// train NaiveBayes
		abstractClassifier.buildClassifier(loader.getStructure());
		Instance current;
		while ((current = loader.getNextInstance(loader.getStructure())) != null) {
			((NaiveBayes) abstractClassifier).updateClassifier(current);
		}
		System.out.println(abstractClassifier);
		return abstractClassifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.ClassifierSelection#crossValidationEvaluation(weka.classifiers.
	 * AbstractClassifier, weka.core.Instances, int, java.util.Random)
	 */
	@Override
	public void crossValidationEvaluation(AbstractClassifier abstractClassifier, Instances data, int numFolds,
			Random random) throws Exception {
		Evaluation eval = new Evaluation(data);
		eval.crossValidateModel(abstractClassifier, data, numFolds, random);
		System.out.println(eval.toSummaryString("Evaluation results:\n", true));
	}
}

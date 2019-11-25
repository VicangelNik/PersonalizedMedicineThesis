package helpful_classes;

import java.util.logging.Level;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.ZeroR;
import weka.core.converters.AbstractFileLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassifierSelectionImpl.
 */
public class ClassifierSelectionImpl implements interfaces.ClassifierSelection {

	/** The logger. */
	private static AppLogger logger = new AppLogger("classifier log", "classifiers\\classifiers.log");

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.ClassifierSelection#selectClassifier(java.lang.String,
	 * weka.core.converters.AbstractFileLoader)
	 */
	@Override
	public AbstractClassifier selectClassifier(String selection, AbstractFileLoader loader) {
		AbstractClassifier abstractClassifier = null;
		switch (selection) {
		case Constants.NAIVE_BAYES: {
			abstractClassifier = new NaiveBayesUpdateable();
			break;
		}
		case Constants.ZERO_R: {
			abstractClassifier = new ZeroR();
			break;
		}
		default: {
			throw new IllegalArgumentException("Select a classifier");
		}
		}
		try {
			// if (abstractClassifier instanceof ZeroR) {
			// abstractClassifier.buildClassifier(loader.getDataSet());
			// } else {
			abstractClassifier.buildClassifier(loader.getStructure());
			// }
		} catch (Exception e) {
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
		abstractClassifier.setDebug(true);
		
		System.out.println(abstractClassifier.getCapabilities());
		logger.getLogger().log(Level.INFO, "{0}", abstractClassifier.getCapabilities());
		//logger.getLogger().log(Level.INFO, abstractClassifier.getRevision());
		logger.getLogger().log(Level.INFO,"Is in debug mode: {0}", abstractClassifier.getDebug());
		logger.getLogger().log(Level.INFO,"Current settings of the classifier: {0}", abstractClassifier.getOptions());
		return abstractClassifier;
	}

	/**
	 * Prints the cross validation results.
	 *
	 * @param eval       the eval
	 * @param classIndex the class index
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unused")
	private void printCrossValidationResults(Evaluation eval, int classIndex) throws Exception {
		System.out.println(eval.correct());
		// System.out.println(eval.correlationCoefficient());
		System.out.println(eval.coverageOfTestCasesByPredictedRegions());
		System.out.println(eval.errorRate());
		System.out.println(eval.falseNegativeRate(classIndex));
		System.out.println(eval.falsePositiveRate(classIndex));
		System.out.println(eval.fMeasure(classIndex));
		System.out.println(eval.getClassPriors());
		System.out.println(eval.incorrect());
		System.out.println(eval.kappa());
		System.out.println(eval.KBInformation());
		System.out.println(eval.KBMeanInformation());
		System.out.println(eval.KBRelativeInformation());
		System.out.println(eval.areaUnderPRC(classIndex));
		System.out.println(eval.avgCost());
		System.out.println(eval.areaUnderROC(classIndex));
		System.out.println(eval.matthewsCorrelationCoefficient(classIndex));
		System.out.println(eval.meanAbsoluteError());
		System.out.println(eval.meanPriorAbsoluteError());
		System.out.println(eval.missingClass());
		System.out.println(eval.numFalseNegatives(classIndex));
		System.out.println(eval.numFalsePositives(classIndex));
		System.out.println(eval.numTrueNegatives(classIndex));
		System.out.println(eval.numTruePositives(classIndex));
		System.out.println(eval.pctCorrect());
		System.out.println(eval.pctIncorrect());
		System.out.println(eval.pctUnclassified());
		System.out.println(eval.precision(classIndex));
		System.out.println(eval.priorEntropy());
		System.out.println(eval.recall(classIndex));
		System.out.println(eval.relativeAbsoluteError());
		System.out.println(eval.rootMeanPriorSquaredError());
		System.out.println(eval.rootMeanSquaredError());
		System.out.println(eval.rootRelativeSquaredError());
		System.out.println(eval.SFEntropyGain());
		System.out.println(eval.SFMeanEntropyGain());
		System.out.println(eval.SFMeanPriorEntropy());
		System.out.println(eval.SFMeanSchemeEntropy());
		System.out.println(eval.SFPriorEntropy());
		System.out.println(eval.SFSchemeEntropy());
		System.out.println(eval.sizeOfPredictedRegions());
		System.out.println(eval.totalCost());
		System.out.println(eval.trueNegativeRate(classIndex));
		System.out.println(eval.truePositiveRate(classIndex));
		System.out.println(eval.unclassified());
		System.out.println(eval.unweightedMacroFmeasure());
		System.out.println(eval.unweightedMicroFmeasure());
		System.out.println(eval.weightedAreaUnderPRC());
		System.out.println(eval.weightedAreaUnderROC());
		System.out.println(eval.weightedFalseNegativeRate());
		System.out.println(eval.weightedFalsePositiveRate());
		System.out.println(eval.weightedFMeasure());
		System.out.println(eval.weightedMatthewsCorrelation());
		System.out.println(eval.weightedPrecision());
		System.out.println(eval.weightedRecall());
		System.out.println(eval.weightedTrueNegativeRate());
		System.out.println(eval.weightedTruePositiveRate());
	}
}

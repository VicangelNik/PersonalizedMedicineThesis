package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.IClassifierSelection;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * The Class ClassifierChooser.
 */
public class ClassifierChooser implements IClassifierSelection {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.IClassifierSelection#selectClassifier(java.lang.String,
	 * weka.core.Instances, java.lang.String[])
	 */
	@Override
	public AbstractClassifier selectClassifier(String selection, Instances instances, String[] options) {
		AbstractClassifier abstractClassifier = null;
		boolean debug = true;
		try {
			switch (selection) {
			case Constants.NAIVE_BAYES: {
				abstractClassifier = new NaiveBayesWeka(instances, options, debug);
				break;
			}
			case Constants.ZERO_R: {
				abstractClassifier = new ZeroRWeka(instances, options, debug);
				break;
			}
			case Constants.JRIP: {
				abstractClassifier = new JRipWeka(instances, options, debug);
				break;
			}
			case Constants.PART: {
				abstractClassifier = new PARTWeka(instances, options, debug);
				break;
			}
			case Constants.IBK: {
				abstractClassifier = new IBkWeka(instances, options, debug);
				break;
			}
			default: {
				throw new IllegalArgumentException("Select a classifier");
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
		System.out.println(abstractClassifier.getCapabilities());
		logger.getLogger().log(Level.INFO, "{0}", abstractClassifier.getCapabilities());
		// logger.getLogger().log(Level.INFO, abstractClassifier.getRevision());
		logger.getLogger().log(Level.INFO, "Is in debug mode: {0}", abstractClassifier.getDebug());
		logger.getLogger().log(Level.INFO, "Current settings of the classifier: {0}", abstractClassifier.getOptions());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.IClassifierSelection#crossValidationAction(java.lang.String,
	 * weka.classifiers.AbstractClassifier, int, int)
	 */
	@Override
	public void crossValidationAction(String clSelection, AbstractClassifier classifier, int numFolds, int random) {
		Random rand = new Random(random);
		switch (clSelection) {
		case Constants.NAIVE_BAYES: {
			((NaiveBayesWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		case Constants.ZERO_R: {
			((ZeroRWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		case Constants.JRIP: {
			((JRipWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		case Constants.PART: {
			((PARTWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		case Constants.IBK: {
			((IBkWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		default: {
			throw new IllegalArgumentException("Select a classifier");
		}
		}
	}
}

package classifiers;

import java.util.Random;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.IClassifierSelection;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.activations.ActivationSoftmax;
import weka.dl4j.layers.OutputLayer;
import weka.dl4j.lossfunctions.LossMCXENT;
import weka.dl4j.updater.Adam;

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
		try {
			switch (selection) {
			case Constants.NAIVE_BAYES_UPDATABLE: {
				abstractClassifier = new NaiveBayesUpdatableWeka(instances, options);
				break;
			}
			case Constants.NAIVE_BAYES: {
				abstractClassifier = new NaiveBayesWeka(instances, options);
				break;
			}
			case Constants.ZERO_R: {
				abstractClassifier = new ZeroRWeka(instances, options);
				break;
			}
			case Constants.JRIP: {
				abstractClassifier = new JRipWeka(instances, options);
				break;
			}
			case Constants.PART: {
				abstractClassifier = new PARTWeka(instances, options);
				break;
			}
			case Constants.IBK: {
				abstractClassifier = new IBkWeka(instances, options);
				break;
			}
			case Constants.DEEPLEARNING4J: {
				// Define the output layer
				OutputLayer outputLayer = new OutputLayer();
				outputLayer.setActivationFunction(new ActivationSoftmax());
				outputLayer.setLossFn(new LossMCXENT());

				NeuralNetConfiguration nnc = new NeuralNetConfiguration();
				nnc.setUpdater(new Adam());
				abstractClassifier = new Deeplearning4jWeka(options, outputLayer, nnc, instances);
				break;
			}
			default: {
				throw new IllegalArgumentException("Select a classifier");
			}
			}
			logger.getLogger().log(Level.INFO, "Current settings of the classifier: {0}",
					abstractClassifier.getOptions());
		} catch (Exception e) {
			e.printStackTrace();
			logger.getLogger().log(Level.SEVERE, "{0}", e);
		}
		return abstractClassifier;
	}

	/**
	 * Prints the cross validation results.
	 *
	 * @param eval       the eval
	 * @param classIndex the class index
	 * @throws Exception the exception
	 */

	public static void printCrossValidationResults(Evaluation eval, int classIndex) throws Exception {
		logger.getLogger().log(Level.INFO, "Number of instances correctly classified: {0}", eval.correct());
		logger.getLogger().log(Level.INFO, "Number of incorrectly classified instances: {0}, ", eval.incorrect());
		logger.getLogger().log(Level.INFO, "Number of unclassified instances: {0}", eval.unclassified());
		logger.getLogger().log(Level.INFO, "Percent of correctly classified instances: {0}", eval.pctCorrect());
		logger.getLogger().log(Level.INFO, "Percent of incorrectly classified instances: {0}", eval.pctIncorrect());
		logger.getLogger().log(Level.INFO, "Percent of unclassified instances: {0} ", eval.pctUnclassified());
		try {
			logger.getLogger().log(Level.INFO, "Correlation coefficient if the class is numeric: {0}",
					eval.correlationCoefficient());
		} catch (Exception e) {
			logger.getLogger().log(Level.SEVERE, e.getMessage());
		}
		// logger.getLogger().log(Level.INFO, "Mathews correlation coefficient: {0}",
		// eval.matthewsCorrelationCoefficient(classIndex));
		logger.getLogger().log(Level.INFO, "Coverage of the test cases by the predicted regions: {0}",
				eval.coverageOfTestCasesByPredictedRegions());
		logger.getLogger().log(Level.INFO, "The estimated error rate: {0}", eval.errorRate());
		// logger.getLogger().log(Level.INFO, "False positive rate: {0}",
		// eval.falseNegativeRate(classIndex));
		// logger.getLogger().log(Level.INFO, "False negative rate: {0}",
		// eval.falsePositiveRate(classIndex));
		// logger.getLogger().log(Level.INFO, "F-Measure: {0}",
		// eval.fMeasure(classIndex));
		logger.getLogger().log(Level.INFO, "The weighted class counts: {0}", eval.getClassPriors());
		logger.getLogger().log(Level.INFO, "Kappa statistic: {0}", eval.kappa());
		logger.getLogger().log(Level.INFO, "K&B information score: {0}", eval.KBInformation());
		logger.getLogger().log(Level.INFO, "K&B mean information score: {0}", eval.KBMeanInformation());
		logger.getLogger().log(Level.INFO, "K&B relative information score: {0}", eval.KBRelativeInformation());
		// logger.getLogger().log(Level.INFO, "Area under the precision-recall curve:
		// {0}", eval.areaUnderPRC(classIndex));
		// logger.getLogger().log(Level.INFO, "Area under the ROC curve: {0}",
		// eval.areaUnderROC(classIndex));
		logger.getLogger().log(Level.INFO, "Average cost: {0}", eval.avgCost());
		logger.getLogger().log(Level.INFO, "Total cost: {0}", eval.totalCost());
		logger.getLogger().log(Level.INFO, "Mean absolute error: {0}", eval.meanAbsoluteError());
		logger.getLogger().log(Level.INFO, "Mean absolute prior error: {0}", eval.meanPriorAbsoluteError());
		try {
			logger.getLogger().log(Level.INFO, "Relative absolute error: {0}", eval.relativeAbsoluteError());
		} catch (Exception e) {
			logger.getLogger().log(Level.SEVERE, e.getMessage());
		}
		logger.getLogger().log(Level.INFO, "Root mean prior squared error: {0}", eval.rootMeanPriorSquaredError());
		logger.getLogger().log(Level.INFO, "Root mean squared error: {0}", eval.rootMeanSquaredError());
		logger.getLogger().log(Level.INFO, "Root relative squared error: {0}", eval.rootRelativeSquaredError());
		logger.getLogger().log(Level.INFO, "Weight of the instances that had missing class values: {0}",
				eval.missingClass());
		// logger.getLogger().log(Level.INFO, "False positive rate: {0}",
		// eval.numFalseNegatives(classIndex));
		// logger.getLogger().log(Level.INFO, "False negative rate: {0}",
		// eval.numFalsePositives(classIndex));
		// logger.getLogger().log(Level.INFO, "True negative rate: {0}",
		// eval.numTrueNegatives(classIndex));
		// logger.getLogger().log(Level.INFO, "True positive rate: {0}",
		// eval.numTruePositives(classIndex));
		// logger.getLogger().log(Level.INFO, "True negative rate: {0}",
		// eval.trueNegativeRate(classIndex));
		// logger.getLogger().log(Level.INFO, "True positive rate: {0}",
		// eval.truePositiveRate(classIndex));
		// logger.getLogger().log(Level.INFO, "Precision: {0}",
		// eval.precision(classIndex));
		// logger.getLogger().log(Level.INFO, "Recall: {0}", eval.recall(classIndex));
		// logger.getLogger().log(Level.INFO, "Null model entropy per instance: {0}",
		// eval.priorEntropy()); // Same as SFMeanPriorEntropy
		logger.getLogger().log(Level.INFO, "Total SF score: {0}", eval.SFEntropyGain());
		logger.getLogger().log(Level.INFO, "Mean SF score: {0}", eval.SFMeanEntropyGain());
		logger.getLogger().log(Level.INFO, "Total null model entropy: {0}", eval.SFPriorEntropy());
		logger.getLogger().log(Level.INFO, "Null model entropy per instance: {0}", eval.SFMeanPriorEntropy());
		logger.getLogger().log(Level.INFO, "Total scheme entropy: {0}", eval.SFSchemeEntropy());
		logger.getLogger().log(Level.INFO, "Scheme entropy per instance: {0}", eval.SFMeanSchemeEntropy());
		logger.getLogger().log(Level.INFO, "Average size of the predicted regions: {0}", eval.sizeOfPredictedRegions());
		logger.getLogger().log(Level.INFO, "Unweighted macro-averaged F-measure: {0}", eval.unweightedMacroFmeasure());
		logger.getLogger().log(Level.INFO, "Unweighted micro-averaged F-measure: {0}", eval.unweightedMicroFmeasure());
		logger.getLogger().log(Level.INFO, "Weighted AUPRC: {0}", eval.weightedAreaUnderPRC());
		logger.getLogger().log(Level.INFO, "Weighted AUC: {0}", eval.weightedAreaUnderROC());
		logger.getLogger().log(Level.INFO, "Weighted false negative rate: {0}", eval.weightedFalseNegativeRate());
		logger.getLogger().log(Level.INFO, "Weighted false positive rate: {0}", eval.weightedFalsePositiveRate());
		logger.getLogger().log(Level.INFO, "Weighted F-Measure: {0}", eval.weightedFMeasure());
		logger.getLogger().log(Level.INFO, "Weighted matthews correlation coefficient: {0}",
				eval.weightedMatthewsCorrelation());
		logger.getLogger().log(Level.INFO, "Weighted precision: {0}", eval.weightedPrecision());
		logger.getLogger().log(Level.INFO, "Weighted recall: {0}", eval.weightedRecall());
		logger.getLogger().log(Level.INFO, "Weighted true negative rate: {0}", eval.weightedTrueNegativeRate());
		logger.getLogger().log(Level.INFO, "Weighted true positive rate: {0}", eval.weightedTruePositiveRate());
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
		case Constants.NAIVE_BAYES_UPDATABLE: {
			((NaiveBayesUpdatableWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
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
		case Constants.DEEPLEARNING4J: {
			((Deeplearning4jWeka) classifier).crossValidationEvaluation(numFolds, rand);
			break;
		}
		default: {
			throw new IllegalArgumentException("Select a classifier");
		}
		}
	}
}

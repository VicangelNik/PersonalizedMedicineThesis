package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.interfaces.IClassifierSelection;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.activations.ActivationSoftmax;
import weka.dl4j.layers.OutputLayer;
import weka.dl4j.lossfunctions.LossMCXENT;
import weka.dl4j.updater.Adam;

import java.util.Arrays;
import java.util.Random;

@Slf4j
public class ClassifierChooser implements IClassifierSelection {

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
        } catch (Exception e) {
            log.error(e.getMessage());
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
        log.info("Number of instances correctly classified: " + eval.correct());
        log.info("Number of incorrectly classified instances: " + eval.incorrect());
        log.info("Number of unclassified instances: " + eval.unclassified());
        log.info("Percent of correctly classified instances: " + eval.pctCorrect());
        log.info("Percent of incorrectly classified instances: " + eval.pctIncorrect());
        log.info("Percent of unclassified instances:  " + eval.pctUnclassified());
        try {
            log.info("Correlation coefficient if the class is numeric: " + eval.correlationCoefficient());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        // logger.getLogger().log(Level.INFO, "Mathews correlation coefficient: ",
        // eval.matthewsCorrelationCoefficient(classIndex));
        log.info(
                "Coverage of the test cases by the predicted regions: " + eval.coverageOfTestCasesByPredictedRegions());
        log.info("The estimated error rate: " + eval.errorRate());
        // logger.getLogger().log(Level.INFO, "False positive rate: ",
        // eval.falseNegativeRate(classIndex));
        // logger.getLogger().log(Level.INFO, "False negative rate: ",
        // eval.falsePositiveRate(classIndex));
        // logger.getLogger().log(Level.INFO, "F-Measure: ",
        // eval.fMeasure(classIndex));
        log.info("The weighted class counts: " + Arrays.toString(eval.getClassPriors()));
        log.info("Kappa statistic: " + eval.kappa());
        log.info("K&B information score: " + eval.KBInformation());
        log.info("K&B mean information score: " + eval.KBMeanInformation());
        log.info("K&B relative information score: " + eval.KBRelativeInformation());
        // logger.getLogger().log(Level.INFO, "Area under the precision-recall curve:
        // ", eval.areaUnderPRC(classIndex));
        // logger.getLogger().log(Level.INFO, "Area under the ROC curve: ",
        // eval.areaUnderROC(classIndex));
        log.info("Average cost: " + eval.avgCost());
        log.info("Total cost: " + eval.totalCost());
        log.info("Mean absolute error: " + eval.meanAbsoluteError());
        log.info("Mean absolute prior error: " + eval.meanPriorAbsoluteError());
        try {
            log.info("Relative absolute error: " + eval.relativeAbsoluteError());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Root mean prior squared error: " + eval.rootMeanPriorSquaredError());
        log.info("Root mean squared error: " + eval.rootMeanSquaredError());
        log.info("Root relative squared error: " + eval.rootRelativeSquaredError());
        log.info("Weight of the instances that had missing class values: " + eval.missingClass());
        // logger.getLogger().log(Level.INFO, "False positive rate: ",
        // eval.numFalseNegatives(classIndex));
        // logger.getLogger().log(Level.INFO, "False negative rate: ",
        // eval.numFalsePositives(classIndex));
        // logger.getLogger().log(Level.INFO, "True negative rate: ",
        // eval.numTrueNegatives(classIndex));
        // logger.getLogger().log(Level.INFO, "True positive rate: ",
        // eval.numTruePositives(classIndex));
        // logger.getLogger().log(Level.INFO, "True negative rate: ",
        // eval.trueNegativeRate(classIndex));
        // logger.getLogger().log(Level.INFO, "True positive rate: ",
        // eval.truePositiveRate(classIndex));
        // logger.getLogger().log(Level.INFO, "Precision: ",
        // eval.precision(classIndex));
        // logger.getLogger().log(Level.INFO, "Recall: ", eval.recall(classIndex));
        // logger.getLogger().log(Level.INFO, "Null model entropy per instance: ",
        // eval.priorEntropy()); // Same as SFMeanPriorEntropy
        log.info("Total SF score: " + eval.SFEntropyGain());
        log.info("Mean SF score: " + eval.SFMeanEntropyGain());
        log.info("Total null model entropy: " + eval.SFPriorEntropy());
        log.info("Null model entropy per instance: " + eval.SFMeanPriorEntropy());
        log.info("Total scheme entropy: " + eval.SFSchemeEntropy());
        log.info("Scheme entropy per instance: " + eval.SFMeanSchemeEntropy());
        log.info("Average size of the predicted regions: " + eval.sizeOfPredictedRegions());
        log.info("Unweighted macro-averaged F-measure: " + eval.unweightedMacroFmeasure());
        log.info("Unweighted micro-averaged F-measure: " + eval.unweightedMicroFmeasure());
        log.info("Weighted AUPRC: " + eval.weightedAreaUnderPRC());
        log.info("Weighted AUC: " + eval.weightedAreaUnderROC());
        log.info("Weighted false negative rate: " + eval.weightedFalseNegativeRate());
        log.info("Weighted false positive rate: " + eval.weightedFalsePositiveRate());
        log.info("Weighted F-Measure: " + eval.weightedFMeasure());
        log.info("Weighted matthews correlation coefficient: " + eval.weightedMatthewsCorrelation());
        log.info("Weighted precision: " + eval.weightedPrecision());
        log.info("Weighted recall: " + eval.weightedRecall());
        log.info("Weighted true negative rate: " + eval.weightedTrueNegativeRate());
        log.info("Weighted true positive rate: " + eval.weightedTruePositiveRate());
    }

    /*
     * (non-Javadoc)
     *
     * @see interfaces.IClassifierSelection#crossValidationAction(java.lang.String,
     * weka.classifiers.AbstractClassifier, int, int)
     */
    @Override
    public void crossValidationAction(String clSelection, AbstractClassifier classifier, int numFolds, int random) {
        val rand = new Random(random);
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

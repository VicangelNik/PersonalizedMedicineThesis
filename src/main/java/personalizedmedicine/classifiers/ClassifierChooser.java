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

import java.util.Random;
import java.util.logging.Level;

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
        log.info("Number of instances correctly classified: {0}", eval.correct());
        log.info("Number of incorrectly classified instances: {0}, ", eval.incorrect());
        log.info("Number of unclassified instances: {0}", eval.unclassified());
        log.info("Percent of correctly classified instances: {0}", eval.pctCorrect());
        log.info("Percent of incorrectly classified instances: {0}", eval.pctIncorrect());
        log.info("Percent of unclassified instances: {0} ", eval.pctUnclassified());
        try {
            Constants.logger.getLogger()
                            .log(Level.INFO, "Correlation coefficient if the class is numeric: {0}",
                                 eval.correlationCoefficient());
        } catch (Exception e) {
            Constants.logger.getLogger().log(Level.SEVERE, e.getMessage());
        }
        // logger.getLogger().log(Level.INFO, "Mathews correlation coefficient: {0}",
        // eval.matthewsCorrelationCoefficient(classIndex));
        log.info("Coverage of the test cases by the predicted regions: {0}",
                 eval.coverageOfTestCasesByPredictedRegions());
        log.info("The estimated error rate: {0}", eval.errorRate());
        // logger.getLogger().log(Level.INFO, "False positive rate: {0}",
        // eval.falseNegativeRate(classIndex));
        // logger.getLogger().log(Level.INFO, "False negative rate: {0}",
        // eval.falsePositiveRate(classIndex));
        // logger.getLogger().log(Level.INFO, "F-Measure: {0}",
        // eval.fMeasure(classIndex));
        log.info("The weighted class counts: {0}", eval.getClassPriors());
        log.info("Kappa statistic: {0}", eval.kappa());
        log.info("K&B information score: {0}", eval.KBInformation());
        log.info("K&B mean information score: {0}", eval.KBMeanInformation());
        log.info("K&B relative information score: {0}", eval.KBRelativeInformation());
        // logger.getLogger().log(Level.INFO, "Area under the precision-recall curve:
        // {0}", eval.areaUnderPRC(classIndex));
        // logger.getLogger().log(Level.INFO, "Area under the ROC curve: {0}",
        // eval.areaUnderROC(classIndex));
        log.info("Average cost: {0}", eval.avgCost());
        log.info("Total cost: {0}", eval.totalCost());
        log.info("Mean absolute error: {0}", eval.meanAbsoluteError());
        log.info("Mean absolute prior error: {0}", eval.meanPriorAbsoluteError());
        try {
            log.info("Relative absolute error: {0}", eval.relativeAbsoluteError());
        } catch (Exception e) {
            Constants.logger.getLogger().log(Level.SEVERE, e.getMessage());
        }
        log.info("Root mean prior squared error: {0}", eval.rootMeanPriorSquaredError());
        log.info("Root mean squared error: {0}", eval.rootMeanSquaredError());
        log.info("Root relative squared error: {0}", eval.rootRelativeSquaredError());
        log.info("Weight of the instances that had missing class values: {0}", eval.missingClass());
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
        log.info("Total SF score: {0}", eval.SFEntropyGain());
        log.info("Mean SF score: {0}", eval.SFMeanEntropyGain());
        log.info("Total null model entropy: {0}", eval.SFPriorEntropy());
        log.info("Null model entropy per instance: {0}", eval.SFMeanPriorEntropy());
        log.info("Total scheme entropy: {0}", eval.SFSchemeEntropy());
        log.info("Scheme entropy per instance: {0}", eval.SFMeanSchemeEntropy());
        log.info("Average size of the predicted regions: {0}", eval.sizeOfPredictedRegions());
        log.info("Unweighted macro-averaged F-measure: {0}", eval.unweightedMacroFmeasure());
        log.info("Unweighted micro-averaged F-measure: {0}", eval.unweightedMicroFmeasure());
        log.info("Weighted AUPRC: {0}", eval.weightedAreaUnderPRC());
        log.info("Weighted AUC: {0}", eval.weightedAreaUnderROC());
        log.info("Weighted false negative rate: {0}", eval.weightedFalseNegativeRate());
        log.info("Weighted false positive rate: {0}", eval.weightedFalsePositiveRate());
        log.info("Weighted F-Measure: {0}", eval.weightedFMeasure());
        log.info("Weighted matthews correlation coefficient: {0}", eval.weightedMatthewsCorrelation());
        log.info("Weighted precision: {0}", eval.weightedPrecision());
        log.info("Weighted recall: {0}", eval.weightedRecall());
        log.info("Weighted true negative rate: {0}", eval.weightedTrueNegativeRate());
        log.info("Weighted true positive rate: {0}", eval.weightedTruePositiveRate());
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

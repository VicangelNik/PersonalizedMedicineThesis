package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.activations.ActivationReLU;
import weka.dl4j.activations.ActivationSoftmax;
import weka.dl4j.layers.DenseLayer;
import weka.dl4j.layers.OutputLayer;
import weka.dl4j.lossfunctions.LossMSE;
import weka.dl4j.updater.Adam;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Slf4j
@Disabled("java.lang.NoClassDefFoundError: Could not initialize class org.nd4j.linalg.factory.Nd4j")
class TestMLPDeepLearning4jTestCase extends ClassifierTest {

    @Test
    @DisplayName("Multilayer Perceptron Default")
    void testNeuralDefault() {
        // for (String datasetFileName : fileNames) {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
            AbstractClassifier classifier = WekaUtils.getClassifier(Constants.DEEPLEARNING4J, originalDataset,
                                                                    new String[]{"1", "true", "10"});
            WekaUtils.crossValidationAction(Constants.DEEPLEARNING4J, classifier, numFolds, random);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
        // }
    }
    
    @Test
    @DisplayName("Multilayer Perceptron")
    void testNeuralClassifier() {
        try {
            File level2File = new File(datasetFileName);
            Instances originalDataset = WekaUtils.getOriginalData(level2File, className);

            // Create a new Multi-Layer-Perceptron classifier
            Dl4jMlpClassifier clf = new Dl4jMlpClassifier();
            clf.setNumEpochs(50);

            DenseLayer denseLayer = new DenseLayer();
            denseLayer.setNOut(256);
            denseLayer.setActivationFunction(new ActivationReLU());

            DenseLayer denseLayer1 = new DenseLayer();
            denseLayer1.setNOut(256);
            denseLayer1.setActivationFunction(new ActivationReLU());

            DenseLayer denseLayer2 = new DenseLayer();
            denseLayer2.setNOut(256);
            denseLayer2.setActivationFunction(new ActivationReLU());

            // Define the output layer
            OutputLayer outputLayer = new OutputLayer();
            outputLayer.setActivationFunction(new ActivationSoftmax());
            outputLayer.setLossFn(new LossMSE());

            NeuralNetConfiguration nnc = new NeuralNetConfiguration();
            nnc.setUpdater(new Adam());
            nnc.setOptimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
            clf.setNeuralNetConfiguration(nnc);
            // Add the layers to the classifier
            clf.setLayers(denseLayer, denseLayer1, outputLayer);

            clf.buildClassifier(originalDataset);

            Evaluation eval = new Evaluation(originalDataset);
            eval.crossValidateModel(clf, originalDataset, numFolds, new Random(random));
            log.info(eval.toSummaryString("Evaluation results:\n", true));
            log.info(eval.toClassDetailsString());
            log.info(eval.toMatrixString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

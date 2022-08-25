package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.layers.OutputLayer;

import java.util.Random;

@Slf4j
public class Deeplearning4jWeka extends Dl4jMlpClassifier implements IAppClassifier {

    /**
     * The instances.
     */
    private final Instances instances;

    /**
     * Instantiates a new deeplearning 4 j weka.
     *
     * @param options     the options
     * @param outputLayer the output layer
     * @param nnc         the nnc
     * @param instances   the instances
     */
    Deeplearning4jWeka(String[] options, OutputLayer outputLayer, NeuralNetConfiguration nnc, Instances instances) {
        // first option will be the seed
        // second option will be the debug
        // Create a new Multi-Layer-Perceptron classifier
        // Set a seed for reproducable results
        int seed = Integer.parseInt(options[0]);
        this.setSeed(seed);
        this.setDebug(Boolean.getBoolean(options[1]));
        this.setNumEpochs(Integer.parseInt(options[2]));
        // Add the layers to the classifier
        this.setLayers(outputLayer);
        this.setNeuralNetConfiguration(nnc);
        this.instances = instances;
    }

    /*
     * (non-Javadoc)
     *
     * @see interfaces.IAppClassifier#crossValidationEvaluation(int,
     * java.util.Random)
     */
    @Override
    public void crossValidationEvaluation(int numFolds, Random random) {
        try {
            val eval = new Evaluation(instances);
            eval.crossValidateModel(this, instances, numFolds, random);
            log.info(eval.toSummaryString("Evaluation results:\n", true));
            log.info(eval.toClassDetailsString());
            log.info(eval.toMatrixString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Instances getInstances() {
        return instances;
    }

}

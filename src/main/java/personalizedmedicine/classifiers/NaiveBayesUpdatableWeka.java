package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.interfaces.IAppClassifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Enumeration;
import java.util.Random;

@Slf4j
public class NaiveBayesUpdatableWeka extends NaiveBayesUpdateable implements IAppClassifier {

    private final Instances instances;

    /**
     * Instantiates a new naive bayes weka.
     *
     * @param data    the data
     * @param options the options
     * @throws Exception the exception
     */
    NaiveBayesUpdatableWeka(Instances data, String[] options) throws Exception {
        super();
        instances = data;
        this.setOptions(options);
        Enumeration<Instance> enumeration = instances.enumerateInstances();
        // build classifier
        this.buildClassifier(instances);
        while (enumeration.hasMoreElements()) {
            this.updateClassifier(enumeration.nextElement());
        }
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

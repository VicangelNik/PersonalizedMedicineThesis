package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.interfaces.IAppClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

import java.util.Random;

@Slf4j
public class NaiveBayesWeka extends NaiveBayes implements IAppClassifier {

    private final Instances instances;

    /**
     * Instantiates a new naive bayes weka.
     *
     * @param instances the instances
     * @param options   the options
     * @throws Exception the exception
     */
    NaiveBayesWeka(Instances instances, String[] options) throws Exception {
        super();
        this.instances = instances;
        this.setOptions(options);
        // build classifier
        this.buildClassifier(instances);
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
}

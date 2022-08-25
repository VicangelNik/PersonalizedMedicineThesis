package personalizedmedicine.classifiers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.interfaces.IAppClassifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;

import java.util.Random;

@Slf4j
public class ZeroRWeka extends ZeroR implements IAppClassifier {

    private final Instances instances;

    /**
     * Instantiates a new zero R weka.
     *
     * @param instances the instances
     * @param options   the options
     * @throws Exception the exception
     */
    ZeroRWeka(Instances instances, String[] options) throws Exception {
        super();
        this.instances = instances;
        this.setOptions(options);
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

    public Instances getInstances() {
        return instances;
    }

}

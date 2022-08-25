package personalizedmedicine.interfaces;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public interface IClassifierSelection {

    /**
     * Select classifier.
     *
     * @param selection the selection
     * @param instances the instances
     * @param options   the options
     * @return the abstract classifier
     */
    AbstractClassifier selectClassifier(String selection, Instances instances, String[] options);

    /**
     * Cross validation action.
     *
     * @param clSelection the cl selection
     * @param classifier  the classifier
     * @param numFolds    the num folds
     * @param random      the random
     */
    void crossValidationAction(String clSelection, AbstractClassifier classifier, int numFolds, int random);
}

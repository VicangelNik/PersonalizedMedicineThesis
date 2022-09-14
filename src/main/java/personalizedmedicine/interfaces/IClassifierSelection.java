package personalizedmedicine.interfaces;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public interface IClassifierSelection {

    AbstractClassifier selectClassifier(String selection, Instances instances, String[] options);

    void crossValidationAction(String clSelection, AbstractClassifier classifier, int numFolds, int random);
}

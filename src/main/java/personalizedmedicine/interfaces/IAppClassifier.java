package personalizedmedicine.interfaces;

import java.util.Random;

public interface IAppClassifier {

    void crossValidationEvaluation(int numFolds, Random random);
}

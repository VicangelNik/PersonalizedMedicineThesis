package personalizedmedicine.interfaces;

import weka.core.Instances;


public interface IDimensionalityReduction {

    Instances dimReductionMethod(String[] options) throws Exception;
}

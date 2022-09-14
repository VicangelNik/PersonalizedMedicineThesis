package personalizedmedicine.interfaces;

import weka.core.Instances;

public interface IDimensionalityReductionSelection {

    Instances dimensionalityReductionSelector(String selection, Instances dataset, boolean debug, String[] options);
}

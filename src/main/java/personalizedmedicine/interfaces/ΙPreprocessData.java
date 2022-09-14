package personalizedmedicine.interfaces;

import weka.core.Instances;

public interface ΙPreprocessData {

    Instances removeFeaturesByType(int tagId) throws Exception;

    Instances removeFeature(String rangeList, boolean invert) throws Exception;

    Instances removeMissingValues() throws Exception;
}

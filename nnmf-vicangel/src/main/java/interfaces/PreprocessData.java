package interfaces;

import weka.core.Instances;

public interface PreprocessData {

	public Instances removeType(Instances data, int tagId) throws Exception;
}

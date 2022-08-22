package java.personalizedmedicine.interfaces;

import weka.core.Instances;

/**
 * The Interface PreprocessData.
 */
public interface ΙPreprocessData {

	/**
	 * Removes the features by type.
	 *
	 * @param tagId the tag id
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeFeaturesByType(int tagId) throws Exception;

	/**
	 * Removes the feature.
	 *
	 * @param rangeList the range list
	 * @param invert    the invert
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeFeature(String rangeList, boolean invert) throws Exception;

	/**
	 * Removes the missing values.
	 *
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeMissingValues() throws Exception;
}

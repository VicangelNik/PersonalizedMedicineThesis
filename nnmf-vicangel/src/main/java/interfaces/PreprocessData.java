package interfaces;

import weka.core.Instances;

/**
 * The Interface PreprocessData.
 */
public interface PreprocessData {

	/**
	 * Removes the features by type.
	 *
	 * @param data  the data
	 * @param tagId the tag id
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeFeaturesByType(Instances data, int tagId) throws Exception;

	/**
	 * Removes the feature.
	 *
	 * @param data      the data
	 * @param rangeList the range list
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeFeature(Instances data, String rangeList) throws Exception;

	/**
	 * Removes the missing values.
	 *
	 * @param data the data
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances removeMissingValues(Instances data) throws Exception;
}

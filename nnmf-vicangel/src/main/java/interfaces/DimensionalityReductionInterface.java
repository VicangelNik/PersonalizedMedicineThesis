package interfaces;

import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Interface DimensionalityReductionInterface.
 */
public interface DimensionalityReductionInterface {

	/**
	 * Pca.
	 *
	 * @param maxAttributeNames the max attribute names
	 * @param maxAttributes     the max attributes
	 * @param varianceCovered   the variance covered
	 * @return the instances
	 * @throws Exception the exception
	 */
	public Instances PCA(int maxAttributeNames, int maxAttributes, int varianceCovered) throws Exception;
}

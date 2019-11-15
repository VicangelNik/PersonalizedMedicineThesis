package dimensionality_reduction_methods;

import abstract_classes.DimensionalityReduction;
import interfaces.DimensionalityReductionInterface;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

// TODO: Auto-generated Javadoc
/**
 * The Class PrincipalComponentAnalysisWeka.
 */
public class PrincipalComponentAnalysisWeka extends DimensionalityReduction
		implements DimensionalityReductionInterface {

	/** The max attribute names. */
	int maxAttributeNames;

	/** The max attributes. */
	int maxAttributes;

	/** The variance covered. */
	int varianceCovered;

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.DimensionalityReductionInterface#PCA(int, int, int)
	 */
	@Override
	public Instances PCA(int maxAttributeNames, int maxAttributes, int varianceCovered) throws Exception {
		PrincipalComponents principalComponents = new PrincipalComponents();
		principalComponents.setInputFormat(this.getDataset());
		principalComponents.setDebug(this.isDebug());
		principalComponents.setMaximumAttributeNames(maxAttributeNames);
		principalComponents.setVarianceCovered(varianceCovered);
		principalComponents.setMaximumAttributes(maxAttributes);
		return Filter.useFilter(this.getDataset(), principalComponents);
	}

	/**
	 * Gets the max attribute names.
	 *
	 * @return the max attribute names
	 */
	public int getMaxAttributeNames() {
		return maxAttributeNames;
	}

	/**
	 * Sets the max attribute names.
	 *
	 * @param maxAttributeNames the new max attribute names
	 */
	public void setMaxAttributeNames(int maxAttributeNames) {
		this.maxAttributeNames = maxAttributeNames;
	}

	/**
	 * Gets the max attributes.
	 *
	 * @return the max attributes
	 */
	public int getMaxAttributes() {
		return maxAttributes;
	}

	/**
	 * Sets the max attributes.
	 *
	 * @param maxAttributes the new max attributes
	 */
	public void setMaxAttributes(int maxAttributes) {
		this.maxAttributes = maxAttributes;
	}

	/**
	 * Gets the variance covered.
	 *
	 * @return the variance covered
	 */
	public int getVarianceCovered() {
		return varianceCovered;
	}

	/**
	 * Sets the variance covered.
	 *
	 * @param varianceCovered the new variance covered
	 */
	public void setVarianceCovered(int varianceCovered) {
		this.varianceCovered = varianceCovered;
	}
}

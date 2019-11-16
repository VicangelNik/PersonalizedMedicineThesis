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
	private int maxAttributeNames;

	/** The max attributes. */
	private int maxAttributes;

	/** The variance covered. */
	private int varianceCovered;

	/** The center data. */
	private boolean centerData;
	
	/**
	 * Instantiates a new principal component analysis weka.
	 */
	public PrincipalComponentAnalysisWeka() {
		super();
	}
	
	/**
	 * Instantiates a new principal component analysis weka.
	 *
	 * @param maxAttributeNames the max attribute names
	 * @param maxAttributes     the max attributes
	 * @param varianceCovered   the variance covered
	 * @param centerData        the center data
	 */
	public PrincipalComponentAnalysisWeka(int maxAttributeNames, int maxAttributes, int varianceCovered,
			boolean centerData) {
		super();
		this.maxAttributeNames = maxAttributeNames;
		this.maxAttributes = maxAttributes;
		this.varianceCovered = varianceCovered;
		this.centerData = centerData;
	}
	
	/* (non-Javadoc)
	 * @see interfaces.DimensionalityReductionInterface#dimReductionMethod(java.lang.String[])
	 */
	@Override
	public Instances dimReductionMethod(String[] options) throws Exception {
		PrincipalComponents principalComponents = new PrincipalComponents();
		principalComponents.setOptions(options);
		principalComponents.setInputFormat(this.getDataset());
		principalComponents.setDebug(this.isDebug());
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

	/**
	 * Checks if is center data.
	 *
	 * @return true, if is center data
	 */
	public boolean isCenterData() {
		return centerData;
	}

	/**
	 * Sets the center data.
	 *
	 * @param centerData the new center data
	 */
	public void setCenterData(boolean centerData) {
		this.centerData = centerData;
	}
}

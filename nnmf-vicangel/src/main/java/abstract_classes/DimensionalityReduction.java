package abstract_classes;

import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Class DimensionalityReduction.
 */
public abstract class DimensionalityReduction {

	/** The dataset. */
	private Instances dataset;

	/** The debug. */
	private boolean debug;

	/**
	 * Gets the dataset.
	 *
	 * @return the dataset
	 */
	public Instances getDataset() {
		return dataset;
	}

	/**
	 * Sets the dataset.
	 *
	 * @param dataset the new dataset
	 */
	public void setDataset(Instances dataset) {
		this.dataset = dataset;
	}

	/**
	 * Checks if is debug.
	 *
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the debug.
	 *
	 * @param debug the new debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}

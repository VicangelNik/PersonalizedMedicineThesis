package tools;

/**
 * The Class ResultsOutputInfo.
 */
public class ResultsOutputInfo {

	/** The file name. */
	private String fileName;

	/** The display name. */
	private String displayName;

	/** The measure. */
	private double fMeasure;

	/** The options. */
	private String options;

	/**
	 * Instantiates a new results output info.
	 *
	 * @param fileName    the file name
	 * @param displayName the display name
	 * @param fMeasure    the f measure
	 * @param options     the options
	 */
	public ResultsOutputInfo(String fileName, String displayName, double fMeasure, String options) {
		this.fileName = fileName;
		this.displayName = displayName;
		this.fMeasure = fMeasure;
		this.options = options;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the f measure.
	 *
	 * @return the f measure
	 */
	public double getfMeasure() {
		return fMeasure;
	}

	/**
	 * Sets the f measure.
	 *
	 * @param fMeasure the new f measure
	 */
	public void setfMeasure(double fMeasure) {
		this.fMeasure = fMeasure;
	}

	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * Sets the options.
	 *
	 * @param options the new options
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return String.format("Test %s for dataset %s with options %s has F-Measure value %f", getDisplayName(),
				getFileName(), getOptions(), getfMeasure());
	}
}

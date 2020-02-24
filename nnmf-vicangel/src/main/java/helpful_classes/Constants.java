package helpful_classes;

/**
 * The Class Constants.
 */
public class Constants {

	/**
	 * Instantiates a new constants.
	 */
	private Constants() {
		throw new IllegalArgumentException("Utillity class should not be initialized");
	}

	/** The Constant SRC_TEST_RESOURCES_PATH. */
	public static final String SRC_TEST_RESOURCES_PATH = "src\\test\\resources\\";

	/** The Constant SRC_MAIN_RESOURCES_PATH. */
	public static final String SRC_MAIN_RESOURCES_PATH = "src\\main\\resources\\";

	/** The Constant C_WORK_CSV_FILE. */
	public static final String C_WORK_CSV_FILE = Constants.SRC_MAIN_RESOURCES_PATH + "patientAndControlData.csv";

	/** The Constant WEKA_FILES. */
	public static final String WEKA_FILES = "src\\test\\resources\\wekaFiles\\";

	/** The Constant loggerPath. */
	public static final String loggerPath = "C:\\applications\\thesis\\";

	/** The Constant MORMAL_TISSUE. */
	public static final String MORMAL_TISSUE = "NormalTissue";

	/** The Constant PRIMARY_TUMOR. */
	public static final String PRIMARY_TUMOR = "PrimaryTumor";

	/** The Constant NAIVE_BAYES. */
	// CLASSIFIERS
	public static final String NAIVE_BAYES = "naiveBayes";

	/** The Constant PCA. */
	// DIMENSIONALITY REDUCTION METHODS
	public static final String PCA = "pca";

	/** The Constant ZERO_R. */
	public static final String ZERO_R = "zeroR";
}

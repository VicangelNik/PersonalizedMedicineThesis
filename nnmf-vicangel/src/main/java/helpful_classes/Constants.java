package helpful_classes;

// TODO: Auto-generated Javadoc
/**
 * The Class Constants.
 */
/**
 * @author vic
 *
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

	// CLASSIFIERS
	/** The Constant NAIVE_BAYES. */
	public static final String NAIVE_BAYES = "naiveBayes";

	/** The Constant ZERO_R. */
	public static final String ZERO_R = "zeroR";

	/** The Constant JRIP. */
	public static final String JRIP = "jRip";

	/** The Constant PART. */
	public static final String PART = "part";

	/** The Constant IBK. */
	public static final String IBK = "ibk";

	// DIMENSIONALITY REDUCTION METHODS
	/** The Constant PCA. */
	public static final String PCA = "pca";

	/** The Constant EMPCA. */
	public static final String EMPCA = "empca";

	/** The Constant ISOMAP. */
	public static final String ISOMAP = "isomap";

	/** The Constant LLE. */
	public static final String LLE = "lle";

	/** The Constant AUTOENCODER_WEKA. */
	public static final String AUTOENCODER_WEKA = "mlpAutoencoder";

}

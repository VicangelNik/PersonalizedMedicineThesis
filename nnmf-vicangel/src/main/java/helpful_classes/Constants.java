package helpful_classes;

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

	/** The logger. */
	public static AppLogger logger = AppLogger.getInstance();

	// PATHS
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

	/** The Constant NAIVE_BAYES_UPDATABLE. */
	// CLASSIFIERS
	public static final String NAIVE_BAYES_UPDATABLE = "naiveBayesUpdatable";

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

	/** The Constant DEEPLEARNING4J. */
	public static final String DEEPLEARNING4J = "deepLearning4j";

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

	// FILE NAMES
	/** The Constant completeFileName. */
	public static final String completeFileName = Constants.SRC_MAIN_RESOURCES_PATH
			+ "PatientAndControlProcessedLevelTwo.arff";

	/** The meth file name. */
	public static final String methFileName = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset.arff";

	/** The mi RNA file name. */
	public static final String miRNAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "miRNADataset.arff";

	/** The m RNA file name. */
	public static final String mRNAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "mRNADataset.arff";

	/** The dataset 10 EMPCA file name. */
	public static final String dataset10EMPCAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "10empcaData.arff";

	/** The dataset 20 EMPCA file name. */
	public static final String dataset20EMPCAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "20empcaData.arff";

	/** The dataset 50 EMPCA file name. */
	public static final String dataset50EMPCAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "50empcaData.arff";

	/** The dataset 100 EMPCA file name. */
	public static final String dataset100EMPCAFileName = Constants.SRC_MAIN_RESOURCES_PATH + "100empcaData.arff";

	/** The Constant datasetIrisFileName. */
	public static final String datasetIrisFileName = Constants.WEKA_FILES + "iris.arff";

	/** The Constant datasetRealfileNames. */
	public static final String[] datasetRealfileNames = new String[] { completeFileName, methFileName, miRNAFileName,
			mRNAFileName };

	/** The Constant datasetEmpcafileNames. */
	public static final String[] datasetEmpcafileNames = new String[] { Constants.dataset10EMPCAFileName,
			Constants.dataset20EMPCAFileName, Constants.dataset50EMPCAFileName, Constants.dataset100EMPCAFileName };

	// CLASS NAMES
	/** The class name for reduced data. */
	public static final String classNameForReducedData = "class";

	/** The class real name. */
	public static final String classRealName = "SampleStatus";
}

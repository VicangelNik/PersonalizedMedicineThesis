package personalizedmedicine.helpful_classes;

public class Constants {

    private Constants() {
        throw new IllegalArgumentException("Utility class should not be initialized");
    }

    //public static AppLogger logger = AppLogger.getInstance();
    public static AppLogger logger = null;

    // PATHS
    public static final String SRC_TEST_RESOURCES_PATH = "src\\test\\resources\\";
    public static final String SRC_TEST_DATABASES_PATH = "databases\\";

    public static final String SRC_MAIN_RESOURCES_PATH = "src\\main\\resources\\";

    public static final String C_WORK_CSV_FILE = SRC_MAIN_RESOURCES_PATH + "patientAndControlData.csv";

    public static final String WEKA_FILES = "src\\test\\resources\\wekaFiles\\";

    // public static final String loggerPath = "C:\\applications\\thesis\\";
    public static String loggerPath = "D:\\Bioscience\\results\\Original Dataset\\JRIP\\log\\";

    public static final String MORMAL_TISSUE = "NormalTissue";

    public static final String PRIMARY_TUMOR = "PrimaryTumor";

    // CLASSIFIERS
    public static final String NAIVE_BAYES_UPDATABLE = "naiveBayesUpdatable";

    public static final String NAIVE_BAYES = "naiveBayes";

    public static final String ZERO_R = "zeroR";

    public static final String JRIP = "jRip";

    public static final String PART = "part";

    public static final String IBK = "ibk";
    public static final String DEEPLEARNING4J = "deepLearning4j";

    // DIMENSIONALITY REDUCTION METHODS
    public static final String PCA = "pca";

    public static final String EMPCA = "empca";

    public static final String ISOMAP = "isomap";

    public static final String LLE = "lle";

    public static final String AUTOENCODER_WEKA = "mlpAutoencoder";

    // FILE NAMES
    /**
     * The Constant completeFileName.
     */
    public static final String completeFileName = SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff";
    public static final String methFileName = SRC_MAIN_RESOURCES_PATH + "methDataset.arff";
    public static final String miRNAFileName = SRC_MAIN_RESOURCES_PATH + "miRNADataset.arff";
    public static final String mRNAFileName = SRC_MAIN_RESOURCES_PATH + "mRNADataset.arff";
    public static final String dataset10EMPCAFileName =
            SRC_TEST_RESOURCES_PATH + SRC_TEST_DATABASES_PATH + EMPCA + "\\" + "10empcaData.arff";
    public static final String dataset20EMPCAFileName =
            SRC_TEST_RESOURCES_PATH + SRC_TEST_DATABASES_PATH + EMPCA + "\\" + "20empcaData.arff";
    public static final String dataset50EMPCAFileName =
            SRC_TEST_RESOURCES_PATH + SRC_TEST_DATABASES_PATH + EMPCA + "\\" + "50empcaData.arff";
    public static final String dataset100EMPCAFileName =
            SRC_TEST_RESOURCES_PATH + SRC_TEST_DATABASES_PATH + EMPCA + "\\" + "100empcaData.arff";

    public static final String datasetIrisFileName = WEKA_FILES + "iris.arff";

    public static final String[] datasetRealfileNames = new String[]{completeFileName, methFileName, miRNAFileName, mRNAFileName};

    public static final String[] datasetEmpcafileNames = new String[]{dataset10EMPCAFileName, dataset20EMPCAFileName, dataset50EMPCAFileName, dataset100EMPCAFileName};

    public static final String dataset10IsomapCsvFileName = SRC_MAIN_RESOURCES_PATH + "Iso10.csv";
    public static final String dataset20IsomapCsvFileName = SRC_MAIN_RESOURCES_PATH + "Iso20.csv";
    public static final String dataset50IsomapCsvFileName = SRC_MAIN_RESOURCES_PATH + "Iso50.csv";
    public static final String dataset100IsomapCsvFileName = SRC_MAIN_RESOURCES_PATH + "Iso100.csv";

    // CLASS NAMES

    public static final String classNameForReducedData = "class";
    public static final String classRealName = "SampleStatus";
}
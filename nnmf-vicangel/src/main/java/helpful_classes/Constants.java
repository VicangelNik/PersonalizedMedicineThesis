package helpful_classes;

public class Constants {

	private Constants() {
		throw new IllegalArgumentException("Utillity class should not be initialized");
	}

	/** The Constant RELATIVE_PATH. */
	public static final String SRC_TEST_RESOURCES_PATH = "src\\test\\resources\\";

	public static final String SRC_MAIN_RESOURCES_PATH = "src\\main\\resources\\";
	
	public static final String C_WORK_CSV_FILE = Constants.SRC_MAIN_RESOURCES_PATH + "allTogether.csv";
}

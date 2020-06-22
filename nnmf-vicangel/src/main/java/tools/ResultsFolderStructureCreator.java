package tools;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The Class ResultsFolderStructureCreator creates all the folders in the system
 * for saving the logged results.
 */
public class ResultsFolderStructureCreator {

	/** The Constant part2FolderName. */
	private final static String part2FolderName = " Dataset";

	/** The Constant logFolderName. */
	private final static String logFolderName = "log";

	/** The Constant mainFolders. */
	private final static String[] mainFolders = new String[] { "Original", "Methylation", "mRNA", "miRNA",
			"Original_EMPCA", "Original_Isomap", "Original_AutoEncoder" };

	/** The result path. */
	private static String resultPath = "D:\\Bioscience\\results";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		File disk = new File("D:\\");
		if (!disk.exists()) {
			// Replace D with C
			resultPath = resultPath.replace('D', 'C');
		}
		// create paths
		for (String folder : mainFolders) {
			Path path = Paths.get(resultPath, folder + part2FolderName, logFolderName);
			File file = path.toFile();
			if (!file.exists() && !file.mkdirs()) {
				System.out.println("Directory " + path.toString() + " could not be created.");
			}
		}
	}
}

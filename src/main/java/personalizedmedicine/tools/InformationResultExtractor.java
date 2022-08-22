package java.personalizedmedicine.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import java.personalizedmedicine.utilpackage.Utils;

/**
 * The Class InformationResultExtractor.
 */
public class InformationResultExtractor {

	/** The Constant searchBy. */
	private static final String searchBy = "Weighted Avg.";

	/** The Constant savefile. */
	private static final String savefile = "INFO: SAVE FILE NAME: ";

	/** The Constant saveTestInfo. */
	private static final String saveTestInfo = "INFO: SAVE TEST INFO NAME: ";

	/** The Constant optionsPreText. */
	private static final String optionsPreText = "INFO: OPTIONS: ";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		writeResultsInfoToFile();
	}

	/**
	 * Write results info to file. Finds all log files from all files included in
	 * the given directory (all the files structure is created by
	 * ResultsFolderStructureCreator) and for each log file creates a txt file that
	 * writes the formatted results.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeResultsInfoToFile() throws IOException {

		try (Stream<Path> stream = Files.walk(Paths.get(ResultsFolderStructureCreator.resultPath))) {
			// get all log files from the directory
			stream.filter(file -> file.toString().endsWith(".log")).forEach(fullPath -> {
				// get results
				List<ResultsOutputInfo> results = resultExtraction(fullPath.toFile().getAbsolutePath());
				// get file without extension
				String fileName = com.google.common.io.Files.getNameWithoutExtension(fullPath.toString());
				// get file's 2 directories up from log and create the full path for the result
				// file
				Path path = Paths.get(fullPath.getParent().getParent().toString(), fileName + Utils.TXT_SUFFIX);
				// gather all results of file, line by line in a string
				StringBuilder builder = new StringBuilder();
				results.forEach(result -> {
					builder.append(result.toString());
					builder.append(System.lineSeparator());
				});
				// write to file
				try {
					Files.write(path, builder.toString().getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * Result extraction.
	 *
	 * @param resultFile the result file
	 * @return the list
	 */
	private static List<ResultsOutputInfo> resultExtraction(String resultFile) {
		File file = new File(resultFile);
		List<ResultsOutputInfo> results = new ArrayList<>();
		String fileName = "";
		String displayName = "";
		String options = "";
		Double fMeasure = 0.;
		try (Scanner scanner = new Scanner(file)) {
			// now read the file line by line...
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains(savefile)) {
					fileName = line.replace(savefile, "");
				} else if (line.contains(saveTestInfo)) {
					displayName = line.replace(saveTestInfo, "");
				} else if (line.contains(optionsPreText)) {
					options = line.replace(optionsPreText, "");
				} else if (line.contains(searchBy)) {
					String fMeasureStr = line.split("\\s+")[6];
					if (fMeasureStr.equals("?")) {
						fMeasure = 0.;
					} else {
						fMeasure = Double.parseDouble(fMeasureStr);
					}
					results.add(new ResultsOutputInfo(fileName, displayName, fMeasure, options));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// sorts results from biggest to lowest f-measure
		results.sort(
				Comparator.comparing(ResultsOutputInfo::getfMeasure, Comparator.nullsLast(Comparator.reverseOrder())));
		results.forEach(x -> System.out.println(x.toString()));
		Stream.generate(() -> "-").limit(30).forEach(System.out::print);
		return results;
	}
}

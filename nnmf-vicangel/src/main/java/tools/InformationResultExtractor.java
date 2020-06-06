package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import helpful_classes.Constants;

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
	 */
	public static void main(String[] args) {
		String resultFile = Constants.loggerPath + "project.log";

		File file = new File(resultFile);
		List<ResultsOutputInfo> results = new ArrayList<>();
		String fileName = "";
		String displayName = "";
		String options = "";
		double fMeasure = 0;
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
					fMeasure = Double.parseDouble(line.split("\\s+")[6]);
					results.add(new ResultsOutputInfo(fileName, displayName, fMeasure, options));
				}
			}
			results.sort(Comparator.comparing(ResultsOutputInfo::getfMeasure));
			results.forEach(x -> System.out.println(x.toString()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

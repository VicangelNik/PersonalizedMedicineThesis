package java.personalizedmedicine.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * The Class LatexTablesCreatorFromResults. This tool class creates the result
 * tables that will be placed in overleaf.
 */
public class LatexTablesCreatorFromResults {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		try (Stream<Path> stream = Files.walk(Paths.get(ResultsFolderStructureCreator.resultPath))) {
			// get all txt files from the directory and not ZeroR(saved as txt)
			// txt files in this directory contain all the useful information of the results
			stream.filter(file -> file.toString().endsWith(".txt") && !file.toString().contains("ZeroR"))
					.forEach(fullPath -> {
						System.out.println(fullPath);
						StringJoiner joiner = new StringJoiner("\n");
						joiner.add("\\begin{table}[ht!]");
						joiner.add("\t\\centering");
						joiner.add("\t\\begin{tabular}{|c|c|}");
						joiner.add("\\hline");
						joiner.add("Configurations & Weighted F-measure \\\\ [0.5ex] ");
						joiner.add("\\hline\\");
						addValuesToTable(fullPath, joiner);
						String captionHeader = fullPath.getParent().getParent().getFileName().toString();
						// some names have the format: name_dataset
						captionHeader = captionHeader.replace("_", " ");
						joiner.add("\t\\end{tabular}");
						joiner.add("\\caption{Results for " + captionHeader + " }");
						joiner.add("\\label{tbl:Results for " + captionHeader + "}");
						joiner.add("\\end{table}");
						System.out.println(joiner.toString());
						Stream.generate(() -> "-").limit(30).forEach(System.out::print);
						System.out.println("");
					});
		}
	}

	/**
	 * Adds the values to table.
	 *
	 * @param fullPath the full path
	 * @param joiner   the joiner
	 */
	private static void addValuesToTable(Path fullPath, StringJoiner joiner) {
		String strLine = "";
		double fMaxValue = 0;
		// LineNumberReader is used to better iterate over the lines in files
		try (LineNumberReader reader = new LineNumberReader(
				new InputStreamReader(new FileInputStream(fullPath.toString()), StandardCharsets.UTF_8))) {
			while (((strLine = reader.readLine()) != null) && reader.getLineNumber() <= 5) {
				// remember the format of each line (example): "Test Configuration_155 for
				// dataset miRNADataset.arff with options [-O, 4, -F, 9, -N, 3.0] has F-Measure
				// value 0.962000"
				String options = strLine
						.substring(strLine.indexOf("options") + "options".length(), strLine.indexOf("has")).trim();
				String fmeasure = strLine.substring(strLine.indexOf("F-Measure value") + "F-Measure value".length())
						.trim();
				// we remove the [] in options and mark with bold the best values.
				String bestValueRow = options.replaceAll("[\\]\\[]", "") + " & " + "\\textbf{" + fmeasure + "} \\\\";
				if (reader.getLineNumber() == 1) {
					fMaxValue = Double.parseDouble(fmeasure);
					joiner.add(bestValueRow);
				} else {
					if (fMaxValue == Double.parseDouble(fmeasure)) {
						joiner.add(bestValueRow);
					} else {
						joiner.add(options.replaceAll("[\\]\\[]", "") + " & " + fmeasure + " \\\\");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

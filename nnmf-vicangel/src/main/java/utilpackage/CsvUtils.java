/*
 * 
 */
package utilpackage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

public final class CsvUtils {

	private CsvUtils() {
		throw new IllegalArgumentException("utillity class");
	}

	public static final String TAB_SEPARATOR = "\t";

	/**
	 * Reads the CSV file and returns list of lists. Each list contains the data of
	 * each case.
	 *
	 * @param fileName the file path.
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<List<String>> readCSVFile(String fileName) throws IOException {
		List<List<String>> records = new ArrayList<>();
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName));) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				records.add(Arrays.asList(values));
			}
		}
		return records;
	}

	/**
	 * Reads the csv file and returns the list of lists. Each list contains the
	 * feature with its data.
	 *
	 * @param fileName
	 * @param dimensionsSize
	 * @return
	 * @throws IOException
	 */
	public static List<List<String>> readCSVFileColumnWise(String fileName, int dimensionsSize) throws IOException {
		List<List<String>> records = Utils.instantiateListOfStringLists(dimensionsSize);
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName));) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				String[] lineString = Arrays.toString(values).split(TAB_SEPARATOR);
				for (int i = 0; i < dimensionsSize; i++) {
					// we ignore the first cell of its line because it contains the case ids. In the
					// line of dimensions contains the data keyword.
					records.get(i).add(lineString[i + 1]);
				}
			}
		}
		return records;
	}

}

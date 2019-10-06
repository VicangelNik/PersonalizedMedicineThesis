import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import helpful_classes.Constants;

public class TestMakeDummyCsvTestCase {

	private static final File csvFile = new File(Constants.SRC_TEST_RESOURCES_PATH + "testCsv.csv");

	@Test
	public void FinsSolution() {
		List<String[]> dataLines = new ArrayList<>();
		String[] features = new String[10];
		String[] data = new String[10];
		for (int i = 0; i < 10; i++) {
			features[i] = "feature" + i;
			data[i] = Integer.toString(i);
		}
		dataLines.add(features);
		dataLines.add(data);
		File csvOutputFile = csvFile;
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream().map(this::convertToCSV).forEach(pw::println);
			//Path expectedFile = Paths.get(Constants.SRC_TEST_RESOURCES_PATH + "csv\\" + "testCsv.csv");
			//Assert.assertTrue("Files should be same", FileUtils.contentEquals(csvFile, expectedFile.toFile()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String convertToCSV(String[] data) {
		return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
	}

	public String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}
}

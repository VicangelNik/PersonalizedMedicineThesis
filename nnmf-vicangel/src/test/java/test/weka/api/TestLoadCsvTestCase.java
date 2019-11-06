/*
 *
 */
package test.weka.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.opencsv.CSVReader;

import helpful_classes.Constants;
import helpful_classes.EnumSeparators;

// TODO: Auto-generated Javadoc
/**
 * The Class TestLoadCsvTestCase.
 */
public class TestLoadCsvTestCase {

//	/**
//	 * Test load christina's csv.
//	 */
//	@Test
//	public void testLoadCsv() {
//		final File csvFile = new File(Constants.C_WORK_CSV_FILE);
//		try {
//			LoadCsv loadCsv = new LoadCsv(csvFile, EnumSeparators.TAB.getSeparator());
//			Assert.assertEquals(csvFile, loadCsv.getFile());
//			Assert.fail("test fails");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Assert.assertTrue(e.getMessage() != null);
//		}
//	}
//
//	/**
//	 * Test make csv valid.
//	 */
//	@Test
//	public void testMakeCsvValid() {
//		String newCsvName = Constants.SRC_MAIN_RESOURCES_PATH + "bladderCancer.csv";
//		try (CSVReader csvReader = new CSVReader(new FileReader(Constants.C_WORK_CSV_FILE));) {
//			String[] values = null;
//			try (BufferedWriter bw = new BufferedWriter(new FileWriter(newCsvName))) {
//				while ((values = csvReader.readNext()) != null) {
//					// the csv file had unnecessary commas, spaces, tabs and brackets and we make it
//					// to be seperated only by tabs.
//					String line = Arrays.toString(values).replace(" ", EnumSeparators.TAB.getSeparator())
//							.replace(",", "").replaceAll("[\\t]{1}(])+", "");
//					bw.write(line);
//					bw.write("\n");
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
//		// ASSERT
//		File file = new File(newCsvName);
//		Assert.assertTrue("The file should exist", file.exists());
//		Assert.assertTrue("The file should be read", file.canRead());
//	}

	@Test
	public void testMakeCsvValid() {
		String newCsvName = Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessed.csv";
		try (CSVReader csvReader = new CSVReader(new FileReader(Constants.C_WORK_CSV_FILE));) {
			String[] values = null;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(newCsvName))) {
				String line1 = Arrays.toString(csvReader.readNext()).replaceAll("[\\[\\]]", "");
				bw.write(line1);
				bw.write(System.lineSeparator());
				while ((values = csvReader.readNext()) != null) {
					// the csv file had unnecessary commas, spaces, tabs and brackets and we make it
					// to be seperated only by tabs.
					String line = Arrays.toString(values).replaceAll("[\\[\\]]", "").replaceAll("na", "NA");
					List<String> list = new ArrayList<>(Arrays.asList(line.split(EnumSeparators.TAB.getSeparator())));
					bw.write(modifyValues(list));
					bw.write(System.lineSeparator());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		// ASSERT
		File file = new File(newCsvName);
		Assert.assertTrue("The file should exist", file.exists());
		Assert.assertTrue("The file should be read", file.canRead());
	}

//
//	/**
//	 * Test load csv 2.
//	 */
//	@Test
//	public void testLoadValidCsv() {
//		final File csvFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "bladderCancer.csv");
//		try {
//			LoadCsv loadCsv = new LoadCsv(csvFile, EnumSeparators.TAB.getSeparator());
//			Assert.assertEquals(csvFile, loadCsv.getFile());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
//	}

	private static String modifyValues(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size() - 1; i++) {
			String value = list.get(i);
			builder.append(value.trim() + EnumSeparators.TAB.getSeparator());
		}
		String lastValue = list.get(list.size() - 1);
		builder.append(lastValue.trim());
		return builder.toString();
	}
}

package test.weka.api;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.opencsv.CSVReader;

import utilpackage.CsvUtils;
import weka.api.library.LoadCsv;

public class TestLoadCsvTestCase {

	private static final File csvFile = new File(CsvUtils.C_WORK_CSV_FILE);

	@Test
	public void testLoadCsv() {
		try {
			LoadCsv loadCsv = new LoadCsv(csvFile);
			// Assert.assertEquals(csvFile, loadCsv.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void FinsSolution() {
		try (CSVReader csvReader = new CSVReader(new FileReader(csvFile));) {
			String[] features = Arrays.toString(csvReader.readNext()).split(CsvUtils.CSV_SEPERATOR);
			
			String[] dataline = Arrays.toString(csvReader.readNext()).split(CsvUtils.CSV_SEPERATOR);
			System.out.println(dataline[1]);
			System.out.println(features.length);
			System.out.println(dataline.length);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

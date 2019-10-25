/*
 *
 */
package test.weka.api;

// TODO: Auto-generated Javadoc
/**
 * The Class TestLoadCsvTestCase.
 */
public class TestLoadCsvTestCase {

	/**
	 * Test load dummy csv.
	 */
//	@Test
//	public void testLoadDummyCsv() {
//		final File csvFile = new File(Constants.SRC_TEST_RESOURCES_PATH + "csv//" + "testCsv.csv");
//		try {
//			LoadCsv loadCsv = new LoadCsv(csvFile, ",");
//			Assert.assertEquals(csvFile, loadCsv.getFile());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Assert.fail(e.getMessage());
//		}
//	}

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
}

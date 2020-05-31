package test.weka.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.opencsv.CSVReader;

import helpful_classes.AppLogger;
import helpful_classes.Constants;
import helpful_classes.EnumSeparators;
import utilpackage.LatexUtils;
import utilpackage.WekaUtils;
import weka.api.library.PreprocessData;
import weka.api.library.WekaFileConverter;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
 * The Class TestPreprocess.
 */
public class TestPreprocess {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/** The Constant className. */
	private static final String className = "SampleStatus";

	/**
	 * Test preprocess level one.
	 */
	@Test
	public void testPreprocessLevelOne() {
		// MAKE CSV VALID
		String newCsvName = Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.csv";
		try (CSVReader csvReader = new CSVReader(new FileReader(Constants.C_WORK_CSV_FILE));) {
			String[] values = null;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(newCsvName))) {
				String line1 = Arrays.toString(csvReader.readNext()).replaceAll("[\\[\\]]", "");
				bw.write(line1);
				bw.write(System.lineSeparator());
				while ((values = csvReader.readNext()) != null) {
					// the csv file had unnecessary commas, spaces, tabs and brackets and we make it
					// to be seperated only by tabs.
					String line = Arrays.toString(values).replaceAll("[\\[\\]]", "").replaceAll("na", "NA")
							.replaceAll("--", "NA");
					List<String> list = new ArrayList<>(Arrays.asList(line.split(EnumSeparators.TAB.getSeparator())));
					bw.write(modifyValues(list));
					bw.write(System.lineSeparator());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		// ASSERT
		File file = new File(newCsvName);
		Assert.assertTrue("The file should exist", file.exists());
		Assert.assertTrue("The file should be read", file.canRead());
	}

	/**
	 * Test preprocess level 2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testPreprocessLevel2() throws Exception {
		// TEST CONVERTER TEST CASE MUST BE RAN BEFORE THIS TO RUN.
		// REMOVE INVALID ATTRIBUTES
		File arffFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.arff");
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Assert.assertTrue("The file should exists", arffFile.exists());
		Assert.assertTrue("The file should be readable.", arffFile.canRead());
		Instances originalDataset = WekaUtils.getOriginalData(arffFile, className);
		Assert.assertEquals("The excpected class should be: ", 73664, originalDataset.classIndex());
		Assert.assertTrue("The arf file should have string attributes", originalDataset.checkForStringAttributes());
		PreprocessData preprocessData = new PreprocessData(originalDataset);
		Instances data = originalDataset;
		// Attributes of String Type contain only NA. Also, they are not accepted by
		// NaiveBayes.
		if (data.checkForStringAttributes()) {
			// there is no need to call always this method
			// stringAttributesInfo(data);
			data = preprocessData.removeFeaturesByType(Attribute.STRING);
		}
		// DATA attribute has no information and we remove it. Vital_status and
		// Days_To_Death attribute are very connected with SampleStatus attribute so are
		// no informative. We could do classification with each of the two.
		// ASSERTS
		Assert.assertTrue("Attribute Vital_status should exist in dataset", data.attribute("Vital_status") != null);
		Assert.assertTrue("Attribute Days_To_Death should exist in dataset", data.attribute("Days_To_Death") != null);
		Assert.assertTrue("Attribute DATA should exist in dataset", data.attribute("DATA") != null);
		// CODE
		int vitalStatusIndex = data.attribute("Vital_status").index() + 1;
		int daysToDeathIndex = data.attribute("Days_To_Death").index() + 1;
		String rangeList = "First," + vitalStatusIndex + "," + daysToDeathIndex;
		preprocessData.setDataset(data);
		data = preprocessData.removeFeature(rangeList, false);
		// Removes the missing values. Replaces all missing values for nominal and
		// numeric attributes in a dataset with the modes and means from the training
		// data. The class attribute is skipped by default.
		preprocessData.setDataset(data);
		data = preprocessData.removeMissingValues();
		// ASSERTS
		Assert.assertFalse("Attribute DATA should not exist in dataset", data.attribute("DATA") != null);
		Assert.assertFalse("Attribute Vital_status should not exist in dataset",
				data.attribute("Vital_status") != null);
		Assert.assertFalse("Attribute Days_To_Death should not exist in dataset",
				data.attribute("Days_To_Death") != null);
		// ArffSaver
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		wekaFileConverterImpl.arffSaver(data, level2File.getAbsolutePath());
		// ASSERTS
		Assert.assertEquals("Number of attributes should be 72122", 72122, data.numAttributes());
		Assert.assertEquals("Number of instances should be 335", 335, data.numInstances());
		Assert.assertEquals("Class index should be 72121", 72121, data.classIndex());
		Assert.assertEquals("Class name should be SampleStatus", className, data.attribute(data.classIndex()).name());
		Assert.assertEquals("Number of classes should be 2", 2, data.numClasses());
		// there is no need to call always this method
		// LatexUtils.createDataInLatexFormat(data);
	}

	/**
	 * Modify values.
	 *
	 * @param list the list
	 * @return the string
	 */
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

	/**
	 * Test show data information.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testShowDataInformation() throws IOException {
		showDataInformation();
	}

	/**
	 * Show data information.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void showDataInformation() throws IOException {
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		logger.getLogger().log(Level.INFO, "{0}", "Number of classes: " + originalDataset.numClasses());
		logger.getLogger().log(Level.INFO, "{0}", "Number of features" + originalDataset.numAttributes());
		logger.getLogger().log(Level.INFO, "{0}", "Number of instances" + originalDataset.numInstances());
		logger.getLogger().log(Level.INFO, "{0}", "Class Index: " + originalDataset.classIndex());
		logger.getLogger().log(Level.INFO, "{0}",
				"Class name" + originalDataset.attribute(originalDataset.classIndex()).name());
		logger.getLogger().log(Level.INFO, "{0}",
				"Attribute Class Index: " + originalDataset.attributeStats(originalDataset.classIndex()));
		logger.getLogger().log(Level.INFO, "{0}", originalDataset.toSummaryString());
	}

	/**
	 * Separates the data set into the 3 different molecular levels.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSeparateMolecularLevels() throws Exception {
		String mRnaPath = Constants.SRC_MAIN_RESOURCES_PATH + "mRNAdataset" + WekaUtils.WEKA_SUFFIX;
		String miRnaPath = Constants.SRC_MAIN_RESOURCES_PATH + "miRNAdataset" + WekaUtils.WEKA_SUFFIX;
		String methPath = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset" + WekaUtils.WEKA_SUFFIX;
		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
		// ASSERTS
		Assert.assertEquals("Number of attributes should be 72122", 72122, originalDataset.numAttributes());
		Assert.assertEquals("Number of instances should be 335", 335, originalDataset.numInstances());
		Assert.assertEquals("Class index should be 72121", 72121, originalDataset.classIndex());
		Assert.assertEquals("Class name should be SampleStatus", className,
				originalDataset.attribute(originalDataset.classIndex()).name());
		Assert.assertEquals("Number of classes should be 2", 2, originalDataset.numClasses());
		List<Integer> mRNAs = new ArrayList<>();
		List<Integer> miRNAs = new ArrayList<>();
		List<Integer> meth = new ArrayList<>();
		getMolecularLists(originalDataset, mRNAs, miRNAs, meth, originalDataset.classIndex());
		//
		String m1 = mRNAs.stream().map(val -> Integer.toString(val + 1)).collect(Collectors.joining(",", "", ""));
		String m2 = miRNAs.stream().map(val -> Integer.toString(val + 1)).collect(Collectors.joining(",", "", ""));
		String m3 = meth.stream().map(val -> Integer.toString(val + 1)).collect(Collectors.joining(",", "", ""));
		// Remove attributes
		PreprocessData preprocessData = new PreprocessData(originalDataset);
		Instances mRnaData = preprocessData.removeFeature(m1, true);
		Instances miRnaData = preprocessData.removeFeature(m2, true);
		Instances methData = preprocessData.removeFeature(m3, true);
		// set Relation names
		mRnaData.setRelationName("mRnaData");
		miRnaData.setRelationName("miRnaData");
		methData.setRelationName("MethylationData");
		// Get Files
		File mRNAFile = new File(mRnaPath);
		File miRNAFile = new File(miRnaPath);
		File methFile = new File(methPath);
		// Save Data sets
		WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
		wekaFileConverterImpl.arffSaver(mRnaData, mRNAFile.getAbsolutePath());
		wekaFileConverterImpl.arffSaver(miRnaData, miRNAFile.getAbsolutePath());
		wekaFileConverterImpl.arffSaver(methData, methFile.getAbsolutePath());
		// get DATA
		Instances mRNADataset = WekaUtils.getOriginalData(mRNAFile, className);
		Instances miRNADataset = WekaUtils.getOriginalData(miRNAFile, className);
		Instances methDataset = WekaUtils.getOriginalData(methFile, className);
		// -2 because we add the class in each sub data set
		Assert.assertEquals("The sum of the 3 datasets should be equal to the original",
				mRNADataset.numAttributes() + miRNADataset.numAttributes() + methDataset.numAttributes() - 2,
				originalDataset.numAttributes());
		Assert.assertEquals("Number of instances should be the same",
				mRNADataset.numInstances() + miRNADataset.numInstances() + methDataset.numInstances(),
				originalDataset.numInstances() * 3);
	}

	/**
	 * Gets indexes of the features according to their molecular level. Adds also
	 * the class index to the lists.
	 *
	 * @param originalDataset the original dataset
	 * @param mRNAs           the m RN as
	 * @param miRNAs          the mi RN as
	 * @param meth            the meth
	 * @param classIndex      the class index
	 * @return the molecular lists
	 */
	private void getMolecularLists(Instances originalDataset, List<Integer> mRNAs, List<Integer> miRNAs,
			List<Integer> meth, int classIndex) {
		// we know that features starting with "m_" are mRNA and features starting with
		// hsa-mir, hsa-let are miRNA and the rest are Methylation
		Enumeration<Attribute> attEnumeration = originalDataset.enumerateAttributes();
		while (attEnumeration.hasMoreElements()) {
			Attribute attribute = attEnumeration.nextElement();
			String name = attribute.name();
			int attIndex = attribute.index();
			if (name.startsWith("m_")) {
				mRNAs.add(attIndex);
			} else if (name.startsWith("hsa-mir") || name.startsWith("hsa-let")) {
				miRNAs.add(attIndex);
			} else {
				meth.add(attIndex);
			}
		}
		// add class index
		mRNAs.add(classIndex);
		miRNAs.add(classIndex);
		meth.add(classIndex);
	}

	/**
	 * String attributes info.
	 *
	 * @param data the data
	 */
	@SuppressWarnings("unused")
	private static void stringAttributesInfo(Instances data) {
		Assert.assertEquals("Number of instances", 335, data.numInstances());
		Assert.assertEquals("Number of attributes", 73665, data.numAttributes());
		int count = 0;
		Enumeration<Attribute> attEnumeration = data.enumerateAttributes();
		StringBuilder sbLatex = new StringBuilder();
		while (attEnumeration.hasMoreElements()) {
			Attribute attribute = attEnumeration.nextElement();
			if (attribute.type() == Attribute.STRING) {
				count++;
				LatexUtils.latexTableForFeatures(sbLatex, attribute.name(), count);
				AttributeStats stats = data.attributeStats(attribute.index());
				Assert.assertEquals("The distinct values", 0, stats.distinctCount);
				Assert.assertEquals("The missing values", 335, stats.missingCount);
				Assert.assertEquals("The unique values", 0, stats.uniqueCount);
			}
		}
		Assert.assertEquals("Number of string attributes", 1540, count);
		// add the last 3 features which are removed and are not string
		sbLatex.append("DATA").append(" & ").append("Vital\\_status").append(" & ").append("Days\\_To\\_Death")
				.append(" & ").append(" & ").append(" \\\\").append(System.lineSeparator());
		logger.getLogger().log(Level.INFO, "{0}", "Invalid Features: " + sbLatex.toString());
	}
}

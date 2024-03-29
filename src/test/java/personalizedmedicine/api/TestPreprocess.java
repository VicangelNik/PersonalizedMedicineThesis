package personalizedmedicine.api;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.helpful_classes.EnumSeparators;
import personalizedmedicine.utilpackage.LatexUtils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.PreprocessData;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static personalizedmedicine.helpful_classes.Constants.classRealName;


@Slf4j
class TestPreprocess {

    @Test
    void testPreprocessLevelOne() {
        // MAKE CSV VALID
        String newCsvName = Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.csv";
        try (CSVReader csvReader = new CSVReader(new FileReader(Constants.C_WORK_CSV_FILE))) {
            String[] values = null;
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(newCsvName))) {
                String line1 = Arrays.toString(csvReader.readNext()).replaceAll("[\\[\\]]", "");
                bw.write(line1);
                bw.write(System.lineSeparator());
                while ((values = csvReader.readNext()) != null) {
                    // the csv file had unnecessary commas, spaces, tabs and brackets and we make it
                    // to be seperated only by tabs.
                    String line = Arrays.toString(values)
                                        .replaceAll("[\\[\\]]", "")
                                        .replaceAll("na", "NA")
                                        .replaceAll("--", "NA");
                    List<String> list = new ArrayList<>(Arrays.asList(line.split(EnumSeparators.TAB.getSeparator())));
                    bw.write(modifyValues(list));
                    bw.write(System.lineSeparator());
                }
            }
        } catch (IOException | CsvValidationException e) {
            Assertions.fail(e);
        }
        // ASSERT
        File file = new File(newCsvName);
        Assertions.assertTrue(file.exists(), "The file should exist");
        Assertions.assertTrue(file.canRead(), "The file should be read");
    }

    @Test
    void testPreprocessLevel2() throws Exception {
        // TEST CONVERTER TEST CASE MUST BE RAN BEFORE THIS TO RUN.
        // REMOVE INVALID ATTRIBUTES
        File arffFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelOne.arff");
        File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
        Assertions.assertTrue(arffFile.exists(), "The file should exists");
        Assertions.assertTrue(arffFile.canRead(), "The file should be readable.");
        Instances originalDataset = WekaUtils.getOriginalData(arffFile, classRealName);
        Assertions.assertEquals(73664, originalDataset.classIndex(), "The expected class should be: ");
        Assertions.assertTrue(originalDataset.checkForStringAttributes(), "The arf file should have string attributes");
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
        Assertions.assertNotNull(data.attribute("Vital_status"), "Attribute Vital_status should exist in dataset");
        Assertions.assertNotNull(data.attribute("Days_To_Death"), "Attribute Days_To_Death should exist in dataset");
        Assertions.assertNotNull(data.attribute("DATA"), "Attribute DATA should exist in dataset");
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
        Assertions.assertNull(data.attribute("DATA"), "Attribute DATA should not exist in dataset");
        Assertions.assertNull(data.attribute("Vital_status"), "Attribute Vital_status should not exist in dataset");
        Assertions.assertNull(data.attribute("Days_To_Death"), "Attribute Days_To_Death should not exist in dataset");
        // ArffSaver
        WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
        wekaFileConverterImpl.arffSaver(data, level2File.getAbsolutePath());
        // ASSERTS
        Assertions.assertEquals(72122, data.numAttributes(), "Number of attributes should be 72122");
        Assertions.assertEquals(335, data.numInstances(), "Number of instances should be 335");
        Assertions.assertEquals(72121, data.classIndex(), "Class index should be 72121");
        Assertions.assertEquals(classRealName, data.attribute(data.classIndex()).name(),
                                "Class name should be SampleStatus");
        Assertions.assertEquals(2, data.numClasses(), "Number of classes should be 2");
        // there is no need to call always this method
        // LatexUtils.createDataInLatexFormat(data);
    }

    private static String modifyValues(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            String value = list.get(i);
            builder.append(value.trim()).append(EnumSeparators.TAB.getSeparator());
        }
        String lastValue = list.get(list.size() - 1);
        builder.append(lastValue.trim());
        return builder.toString();
    }

    @Test
    void testShowDataInformation() throws IOException {
        showDataInformation();
    }

    public void showDataInformation() throws IOException {
        File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
        Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        log.info("Number of classes: " + originalDataset.numClasses());
        log.info("Number of features" + originalDataset.numAttributes());
        log.info("Number of instances" + originalDataset.numInstances());
        log.info("Class Index: " + originalDataset.classIndex());
        log.info("Class name" + originalDataset.attribute(originalDataset.classIndex()).name());
        log.info("Attribute Class Index: " + originalDataset.attributeStats(originalDataset.classIndex()));
        log.info(originalDataset.toSummaryString());
    }

    /**
     * Separates the data set into the 3 different molecular levels.
     *
     * @throws Exception the exception
     */
    @Test
    void testSeparateMolecularLevels() throws Exception {
        String mRnaPath = Constants.SRC_MAIN_RESOURCES_PATH + "mRNAdataset" + WekaUtils.WEKA_SUFFIX;
        String miRnaPath = Constants.SRC_MAIN_RESOURCES_PATH + "miRNAdataset" + WekaUtils.WEKA_SUFFIX;
        String methPath = Constants.SRC_MAIN_RESOURCES_PATH + "methDataset" + WekaUtils.WEKA_SUFFIX;
        File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
        Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        // ASSERTS
        Assertions.assertEquals(72122, originalDataset.numAttributes(), "Number of attributes should be 72122");
        Assertions.assertEquals(335, originalDataset.numInstances(), "Number of instances should be 335");
        Assertions.assertEquals(72121, originalDataset.classIndex(), "Class index should be 72121");
        Assertions.assertEquals(classRealName, originalDataset.attribute(originalDataset.classIndex()).name(),
                                "Class name should be SampleStatus");
        Assertions.assertEquals(2, originalDataset.numClasses(), "Number of classes should be 2");
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
        Instances mRNADataset = WekaUtils.getOriginalData(mRNAFile, classRealName);
        Instances miRNADataset = WekaUtils.getOriginalData(miRNAFile, classRealName);
        Instances methDataset = WekaUtils.getOriginalData(methFile, classRealName);
        // -2 because we add the class in each sub data set
        Assertions.assertEquals(
                mRNADataset.numAttributes() + miRNADataset.numAttributes() + methDataset.numAttributes() - 2,
                originalDataset.numAttributes(), "The sum of the 3 datasets should be equal to the original");
        Assertions.assertEquals(mRNADataset.numInstances() + miRNADataset.numInstances() + methDataset.numInstances(),
                                originalDataset.numInstances() * 3, "Number of instances should be the same");
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
            }
            else if (name.startsWith("hsa-mir") || name.startsWith("hsa-let")) {
                miRNAs.add(attIndex);
            }
            else {
                meth.add(attIndex);
            }
        }
        // add class index
        mRNAs.add(classIndex);
        miRNAs.add(classIndex);
        meth.add(classIndex);
    }

    @SuppressWarnings("unused")
    private static void stringAttributesInfo(Instances data) {
        Assertions.assertEquals(335, data.numInstances(), "Number of instances");
        Assertions.assertEquals(73665, data.numAttributes(), "Number of attributes");
        int count = 0;
        Enumeration<Attribute> attEnumeration = data.enumerateAttributes();
        StringBuilder sbLatex = new StringBuilder();
        while (attEnumeration.hasMoreElements()) {
            Attribute attribute = attEnumeration.nextElement();
            if (attribute.type() == Attribute.STRING) {
                count++;
                LatexUtils.latexTableForFeatures(sbLatex, attribute.name(), count);
                AttributeStats stats = data.attributeStats(attribute.index());
                Assertions.assertEquals(0, stats.distinctCount, "The distinct values");
                Assertions.assertEquals(335, stats.missingCount, "The missing values");
                Assertions.assertEquals(0, stats.uniqueCount, "The unique values");
            }
        }
        Assertions.assertEquals(1540, count, "Number of string attributes");
        // add the last 3 features which are removed and are not string
        sbLatex.append("DATA")
               .append(" & ")
               .append("Vital\\_status")
               .append(" & ")
               .append("Days\\_To\\_Death")
               .append(" & ")
               .append(" & ")
               .append(" \\\\")
               .append(System.lineSeparator());
        log.info("Invalid Features: " + sbLatex.toString());
    }
}

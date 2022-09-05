package personalizedmedicine.tests;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import scala.Tuple2;
import weka.core.Attribute;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static personalizedmedicine.helpful_classes.Constants.*;

/*
 * The standard Scala backend is a Java VM. Scala classes are Java classes, and vice versa.
 * You can call the methods of either language from methods in the other one.
 * You can extend Java classes in Scala, and vice versa.
 * The main limitation is that some Scala features do not have equivalents in Java, for example traits.
 */
@Slf4j
public class TestEMPCA {

    /**
     * Test EMPCA calculation, prints eigenvalues and eigenvectors to log, saves new
     * low dimensional data to arff file and does a simple classification.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void testEMPCA() throws IOException {
        File level2File = new File(completeFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        int nPCs = 20;
        // INPUT TO EMPCA PART
        List<ArrayList<Feature>> empcaInput = TransformToFromWeka.createEMPCAInputFromWekaV2(originalDataset);
        Assertions.assertEquals(72121, empcaInput.size(), "The number of lists (features) should be 72121");
        // the position 3 is chosen arbitary.
        Assertions.assertEquals(335, empcaInput.get(3).size(), "The number of features of each instance should be 335");
        scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala.convert(
                (ArrayList<ArrayList<Feature>>) empcaInput);
        Assertions.assertEquals(empcaInput.size(), convertedToScalaList.length,
                                "The sizes of the 2 arrays should be the same");
        Assertions.assertEquals(empcaInput.get(3).size(), convertedToScalaList[3].length(),
                                "The sizes of the 2 arrays should be the same");
        // DO EMPCA PART
        // the second parameter is the number of the principal components we want as
        // result. Due to a hack it will return always minus 10 from the expected.
        EMPCA empca = new EMPCA(convertedToScalaList, nPCs);
        DoubleMatrix2D c = empca.performEM(20);
        log.info("finish perform em");
        Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
        Utils.writeEigensToFile(TESTS_RESULTS_FOLDER_PATH + "output.log", eigenValueAndVectors);
        log.info("finish doEig");
        // OUTPUT TO WEKA PART
        Instances reData = TransformToFromWeka.eigensToWeka(eigenValueAndVectors._2, "empcaDataset",
                                                            WekaUtils.getDatasetClassValues(originalDataset),
                                                            classNameForReducedData);
        // here we save the new data in an arff file
        WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
        wekaFileConverterImpl.arffSaver(reData, TESTS_RESULTS_FOLDER_PATH + (nPCs - 10) + "empcaData.arff");
        // eigenValueAndVectors._2.columns() + 1 be the new data set has also the class
        // attribute.
        Assertions.assertEquals(reData.numAttributes(), eigenValueAndVectors._2.columns() + 1);
        Assertions.assertEquals(reData.numInstances(), eigenValueAndVectors._2.rows());
    }

    /**
     * Same as testEMPCA but it is called from DimensionalityReductionSelector.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void doEMPCA() throws IOException {
        // first option should be the number of expecting principal components
        // second option should be the desirable number of em iterations
        // the last 2 options will be always the dataset name and the name of the class
        File level2File = new File(completeFileName);
        Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        // number of principal components, the result due to hack will be minus 10
        // There is a hack in the source code of EMPCA you can not create more principal
        // components than the data instances
        String[] nPCsArray = {"20", "30", "60", "110"};

        for (String nPCs : nPCsArray) {
            String newDatasetName = Integer.parseInt(nPCs) - 10 + "empcaData";
            String[] options = {nPCs, "20", newDatasetName, classNameForReducedData};
            // Get current time
            long start = System.nanoTime();
            DimensionalityReductionChooser dimensionalityReductionSelection = new DimensionalityReductionChooser();
            Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(Constants.EMPCA,
                                                                                                 originalDataset, true,
                                                                                                 options);
//			System.out.println("Execution for EMPCA with " + nPCs + " principal components\n");
//			Constants.logger.getLogger().log(Level.INFO,
//					"Execution for EMPCA with " + nPCs + " principal components\n");
//			Constants.logger.getLogger().log(Level.INFO, Utils.printExecutionTime(start, System.nanoTime()));
            // here we save the new data in an arff file
            WekaFileConverter wekaFileConverterImpl = new WekaFileConverter();
            wekaFileConverterImpl.arffSaver(dataset,
                                            TESTS_RESULTS_FOLDER_PATH + newDatasetName + WekaUtils.WEKA_SUFFIX);
        }
    }

    /**
     * Prints the eigen values vectors.
     *
     * @param eigenValueAndVectors the eigen value and vectors
     */
    @SuppressWarnings("unused")
    private static void printEigenValuesVectors(Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors) {
        log.info("start printing values");
        log.info("EigenValues" + System.lineSeparator());
        for (int i = 0; i < eigenValueAndVectors._1.length; i++) {
            double value = eigenValueAndVectors._1[i];
            log.info(value + "\t");
        }
        log.info("EigenVectors" + System.lineSeparator());
        for (int i = 0; i < eigenValueAndVectors._2.columns(); i++) {
            log.info(String.valueOf(eigenValueAndVectors._2.viewColumn(i)));
        }
    }

    /**
     * Test random data set.
     */
    @Test
    void testRandomDataSet() {
        ArrayList<ArrayList<Feature>> dataset = testData();
        scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala.convert(
                dataset);
        // DO EMPCA PART
        EMPCA empca = new EMPCA(convertedToScalaList, 20);
        DoubleMatrix2D c = empca.performEM(20);
        System.out.println("finish perform em");
        Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
        log.info("finish doEig");
        log.info("Number of eigenValues: " + eigenValueAndVectors._1.length + System.lineSeparator());
        log.info("Number of eigenVectors (rows): " + eigenValueAndVectors._2.rows() + System.lineSeparator());
        log.info("Number of eigenVectors (columns): " + eigenValueAndVectors._2.columns() + System.lineSeparator());
    }

    /**
     * Test data.
     *
     * @return the array list
     */
    private static ArrayList<ArrayList<Feature>> testData() {
        Random rand = new Random();
        DecimalFormat df = new DecimalFormat("#.###");
        ArrayList<ArrayList<Feature>> empcaInput = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            ArrayList<Feature> features = new ArrayList<>();
            for (int j = 0; j < 750; j++) {
                double random = rand.nextDouble();
                features.add(new Feature(j, Double.parseDouble(df.format(random))));
            }
            empcaInput.add(features);
        }
        return empcaInput;
    }

    public static ArrayList<ArrayList<Feature>> exampleTest() {
        ArrayList<ArrayList<Feature>> data = new ArrayList<>();
        ArrayList<Feature> instance1 = new ArrayList<>();
        instance1.add(new Feature(1, 0.1));
        instance1.add(new Feature(2, 0.2));
        instance1.add(new Feature(3, 0.245));
        instance1.add(new Feature(4, 0.34));
        instance1.add(new Feature(5, 0.2));
        instance1.add(new Feature(6, 0.3));
        instance1.add(new Feature(7, 0.5));
        ArrayList<Feature> instance2 = new ArrayList<>();
        instance2.add(new Feature(1, 0.543));
        instance2.add(new Feature(2, 0.54));
        instance2.add(new Feature(3, 0.245));
        instance2.add(new Feature(4, 0.3234));
        instance2.add(new Feature(5, 0.24));
        instance2.add(new Feature(6, 0.24));
        instance2.add(new Feature(7, 0.43));

        data.add(instance1);
        data.add(instance2);
        return data;
    }

    /**
     * Check is nominal.
     *
     * @param attribute the attribute
     */
    @SuppressWarnings("unused")
    private void checkIsNominal(Attribute attribute) {
        if (attribute.isNominal()) {
            log.info(attribute.name());
        }
    }

    @Test
    void testEMPCADataFiles() {
        try {
            int[] nPCsArray = {10, 20, 50, 100};
            for (int nPCs : nPCsArray) {
                String newDatasetName = nPCs + "empcaData";
                File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + newDatasetName + WekaUtils.WEKA_SUFFIX);
                Instances dataset = WekaUtils.getOriginalData(level2File, classNameForReducedData);
                Assertions.assertEquals(335, dataset.numInstances(), "Number of instances");
                Assertions.assertEquals(nPCs + 1, dataset.numAttributes(), "Number of attributes");
            }
        } catch (IOException e) {
            Assertions.fail();
        }
    }
}

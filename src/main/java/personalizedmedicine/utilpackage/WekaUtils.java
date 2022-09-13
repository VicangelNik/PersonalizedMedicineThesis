package personalizedmedicine.utilpackage;

import lombok.extern.slf4j.Slf4j;
import personalizedmedicine.classifiers.ClassifierChooser;
import personalizedmedicine.interfaces.IClassifierSelection;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public final class WekaUtils {

    private WekaUtils() {
        throw new IllegalArgumentException("Utility class");
    }

    public static final String WEKA_SUFFIX = ".arff";
    public static final String CSV_SUFFIX = ".csv";
    public static final String NOT_AVAILABLE = "NA";

    /**
     * Dimension is NA.
     * it will be used in the future do not remove it!!!!
     *
     * @param featureDataList the feature data list
     * @return true, if successful
     */
    @SuppressWarnings("unused")
    private static boolean dimensionIsNA(List<String> featureDataList) {
        for (String datum : featureDataList) {
            if (!NOT_AVAILABLE.equals(datum)) {
                return false;
            }
        }
        return true;
    }

    public static AbstractClassifier getClassifier(final String classifierName, final Instances instances,
                                                   final String[] options) {
        log.info("OPTIONS: " + Arrays.toString(options));
        IClassifierSelection classifierSelection = new ClassifierChooser();
        return classifierSelection.selectClassifier(classifierName, instances, options);
    }

    public static Instances getOriginalData(File file, String featureClassName) throws IOException {
        AbstractFileLoader loader = new ArffLoader();
        String ext = com.google.common.io.Files.getFileExtension(file.toString());
        // returns the extension (csv) without the dot, so we remove the dot from constant.
        if (ext.equals(CSV_SUFFIX.substring(1))) {
            loader = new CSVLoader();
        }
        loader.setFile(file);
        final Instances originalData = loader.getDataSet();
        originalData.setClass(originalData.attribute(featureClassName));
        return originalData;
    }

    public static void serializeObject(final String filePath, final Object[] object, final boolean writeAll) {
        Path path = Paths.get(filePath);
        // create the directory to which the object will be written
        try {
            Files.createDirectories(path.getParent());
            if (writeAll) {
                weka.core.SerializationHelper.writeAll(path.toString(), object);
            }
            else {
                weka.core.SerializationHelper.write(path.toString(), object);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            try {
                Files.delete(path);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static Object[] deserializeObject(final String filePath, final boolean readAll) throws Exception {
        Path path = Paths.get(filePath);
        if (readAll) {
            return weka.core.SerializationHelper.readAll(path.toString());
        }
        else {
            return (Object[]) weka.core.SerializationHelper.read(path.toString());
        }
    }

    public static List<Double> getDatasetClassValues(final Instances originalDataset) {
        List<Double> classValues = new ArrayList<>();
        for (int i = 0; i < originalDataset.numInstances(); i++) {
            Instance current = originalDataset.get(i);
            classValues.add(current.value(originalDataset.classIndex()));
        }
        return classValues;
    }

    public static void crossValidationAction(String classifierName, AbstractClassifier classifier, int numFolds,
                                             int random) {
        IClassifierSelection classifierSelection = new ClassifierChooser();
        classifierSelection.crossValidationAction(classifierName, classifier, numFolds, random);
    }
}

/*
 *
 */
package personalizedmedicine.utilpackage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import personalizedmedicine.classifiers.ClassifierChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.interfaces.IClassifierSelection;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

/**
 * The Class WekaUtils.
 */
public final class WekaUtils {

	/**
	 * Instantiates a new weka utils.
	 */
	private WekaUtils() {
		throw new IllegalArgumentException("utillity class");
	}

	/** The Constant WEKA_SUFFIX. */
	public static final String WEKA_SUFFIX = ".arff";
	/** The Constant CSV_SUFFIX. */
	public static final String CSV_SUFFIX = ".csv";
	/** The Constant NUMERIC_ATTRIBUTE. */
	public static final String NUMERIC_ATTRIBUTE = "NUMERIC";
	/** The Constant NOT_AVAILABLE. */
	public static final String NOT_AVAILABLE = "NA";

	/**
	 * Dimension is NA.
	 *
	 * @param featureDataList the feature data list
	 * @return true, if successful
	 */
	// it will be used in the future dont remove it!!!!
	@SuppressWarnings("unused")
	private static boolean dimensionIsNA(List<String> featureDataList) {
		for (String datum : featureDataList) {
			if (!WekaUtils.NOT_AVAILABLE.equals(datum)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the classifier.
	 *
	 * @param classifierName the classifier name
	 * @param instances      the instances
	 * @param options        the options
	 * @return the classifier
	 */
	public static AbstractClassifier getClassifier(String classifierName, Instances instances, String[] options) {
		if (Constants.logger != null) {
			Constants.logger.getLogger().log(Level.INFO, "OPTIONS: {0}", Arrays.toString(options));
		}
		IClassifierSelection classifierSelection = new ClassifierChooser();
		return classifierSelection.selectClassifier(classifierName, instances, options);
	}

	/**
	 * Gets the original data.
	 *
	 * @param file             the file
	 * @param featureClassName the feature class name
	 * @return the original data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Instances getOriginalData(File file, String featureClassName) throws IOException {
		AbstractFileLoader loader = new ArffLoader();
		String ext = com.google.common.io.Files.getFileExtension(file.toString());
		// returns the extension (csv) without the dot so we remove the dot from
		// constant
		if (ext.equals(CSV_SUFFIX.substring(1))) {
			loader = new CSVLoader();
		}
		loader.setFile(file);
		// Constants.logger.getLogger().log(Level.INFO, "SAVE FILE NAME: " +
		// file.getName());
		Instances originalData = loader.getDataSet();
		originalData.setClass(originalData.attribute(featureClassName));
		return originalData;
	}

	/**
	 * Serialize object.
	 *
	 * @param filePath the file path
	 * @param object   the object
	 * @param writeAll the write all
	 */
	public static void serializeObject(String filePath, Object[] object, boolean writeAll) {
		Path path = Paths.get(filePath);
		// create the directory to which the object will be written
		try {
			Files.createDirectories(path.getParent());
			if (writeAll) {
				weka.core.SerializationHelper.writeAll(path.toString(), object);
			} else {
				weka.core.SerializationHelper.write(path.toString(), object);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			// nothing to do yet
		}
	}

	/**
	 * Desirialize object.
	 *
	 * @param filePath the file path
	 * @param readAll  the read all
	 * @return the object[]
	 * @throws Exception the exception
	 */
	public static Object[] desirializeObject(String filePath, boolean readAll) throws Exception {
		Path path = Paths.get(filePath);
		if (readAll) {
			return weka.core.SerializationHelper.readAll(path.toString());
		} else {
			return (Object[]) weka.core.SerializationHelper.read(path.toString());
		}
	}

	/**
	 * Gets the dataset class values.
	 *
	 * @param originalDataset the original dataset
	 * @return the dataset class values
	 */
	public static List<Double> getDatasetClassValues(Instances originalDataset) {
		List<Double> classValues = new ArrayList<>();
		for (int i = 0; i < originalDataset.numInstances(); i++) {
			Instance current = originalDataset.get(i);
			classValues.add(current.value(originalDataset.classIndex()));
		}
		return classValues;
	}

	/**
	 * Cross validation action.
	 *
	 * @param classifierName the classifier name
	 * @param classifier     the classifier
	 * @param numFolds       the num folds
	 * @param random         the random
	 */
	public static void crossValidationAction(String classifierName, AbstractClassifier classifier, int numFolds,
			int random) {
		IClassifierSelection classifierSelection = new ClassifierChooser();
		classifierSelection.crossValidationAction(classifierName, classifier, numFolds, random);
	}
}

/*
 *
 */
package utilpackage;

/*
 * Class for weka utilities
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.base.Strings;

import helpful_classes.MultiKey;

// TODO: Auto-generated Javadoc
/**
 * The Class WekaUtils.
 */
public final class WekaUtils {

	private WekaUtils() {
		// nothing to do
	}

	/** The Constant WEKA_SUFFIX. */
	public static final String WEKA_SUFFIX = ".arff";

	public static final String NUMERIC_ATTRIBUTE = "NUMERIC";

	public static final String NOT_AVAILABLE = "NA";

	private static final Logger LOGGER = Logger.getLogger(WekaUtils.class.getName());

	/**
	 * Prepare attributes for weka file.
	 *
	 * @param attributes the attributes
	 * @return the string
	 */
	private static String prepareAttributesForWekaFile(Map<String, List<String>> attributes) {
		StringBuilder attributeLine = new StringBuilder();
		attributes.forEach((key, value) -> {
			attributeLine.append("@attribute ");
			attributeLine.append(key);
			attributeLine.append(" ");
			if (value.size() == 1) {
				attributeLine.append(value.get(0));
				attributeLine.append(System.lineSeparator());
			} else {
				StringBuilder attributeMultiValue = new StringBuilder("{");
				for (String attribute : value) {
					if (!value.get(value.size() - 1).equals(attribute)) {
						attributeMultiValue.append(attribute + ", ");
					} else {
						attributeMultiValue.append(attribute);
						attributeMultiValue.append("}");
						attributeMultiValue.append(System.lineSeparator());
					}
				}
				attributeLine.append(attributeMultiValue.toString());
			}
		});
		return attributeLine.toString();
	}

	/**
	 * Prepare weka file. Make the file with the data that will be as input in the
	 * weka
	 *
	 * @param relation     the relation
	 * @param attributes   the attributes
	 * @param fileDataName the data file name to read.
	 * @param fileName     the file name to write
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void prepareWekaFile(String relation, Map<String, List<String>> attributes, File fileDataName,
			String fileName) throws IOException {
		// make the relation tag line
		String relationline = "@relation " + relation + System.lineSeparator();
		// make the attribute tag lines
		String attributeLines = prepareAttributesForWekaFile(attributes);
		String readLine;
		// read from data file
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileDataName));
				Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
			// write the relation tag line
			writer.write(relationline);
			writer.write(System.lineSeparator());
			// write the attribute tag lines
			writer.write(attributeLines);
			writer.write(System.lineSeparator());
			writer.write("@data");
			writer.write(System.lineSeparator());
			// read from data file line by line
			while ((readLine = bufferedReader.readLine()) != null) {
				// write line to weka file
				writer.write(readLine);
				writer.write(System.lineSeparator());
			}
		}
	}

	/**
	 * Checks it the feature is numeric
	 *
	 * @param featureDataList A list with all data of the dimension.
	 * @return
	 */
	private static boolean dimensionIsNumeric(List<String> featureDataList) {
		for (String datum : featureDataList) {
			if (NumberUtils.isCreatable(datum)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the feature is not Numeric. It can be nominal, string or date.
	 *
	 * @param featureDataList A list with all data of the dimension.
	 * @return
	 */
	private static List<String> dimensionNotNumeric(List<String> featureDataList) {
		List<String> notNumeric = new ArrayList<>();
		for (int i = 1; i < featureDataList.size(); i++) {
			String datum = featureDataList.get(i);
			if (!notNumeric.contains(datum) && !WekaUtils.NOT_AVAILABLE.equals(datum)
					&& !Strings.isNullOrEmpty(datum)) {
				notNumeric.add(datum);
			}
		}
		return notNumeric;
	}

	/**
	 * Checks if the feature has only NA data.
	 *
	 * @param featureDataList
	 * @return
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
	 *
	 * @param allData
	 * @return the actual dimension size list.
	 */
	public static List<String> getDimensions(List<List<String>> allData) {
		String notFeatureWord = "DATA";
		// get the dimension names from the file
		List<String> dimensions = new ArrayList<>(Arrays.asList(allData.get(0).get(0).split("\t")));
		if (dimensions.contains(notFeatureWord)) {
			boolean isRemoved = dimensions.remove(notFeatureWord);
			LOGGER.log(Level.INFO, "data word was contained in feature list: {0}", Boolean.toString(isRemoved));
		}
		// LOGGER.log(Level.INFO, "All dimensions: {0}", dimensions);
		LOGGER.log(Level.INFO, "Number of features is: {0}", dimensions.size());
		return dimensions;
	}

	/**
	 * Filtes the data and keeps only valid columns. Returns the attributes with
	 * their types.
	 *
	 * @param allDataColumnWise every list the data contains its a feature with the
	 *                          data.
	 * @return the feature mapped with the data.
	 */
	public static Map<MultiKey, List<String>> filterValidFeaturesAndData(List<List<String>> allDataColumnWise) {
		LinkedHashMap<MultiKey, List<String>> attributeMap = new LinkedHashMap<>();
		// we start from one to avoid the first line that contains the list of the
		// cases.
		for (int j = 1; j < allDataColumnWise.size(); j++) {
			List<String> featureDataList = allDataColumnWise.get(j);
			// we take features that all their data are available.
			if (!featureDataList.contains(WekaUtils.NOT_AVAILABLE)) {
				List<String> attributeTypeList = new ArrayList<>();
				if (dimensionIsNumeric(featureDataList)) {
					attributeTypeList.add(WekaUtils.NUMERIC_ATTRIBUTE);
				} else {
					// we take features that are not numeric. They can be String, nominal or date.
					attributeTypeList = dimensionNotNumeric(featureDataList);
				}
				if (attributeTypeList.isEmpty()) {
					LOGGER.log(Level.SEVERE, "The type of the feature {0} could not be determined ",
							featureDataList.get(0));
					allDataColumnWise.remove(j);
				} else {
					// in the first position of each list, there is always the feature name.
					MultiKey multiKey = new MultiKey(featureDataList.get(0), attributeTypeList);
					attributeMap.put(multiKey, featureDataList);
				}
			} else {
				LOGGER.log(Level.INFO, " Feature {0} is invalid and is removed", featureDataList.get(0));
				allDataColumnWise.remove(j);
			}
		}
		return attributeMap;
	}

	/**
	 * Creates the weka file.
	 *
	 * @param relation          the relation of weka data.
	 * @param attributes        the attribute names with their types.
	 * @param allDataColumnWise the data of the attributes.
	 * @param fileName          the file path of the weka file to be created.
	 * @throws IOException
	 */
	public static void createWekaFile(String relation, Map<MultiKey, List<String>> attributes,
			List<List<String>> allDataColumnWise, String fileName) throws IOException {
		// make the relation tag line
		String relationline = "@relation " + relation + System.lineSeparator();
		// make the attribute tag lines
		String attributeLines = prepareAttributesForWekaFile(getMultikeyAsMap(attributes.keySet()));
		// read from data file
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
			// write the relation tag line
			writer.write(relationline);
			writer.write(System.lineSeparator());
			// write the attribute tag lines
			writer.write(attributeLines);
			writer.write(System.lineSeparator());
			writer.write("@data");
			writer.write(System.lineSeparator());
			List<List<String>> patientWiseData = convertFeatureWiseToPatientWise(attributes.values());
			// read from data line by line
			for (List<String> lineDataList : patientWiseData) {
				writer.write(lineDataList.toString().replaceAll("[|]", ""));
				// write line to weka file
				writer.write(System.lineSeparator());
			}
		}
	}

	/**
	 * Takes a list of lists that each list has the feature and its data and returns
	 * a list of lists that for each list the data of the case are contained by.
	 *
	 * @param allDataColumnWise
	 * @return
	 */
	private static List<List<String>> convertFeatureWiseToPatientWise(Collection<List<String>> values) {
		Iterator<List<String>> iterator = values.iterator();
		int lineNumber = iterator.next().size();
		List<List<String>> records = Utils.instantiateListOfStringLists(lineNumber);
		while (iterator.hasNext()) {
			List<String> featureDataList = iterator.next();
			for (int i = 1; i < lineNumber; i++) {
				records.get(i - 1).add(featureDataList.get(i));
			}
		}
		return records;
	}

	/**
	 * Checks the number of features that are the same with the number of data.
	 *
	 * @param data
	 */
	public static boolean checkNumberFeatureData(List<List<String>> data) {
		boolean isValid = true;
		int featureSize = data.get(0).size();
		for (int i = 1; i < data.size(); i++) {
			int lineSize = data.get(i).size();
			if (lineSize != featureSize) {
				isValid = false;
				Object[] information = { data.get(i).get(0), lineSize };
				LOGGER.log(Level.INFO, "The feature {0} has {1} number of values.", information);
			}
		}
		return isValid;
	}

	private static Map<String, List<String>> getMultikeyAsMap(Set<MultiKey> multiKeySet) {

		Map<String, List<String>> attributeForWeka = new LinkedHashMap<>();
		for (MultiKey key : multiKeySet) {
			attributeForWeka.put(key.getFeatureName(), key.getFeatureType());
		}
		return attributeForWeka;

	}
}

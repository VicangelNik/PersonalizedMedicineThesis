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
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class WekaUtils.
 */
public class WekaUtils {

	/** The Constant WEKA_SUFFIX. */
	public static final String WEKA_SUFFIX = ".arff";

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
	 * @param relation   the relation
	 * @param attributes the attributes
	 * @param data       the data
	 * @param fileName   the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void prepareWekaFile(String relation, Map<String, List<String>> attributes, File data,
			String fileName) throws IOException {
		// make the relation tag line
		String relationline = "@relation " + relation + System.lineSeparator();
		// make the attribute tag lines
		String attributeLines = prepareAttributesForWekaFile(attributes);
		String readLine;
		// read from data file
		try (BufferedReader bufferedReader = new BufferedReader(
				new FileReader(Utils.RELATIVE_PATH + "data" + Utils.FILE_SUFFIX));
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
	 * Instantiates a new weka utils.
	 */
	public WekaUtils() {
		// nothing to do
	}
}

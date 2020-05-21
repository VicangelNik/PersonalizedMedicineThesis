package utilpackage;

import java.util.Enumeration;
import java.util.logging.Level;

import helpful_classes.AppLogger;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * The Class LatexUtils.
 */
public final class LatexUtils {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/**
	 * Instantiates a new latex utils.
	 */
	private LatexUtils() {
		throw new IllegalArgumentException("utillity class");
	}

	/**
	 * Latex table for features.
	 *
	 * @param sbLatex       the sb latex
	 * @param attributeName the attribute name
	 * @param count         the count
	 */
	public static void latexTableForFeatures(StringBuilder sbLatex, String attributeName, int count) {
		if (attributeName.contains("_")) {
			attributeName = attributeName.replace("_", "\\_");
		}
		sbLatex.append(attributeName);
		if (count % 5 == 0) {
			// every 5 features define new line in table
			sbLatex.append(" \\\\"); // to write \\ because \ is special character
			sbLatex.append(System.lineSeparator());
		} else {
			// add feature in line
			sbLatex.append(" & ");
		}
	}

	/**
	 * Creates the data in latex format.
	 *
	 * @param data the data
	 */
	public static void createDataInLatexFormat(Instances data) {
		int count = 0;
		Enumeration<Attribute> attEnumeration = data.enumerateAttributes();
		StringBuilder sbLatex = new StringBuilder();
		while (attEnumeration.hasMoreElements()) {
			Attribute attribute = attEnumeration.nextElement();
			count++;
			LatexUtils.latexTableForFeatures(sbLatex, attribute.name(), count);
		}
		int addAmber = count % 5;
		for (int i = 0; i < 4 - addAmber; i++) {
			sbLatex.append(" & ");
		}
		sbLatex.append(data.classAttribute().name()).append(" \\\\").append(System.lineSeparator());
		logger.getLogger().log(Level.INFO, "{0}", "Valid Features: " + sbLatex.toString());
	}
}

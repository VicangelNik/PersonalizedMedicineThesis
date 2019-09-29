/*
 * Custom validation of the weka arf file.
 */
package validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

public final class WekaValidation {

	private WekaValidation() {
		throw new IllegalArgumentException("Utility class");
	}

	private static final Logger LOGGER = Logger.getLogger(WekaValidation.class.getName());

	/**
	 * Validates weka arf file.
	 * 
	 * @param filePath
	 * @param attributesNumber
	 * @param dataSeperator
	 */
	public static void wekaArfFileValidation(String filePath, int attributesNumber, String dataSeperator) {
		try (FileInputStream inputStream = new FileInputStream(filePath);
				Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			int lines = 0;
			int attributesCount = 0;
			boolean isDataParsed = false;
			int dataCount = 0;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				lines++;
				if (lines == 1 && !isRelationValid(line)) {
					throw new RuntimeException("The relation is not valid " + line);
				}
				if (lines > 2 && !Strings.isNullOrEmpty(line)) {
					if (!isDataParsed && isAttributeValid(line)) {
						attributesCount++;
					} else if (isDataParsed) {
						isDataValid(line, dataSeperator, attributesCount);
						dataCount++;
					} else if (line.equals("@data")) {
						isDataParsed = true;
					} else {
						throw new RuntimeException("Data are invalid " + line);
					}
				}

			}
			if (!isDataParsed || attributesCount != attributesNumber || attributesCount != dataCount) {
				LOGGER.log(Level.SEVERE, "Data are invalid");
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Checks if attribute is valid.
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isAttributeValid(String line) {
		Pattern pattern = Pattern.compile(
				"[@][a][t][t][r][i][b][u][t][e][\\s][\\w\\.-]+[\\s](([a-zA-Z]+)|([{]([a-zA-Z]+[,])+[\\s][a-zA-Z]+[}]))");
		Matcher matcher = pattern.matcher(line.trim());
		return matcher.matches();
	}

	/**
	 * Checks if relation is valid.
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isRelationValid(String line) {
		Pattern pattern = Pattern.compile("[@][r][e][l][a][t][i][o][n][\\s][a-z]+");
		Matcher matcher = pattern.matcher(line.trim());
		return matcher.matches();
	}

	/**
	 * Checks if the data line has the expected number of data.
	 * 
	 * @param line
	 * @param dataSeperator
	 * @param attributesCount
	 */
	private static void isDataValid(String line, String dataSeperator, int attributesCount) {
		String[] dataValues = line.split(dataSeperator);
		if (dataValues.length != attributesCount) {
			throw new RuntimeException("Data are invalid with number of values: " + dataValues.length);
		}
	}
}

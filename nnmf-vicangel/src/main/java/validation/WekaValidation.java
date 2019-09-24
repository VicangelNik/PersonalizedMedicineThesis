/*
 *
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
		// nothing to do
	}

	private static final Logger LOGGER = Logger.getLogger(WekaValidation.class.getName());

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
					String message = "The relation is not valid";
					LOGGER.log(Level.SEVERE, message);
				}
				if (lines > 1 && !isAttributeValid(line)) {
					String message = "The attribute is not valid";
					LOGGER.log(Level.SEVERE, message);
				} else {
					attributesCount++;
				}
				if (attributesCount == attributesNumber) {
					if (line.equals("@data")) {
						isDataParsed = true;
					}
					if (isDataParsed && !Strings.isNullOrEmpty(line)) {
						String[] dataValues = line.split(dataSeperator);
						if (dataValues.length != 413) {
							LOGGER.log(Level.SEVERE, "Data are invalid");
						}
						dataCount++;
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

	private static boolean isAttributeValid(String line) {
		Pattern pattern = Pattern.compile(
				"[@][a][t][t][r][i][b][u][t][e][\\s][a-zA-Z]+[\\s](([a-zA-Z]+)|([{]([a-zA-Z]+[,])+[\\s][a-zA-Z]+[}]))");
		Matcher matcher = pattern.matcher(line.trim());
		return matcher.matches();
	}

	private static boolean isRelationValid(String line) {
		Pattern pattern = Pattern.compile("[@][r][e][l][a][t][i][o][n][\\s][a-z]+");
		Matcher matcher = pattern.matcher(line.trim());
		return matcher.matches();
	}
}

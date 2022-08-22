/*
 *
 */
package java.personalizedmedicine.utiltestpackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The Class Utils.
 */
public class TestUtils {

	/**
	 * Read index for isomap test.
	 *
	 * @param fileName the file name
	 * @return the int[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int[] readIndexForIsomapTest(String fileName) throws IOException {
		String data = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] arrOfStr = data.replaceAll("[\\[\\] ]", "").split(",");
		int[] index = new int[arrOfStr.length];
		for (int i = 0; i < arrOfStr.length; i++) {
			index[i] = Integer.parseInt(arrOfStr[i].toString());
		}
		return index;
	}

	/**
	 * Read coordinates for isomap test.
	 *
	 * @param fileName the file name
	 * @return the double[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static double[] readCoordinatesForIsomapTest(String fileName) throws IOException {
		String data = new String(Files.readAllBytes(Paths.get(fileName)));
		data.replaceAll("[", "");
		data.replaceAll("]", "");
		String[] arrOfStr = data.split(",", 2);
		double[] coordinates = new double[arrOfStr.length];
		for (int i = 0; i < arrOfStr.length; i++) {
			coordinates[i] = Double.parseDouble(arrOfStr[i]);
		}
		return coordinates;
	}
}

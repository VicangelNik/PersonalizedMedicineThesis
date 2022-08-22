/*
 *
 */
package personalizedmedicine.utilpackage;

/*
 *
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jblas.DoubleMatrix;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.helpful_classes.EnumSeparators;
import scala.Tuple2;

/**
 * The Class Utils.
 *
 * @author Nikiforos
 */
public final class Utils {

	/** The Constant rand. */
	public static final Random rand = new Random();

	/** The Constant FILE_SUFFIX. */
	public static final String TXT_SUFFIX = ".txt";

	/** The file data path. */
	public static final String FILE_DATA_PATH = Constants.SRC_TEST_RESOURCES_PATH + "data" + TXT_SUFFIX;

	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

	/**
	 * Instantiates a new utils.
	 */
	private Utils() {
		throw new IllegalArgumentException("utillity class");
	}

	/**
	 * Feed double array with random numbers.
	 *
	 * @param seed              the seed
	 * @param fromRow           the from row
	 * @param toRow             the to row
	 * @param dimension         the dimension
	 * @param desiredMean       the desired mean
	 * @param standardDeviation the standard deviation
	 * @param doubleArray       the double array
	 * @param isPositive        the is positive
	 */
	public static void feedDoubleArray(long seed, int fromRow, int toRow, int dimension, double desiredMean,
			double standardDeviation, double doubleArray[][], boolean isPositive) {
		// We generate the same "random" numbers, seed variable is a number of
		// my
		// choice.
		Utils.rand.setSeed(seed);
		// Returns the next pseudorandom, Gaussian ("normally") distributed
		// double value
		// with mean 0.0 and standarddeviation 1.0 from this random number
		// generator's
		// sequence. r.nextGaussian()*desiredStandardDeviation+desiredMean;
		for (int i = fromRow; i < toRow; i++) {
			for (int j = dimension; j <= dimension; j++) {
				doubleArray[i][j] = Utils.rand.nextGaussian() * desiredMean + standardDeviation;
				if (isPositive && doubleArray[i][j] < 0) {
					doubleArray[i][j] *= -1;
				}
			}
		}
	}

	/**
	 * Make file path. Construct the path which the file will be written
	 *
	 * @param matrixName   the matrix name
	 * @param nameOfMethod the name of method
	 * @param suffix       the suffix
	 * @return the string
	 */

	private static String makeFilePath(String matrixName, String nameOfMethod, String suffix) {
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.SRC_TEST_RESOURCES_PATH);
		sb.append(nameOfMethod);
		sb.append(matrixName);
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * Sets the parameters to array.
	 *
	 * @param doubleArray the new parameters to array
	 */
	public static void setParametersToArray(double doubleArray[][]) {
		// chinese
		Utils.feedDoubleArray(4, 0, 200, 0, 1.6f, 0.6f, doubleArray, true); // height
		Utils.feedDoubleArray(4, 0, 200, 1, 90f, 5, doubleArray, true); // death estimation
		Utils.feedDoubleArray(4, 0, 200, 2, 50f, 10, doubleArray, true); // age
		Utils.feedDoubleArray(4, 0, 200, 3, 55f, 15, doubleArray, true); // weight
		// greek
		Utils.feedDoubleArray(4, 200, 400, 0, 1.8f, 0.6f, doubleArray, true); // height
		Utils.feedDoubleArray(4, 200, 400, 1, 80f, 5, doubleArray, true); // death estimation
		Utils.feedDoubleArray(4, 200, 400, 2, 55f, 10, doubleArray, true); // age
		Utils.feedDoubleArray(4, 200, 400, 3, 60f, 30, doubleArray, true); // weight
	}

	/**
	 * Simple text.
	 *
	 * @param text     the text
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void simpleText(String text, String fileName) throws IOException {
		try (FileWriter fileWriter = new FileWriter(fileName); PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.print(text);
		}
	}

	/**
	 * Write to file.
	 *
	 * @param filename the filename
	 * @param matrix   the matrix
	 */
	public static void writeMatrixToFile(String filename, DoubleMatrix matrix) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			for (int i = 0; i < matrix.rows; i++) {
				// write the line number bw.write(i + "\t");
				for (int j = 0; j < matrix.getRow(i).columns; j++) {
					// write the data
					bw.write(Double.toString(matrix.get(i, j)) + EnumSeparators.TAB.getSeparator());
					// write the class in the end of the row
					if ((filename.contains("MATRIXX") || filename.contains("data") || filename.contains("Data"))
							&& j == matrix.getRow(i).columns - 1) {
						if (i < 200) {
							bw.write("chinese");
						} else {
							bw.write("greek");
						}
					}
				}
				// change line
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			// nothing to do
		} finally {
			// nothing to do
		}
	}

	/**
	 * Write results. Prepare results to be written in file
	 *
	 * @param nameOfMethod    the name of method
	 * @param fileSuffix      the file suffix
	 * @param matrices        the matrices
	 * @param measureDistance the measure distance
	 */
	//
	public static void writeResults(String nameOfMethod, String fileSuffix, Object[] matrices, Double measureDistance) {
		if (matrices != null) {
			for (int i = 0; i < matrices.length; i++) {
				switch (i) {
				case 0:
					Utils.writeMatrixToFile(Utils.makeFilePath("matrixX".toUpperCase(), nameOfMethod, fileSuffix),
							(DoubleMatrix) matrices[i]);
					break;
				case 1:
					Utils.writeMatrixToFile(Utils.makeFilePath("matrixW".toUpperCase(), nameOfMethod, fileSuffix),
							(DoubleMatrix) matrices[i]);
					break;
				case 2:
					Utils.writeMatrixToFile(Utils.makeFilePath("matrixH".toUpperCase(), nameOfMethod, fileSuffix),
							(DoubleMatrix) matrices[i]);
					break;
				case 3:
					Utils.writeMatrixToFile(
							Utils.makeFilePath("matrixRegressionH".toUpperCase(), nameOfMethod, fileSuffix),
							(DoubleMatrix) matrices[i]);
					break;
				default:
					// nothing to do
					break;
				}
			}
		}
		if (measureDistance != null) {
			String measureName = "measureDistance";
			if (matrices == null) {
				measureName = "measureDistanceAfterRegression";
			}
			try {
				simpleText(measureDistance.toString(),
						Utils.makeFilePath(measureName.toUpperCase(), nameOfMethod, fileSuffix));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Instantiate the list of lists. THe lists will be as much as the listSize
	 * parameter indicates.
	 *
	 * @param listSize
	 * @return
	 */
	public static List<List<String>> instantiateListOfStringLists(int listSize) {
		List<List<String>> records = new ArrayList<>();
		for (int i = 0; i < listSize; i++) {
			records.add(new ArrayList<>());
		}
		return records;
	}

	/**
	 * Removes all files in a directory but no subdirectories and the files in them.
	 *
	 * @param pathOfFiles
	 */
	public static void removeFiles(String pathOfFiles) {
		final File folder = new File(pathOfFiles);
		// get the files but not the files in subdirectories
		for (final File file : folder.listFiles()) {
			try {
				if (!file.isDirectory()) {
					Files.delete(file.toPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.log(Level.INFO, e.getMessage());
			}
		}
	}

	/**
	 * Checks whether directory is empty.
	 *
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static boolean isDirEmpty(final Path directory) throws IOException {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
			return !dirStream.iterator().hasNext();
		}
	}

	/**
	 * Write eigens to file.
	 *
	 * @param fileName             the file name
	 * @param eigenValueAndVectors the eigen value and vectors
	 */
	public static void writeEigensToFile(String fileName, Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			bw.write("Number of eigenValues: " + eigenValueAndVectors._1.length + System.lineSeparator());
			bw.write("Number of eigenVectors (rows): " + eigenValueAndVectors._2.rows() + System.lineSeparator());
			bw.write("Number of eigenVectors (columns): " + eigenValueAndVectors._2.columns() + System.lineSeparator());
			bw.newLine();
			for (int i = 0; i < eigenValueAndVectors._1.length; i++) {
				bw.write(String.valueOf(eigenValueAndVectors._1[i]) + System.lineSeparator());
			}
			for (int i = 0; i < eigenValueAndVectors._2.columns(); i++) {
				bw.write(String.valueOf(eigenValueAndVectors._2.viewColumn(i)));
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// nothing to do
		}
	}

	/**
	 * Adds temporarily an additional path to java.library.path. It may be useful in
	 * the future.
	 *
	 * @param tmpDirName
	 */
	@SuppressWarnings("unused")
	private static void addLibsToJavaLibraryPath(final String tmpDirName) {
		try {
			String JAVA_LIBRARY_PATH = "java.library.path";
			String SYS_PATHS = "sys_paths";
			String paths = System.getProperty(JAVA_LIBRARY_PATH);
			System.setProperty(JAVA_LIBRARY_PATH, paths + ";" + tmpDirName);
			/* Optionally add these two lines */
			System.setProperty("jna.library.path", tmpDirName);
			System.setProperty("jni.library.path", tmpDirName);
			final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param startTime
	 * @param endTime
	 */
	public static String printExecutionTime(long startTime, long endTime) {
		long timeNanoSecs = endTime - startTime;
		long timeMilliSecs = TimeUnit.NANOSECONDS.toMillis(timeNanoSecs);
		long timeSecs = TimeUnit.NANOSECONDS.toSeconds(timeNanoSecs);
		long timeMin = TimeUnit.NANOSECONDS.toMinutes(timeNanoSecs);
		long timeHour = TimeUnit.NANOSECONDS.toHours(timeNanoSecs);
		StringBuilder sb = new StringBuilder("\nExecution Time: ");
		System.out.print("\nExecution Time: ");
		if (timeHour > 0) {
			System.out.print(timeHour + " Hours, ");
			sb.append(timeHour + " Hours, ");
		}
		if (timeMin > 0) {
			System.out.print(timeMin % 60 + " Minutes, ");
			sb.append(timeMin % 60 + " Minutes, ");
		}
		if (timeSecs > 0) {
			System.out.print(timeSecs % 60 + " Seconds, ");
			sb.append(timeSecs % 60 + " Seconds, ");
		}
		if (timeMilliSecs > 0) {
			System.out.print(timeMilliSecs % 1E+3 + " MicroSeconds, ");
			sb.append(timeMilliSecs % 1E+3 + " MicroSeconds, ");
		}
		if (timeNanoSecs > 0) {
			System.out.print(timeNanoSecs % 1E+6 + " NanoSeconds");
			sb.append(timeNanoSecs % 1E+6 + " NanoSeconds");
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}
}

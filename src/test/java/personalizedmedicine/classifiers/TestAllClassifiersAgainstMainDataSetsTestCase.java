package java.personalizedmedicine.classifiers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.personalizedmedicine.helpful_classes.AppLogger;
import java.personalizedmedicine.helpful_classes.Constants;
import java.personalizedmedicine.utilpackage.WekaUtils;
import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

/**
 * The Class TestAllClassifiersAgainstMainDataSetsTestCase.
 */
public class TestAllClassifiersAgainstMainDataSetsTestCase {

	/** The class name. */
	private final String className = Constants.classRealName;

	/** The num folds. */
	private int numFolds = 10;

	/** The random. */
	private int random = 1;

	/** The logger test. */
	private static AppLogger loggerTest = null;

	/** The file log map. */
	private Map<String, String> fileLogMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1226004314628974778L;

		{
//			put(Constants.completeFileName, "D:\\Bioscience\\results\\Original Dataset\\");
//			put(Constants.methFileName, "D:\\Bioscience\\results\\Methylation Dataset\\");
//			put(Constants.mRNAFileName, "D:\\Bioscience\\results\\mRNA Dataset\\");
//			put(Constants.miRNAFileName, "D:\\Bioscience\\results\\miRNA Dataset\\");
//			put(Constants.dataset10EMPCAFileName, "D:\\Bioscience\\results\\Original_EMPCA Dataset\\");
//			put(Constants.dataset20EMPCAFileName, "D:\\Bioscience\\results\\Original_EMPCA Dataset\\");
//			put(Constants.dataset50EMPCAFileName, "D:\\Bioscience\\results\\Original_EMPCA Dataset\\");
//			put(Constants.dataset100EMPCAFileName, "D:\\Bioscience\\results\\Original_EMPCA Dataset\\");
			put(Constants.dataset10IsomapCsvFileName, "D:\\Bioscience\\results\\Original_Isomap Dataset\\");
			put(Constants.dataset20IsomapCsvFileName, "D:\\Bioscience\\results\\Original_Isomap Dataset\\");
			put(Constants.dataset50IsomapCsvFileName, "D:\\Bioscience\\results\\Original_Isomap Dataset\\");
			put(Constants.dataset100IsomapCsvFileName, "D:\\Bioscience\\results\\Original_Isomap Dataset\\");
		}
	};;

	/**
	 * Test NB All.
	 */
	@Test
	@DisplayName("Naive Bayes All")
	public void testNaiveBayesAll() {
		String logPath = "NAIVE_BAYES\\log\\";
		String suffix = "_naive_bayes.log";
		fileLogMap.forEach((fileName, logMapPath) -> {
			AppLogger.setAppLoggerNull();
			Constants.loggerPath = logMapPath + logPath + logFileName(fileName) + suffix;
			System.out.println(Constants.loggerPath);
			loggerTest = AppLogger.getInstance();
			Constants.logger = null;
			Constants.logger = loggerTest;
			try {
				File level2File = new File(fileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				String[] configs = new String[] { "", "-K", "-D" };
				int count = 0;
				for (String option : configs) {
					String[] options = new String[] { option };
					Constants.logger.getLogger().log(Level.INFO, "SAVE TEST INFO NAME: " + "Configuration_" + count);
					AbstractClassifier classifier = WekaUtils.getClassifier(Constants.NAIVE_BAYES, originalDataset,
							options);
					WekaUtils.crossValidationAction(Constants.NAIVE_BAYES, classifier, numFolds, random);
					count++;
				}
			} catch (IOException e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		});
	}

	/**
	 * Test part all.
	 */
	@Test
	@DisplayName("Part All")
	public void testPartAll() {
		// -M 2 -C 0.25 -Q 1
		String[] binarySplits = new String[] { " -B ", "" };
		String[] useMDLCorrection = new String[] { " -J ", "" };
		String[] unpruned = new String[] { " -U ", "" };
		String logPath = "PART\\log\\";
		String suffix = "_part.log";
		fileLogMap.forEach((fileName, logMapPath) -> {
			AppLogger.setAppLoggerNull();
			Constants.loggerPath = logMapPath + logPath + logFileName(fileName) + suffix;
			System.out.println(Constants.loggerPath);
			loggerTest = AppLogger.getInstance();
			Constants.logger = null;
			Constants.logger = loggerTest;
			try {
				File level2File = new File(fileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				int count = 0;
				// confidenceFactor must be greater than 0 and less than 1
				for (double confidenceFactor = 0.1; confidenceFactor <= 0.5; confidenceFactor += 0.1) { // -C
					for (int minNumObj = 1; minNumObj <= 25; minNumObj++) { // -M
						for (String mdl : useMDLCorrection) {
							for (String pruningIsPerformed : unpruned) {
								for (String bSplit : binarySplits) {
									StringJoiner joiner = new StringJoiner(" ");
									joiner.add(bSplit).add(pruningIsPerformed).add(mdl).add("-M")
											.add(String.valueOf(minNumObj)).add("-C")
											.add(String.valueOf(confidenceFactor));
									Constants.logger.getLogger().log(Level.INFO,
											"SAVE TEST INFO NAME: " + "Configuration_" + count);
									String[] options = weka.core.Utils.splitOptions(joiner.toString());
									count++;
									AbstractClassifier classifier = WekaUtils.getClassifier(Constants.PART,
											originalDataset, options);
									WekaUtils.crossValidationAction(Constants.PART, classifier, numFolds, random);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		});
	}

	/**
	 * Test jrip all.
	 */
	@Test
	@DisplayName("Jrip All")
	public void testJripAll() {
		// Default: "-F 3 -N 2.0 -O 2 -S 1"
		// in this test always use pruning and always check error rate>0.5
		// number of configurations should be around 200
		String logPath = "JRIP\\log\\";
		String suffix = "_jrip.log";
		fileLogMap.forEach((fileName, logMapPath) -> {
			AppLogger.setAppLoggerNull();
			Constants.loggerPath = logMapPath + logPath + logFileName(fileName) + suffix;
			System.out.println(Constants.loggerPath);
			loggerTest = AppLogger.getInstance();
			Constants.logger = null;
			Constants.logger = loggerTest;
			try {
				File level2File = new File(fileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				int count = 0;
				for (int optimization = 1; optimization <= 5; optimization++) {
					for (int folds = 1; folds <= 10; folds++) {
						for (double mWeight = 1; mWeight <= 4; mWeight += 1) {
							StringJoiner joiner = new StringJoiner(" ");
							String options = joiner.add("-O").add(String.valueOf(optimization)).add("-F")
									.add(String.valueOf(folds)).add("-N").add(String.valueOf(mWeight)).toString();
							count++;
							Constants.logger.getLogger().log(Level.INFO,
									"SAVE TEST INFO NAME: " + "Configuration_" + count);
							AbstractClassifier classifier = WekaUtils.getClassifier(Constants.JRIP, originalDataset,
									weka.core.Utils.splitOptions(options));
							WekaUtils.crossValidationAction(Constants.JRIP, classifier, numFolds, random);
						}
					}
				}
			} catch (Exception e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		});
	}

	/**
	 * Test ibk all.
	 */
	@Test
	public void testIbkAll() {
		// this test has only important configurations that have meaning to use.
		// weka.classifiers.lazy.IBk -K 1 -W 0 -A
		// "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R
		// first-last\""
		String logPath = "IBK\\log\\";
		String suffix = "_ibk.log";
		String[] nearestNeighbourSearchAlgorithm = new String[] { "weka.core.neighboursearch.LinearNNSearch " }; // -A
		String[] distanceFunction = new String[] { "weka.core.EuclideanDistance -R first-last ",
				"weka.core.ManhattanDistance -R first-last ", "weka.core.ChebyshevDistance -R first-last " }; // -A
		String[] distanceWeighting = new String[] { " -I", " -F", "" };
		fileLogMap.forEach((fileName, logMapPath) -> {
			AppLogger.setAppLoggerNull();
			Constants.loggerPath = logMapPath + logPath + logFileName(fileName) + suffix;
			System.out.println(Constants.loggerPath);
			loggerTest = AppLogger.getInstance();
			Constants.logger = null;
			Constants.logger = loggerTest;
			try {
				File level2File = new File(fileName);
				Instances originalDataset = WekaUtils.getOriginalData(level2File, className);
				int count = 0;
				for (String nNAlgorithm : nearestNeighbourSearchAlgorithm) {
					for (String dFunction : distanceFunction) {
						{
							// knn must be > 0
							for (int knn = 1; knn <= 25; knn++) // -K
							{
								for (String dWeight : distanceWeighting) {
									StringJoiner joiner = new StringJoiner(" ");
									joiner.add(dWeight).add("-K") // .add(cross)
											.add(String.valueOf(knn)).add("-W").add(String.valueOf(0)).add("-A")
											.add("\"").add(nNAlgorithm).add("-A").add("\\\"").add(dFunction).add("\\\"")
											.add("\"");
									String[] options = weka.core.Utils.splitOptions(joiner.toString());
									count++;
									Constants.logger.getLogger().log(Level.INFO,
											"SAVE TEST INFO NAME: " + "Configuration_" + count);
									AbstractClassifier classifier = WekaUtils.getClassifier(Constants.IBK,
											originalDataset, options);
									WekaUtils.crossValidationAction(Constants.IBK, classifier, numFolds, random);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				Assert.assertFalse(e.getMessage(), true);
			}
		});
	}

//	/**
//	 * Test neural all.
//	 */
//	@Test
//	public void testNeuralAll() {
//		AppLogger.setAppLoggerNull();
//		Constants.loggerPath = "D:\\Bioscience\\results\\Original Dataset\\NEURAL_CLASSIFIER\\log\\neural.log";
//		loggerTest = AppLogger.getInstance();
//		System.out.println("testNeuralAll");
//		for (int i = 0; i < 200; i++) {
//			loggerTest.getLogger().log(Level.INFO, "SAVE TEST INFO NAME: " + "Configuration_" + i);
//		}
//	}

	/**
	 * Log file name.
	 *
	 * @param fileName the file name
	 * @return the string
	 */
	private String logFileName(String fileName) {
		switch (fileName) {
		case Constants.completeFileName:
			return "originalFile";
		case Constants.methFileName:
			return "methylationFile";
		case Constants.mRNAFileName:
			return "mRNAFile";
		case Constants.miRNAFileName:
			return "miRNAFile";
		case Constants.dataset10EMPCAFileName:
			return "10empcaData";
		case Constants.dataset20EMPCAFileName:
			return "20empcaData";
		case Constants.dataset50EMPCAFileName:
			return "50empcaData";
		case Constants.dataset100EMPCAFileName:
			return "100empcaData";
		case Constants.dataset10IsomapCsvFileName:
			return "10IsoData";
		case Constants.dataset20IsomapCsvFileName:
			return "20IsoData";
		case Constants.dataset50IsomapCsvFileName:
			return "50IsoData";
		case Constants.dataset100IsomapCsvFileName:
			return "100IsoData";
		default:
			throw new IllegalArgumentException("File name is not recognized.");
		}
	}
}

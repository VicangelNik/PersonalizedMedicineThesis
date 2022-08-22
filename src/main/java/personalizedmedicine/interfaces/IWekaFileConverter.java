package personalizedmedicine.interfaces;

import java.io.IOException;

import weka.core.Instances;

/**
 * The Interface WekaFileConverter.
 */
public interface IWekaFileConverter {

	/**
	 * Convert csv to arff.
	 *
	 * @param data         the data
	 * @param filePathName the file path name to write the data.
	 */
	public void arffSaver(Instances data, String filePathName) throws IOException;

	/**
	 * Convert arff to csv.
	 *
	 * @param data         the data
	 * @param filePathName the file path name to write the data.
	 * @throws IOException
	 */
	public void csvSaver(Instances data, String filePathName) throws IOException;
}

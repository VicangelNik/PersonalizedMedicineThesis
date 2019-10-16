package weka.api.library;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 * The Class LoadCsv.
 */
public class LoadCsv {

	/** The file. */
	private File file;

	/** The csv data. */
	private Instances csvData;

	/**
	 * Instantiates a new load csv.
	 *
	 * @param file      the file
	 * @param separator the separator
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LoadCsv(File file, String separator) throws IOException {
		this.setFile(file);
		// load file
		CSVLoader loader = new CSVLoader();
		loader.setFieldSeparator(separator);
		loader.setSource(this.file);
		this.setCsvData(loader.getDataSet());
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Gets the csv data.
	 *
	 * @return the csv data
	 */
	public Instances getCsvData() {
		return csvData;
	}

	/**
	 * Sets the csv data.
	 *
	 * @param csvData the new csv data
	 */
	public void setCsvData(Instances csvData) {
		this.csvData = csvData;
	}
}

/*
 *
 */
package weka.api.library;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadCsv.
 */
public class LoadCsv extends CSVLoader {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1655811640781388320L;

	/** The file. */
	private File file;

	/** The class index. */
	private int classIndex;

	/**
	 * Instantiates a new load csv.
	 *
	 * @param file       the file
	 * @param classIndex the class index
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LoadCsv(File file, int classIndex) throws IOException {
		this.file = file;
		this.setFile(file);
		this.classIndex = classIndex;
		setStructure();
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
	 * Sets the structure.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setStructure() throws IOException {
		Instances csvStructure;
		csvStructure = this.getStructure();
		csvStructure.setClassIndex(this.classIndex);
	}
}

/*
 *
 */
package weka.api.library;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadArff.
 */
public class LoadArff extends ArffLoader {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2159246798094863226L;

	/** The file. */
	private File file;

	/** The class index. */
	private int classIndex;

	/**
	 * Instantiates a new load arff.
	 *
	 * @param file       the file
	 * @param classIndex the class index
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LoadArff(File file, int classIndex) throws IOException {
		this.file = file;
		this.setFile(file);
		this.classIndex = classIndex;
	}

	/**
	 * Instantiates a new load arff.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LoadArff(File file) throws IOException {
		this.file = file;
		this.setFile(file);
		this.classIndex = 0;
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
	 * Sets the class index.
	 *
	 * @param classIndex the new class index
	 */
	public void setClassIndex(int classIndex)
	{
		this.classIndex = classIndex;
	}

	/*
	 * (non-Javadoc) Method is overrided for setting the class index
	 *
	 * @see weka.core.converters.ArffLoader#getStructure()
	 */
	@Override
	public Instances getStructure() throws IOException {

		if (m_structure == null) {
			if (m_sourceReader == null) {
				throw new IOException("No source has been specified");
			}

			try {
				m_ArffReader = new ArffReader(m_sourceReader, 1, getRetrieval() == BATCH);
				m_ArffReader.setRetainStringValues(getRetainStringVals());
				m_structure = m_ArffReader.getStructure();
			} catch (Exception ex) {
				throw new IOException("Unable to determine structure as arff (Reason: " + ex.toString() + ").");
			}
		}
		// set the class index. The structure knows which is the feature for
		// classification.
		m_structure.setClassIndex(classIndex);
		return new Instances(m_structure, 0);
	}
}

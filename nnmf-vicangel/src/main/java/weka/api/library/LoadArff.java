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
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	@Override
	public Instances getStructure() throws IOException {

		if (m_structure == null) {
			if (m_sourceReader == null) {
				throw new IOException("No source has been specified");
			}

			try {
				m_ArffReader = new ArffReader(m_sourceReader, 1, (getRetrieval() == BATCH));
				m_ArffReader.setRetainStringValues(getRetainStringVals());
				m_structure = m_ArffReader.getStructure();
			} catch (Exception ex) {
				throw new IOException("Unable to determine structure as arff (Reason: " + ex.toString() + ").");
			}
		}
		m_structure.setClassIndex(this.classIndex);
		return new Instances(m_structure, 0);
	}
}

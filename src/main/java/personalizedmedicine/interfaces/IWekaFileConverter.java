package personalizedmedicine.interfaces;

import weka.core.Instances;

import java.io.IOException;

public interface IWekaFileConverter {

    /**
     * Convert csv to arff.
     *
     * @param data         the data
     * @param filePathName the file path name to write the data.
     */
    void arffSaver(Instances data, String filePathName) throws IOException;

    /**
     * Convert arff to csv.
     *
     * @param data         the data
     * @param filePathName the file path name to write the data.
     * @throws IOException
     */
    void csvSaver(Instances data, String filePathName) throws IOException;
}

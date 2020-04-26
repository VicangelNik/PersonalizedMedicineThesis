package weka.api.library;

import java.io.File;
import java.io.IOException;

import interfaces.IWekaFileConverter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;

/**
 * The Class WekaFileConverterImpl.
 */
public class WekaFileConverter implements IWekaFileConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.WekaFileConverter#arffSaver(weka.core.Instances,
	 * java.lang.String)
	 */
	@Override
	public void arffSaver(Instances data, String filePathName) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(filePathName));
		saver.setMaxDecimalPlaces(20);
		saver.writeBatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.WekaFileConverter#convertArffToCsv(weka.core.Instances,
	 * java.lang.String)
	 */
	@Override
	public void csvSaver(Instances data, String filePathName) throws IOException {
		CSVSaver saver = new CSVSaver();
		saver.setInstances(data);
		saver.setFile(new File(filePathName));
		saver.setMaxDecimalPlaces(20);
		saver.writeBatch();
	}
}

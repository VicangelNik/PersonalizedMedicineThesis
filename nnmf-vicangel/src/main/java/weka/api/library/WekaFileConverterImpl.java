package weka.api.library;

import java.io.File;
import java.io.IOException;

import interfaces.WekaFileConverter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class WekaFileConverterImpl implements WekaFileConverter {

	@Override
	public void convertCsvToArff(Instances data, String filePathName) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(filePathName));
		saver.setMaxDecimalPlaces(20);
		saver.writeBatch();
	}

	@Override
	public void convertArffToCsv(Instances data, String filePathName) {
		// TODO Auto-generated method stub

	}

}

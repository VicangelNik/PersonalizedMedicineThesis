package weka.api.library;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class LoadCsv {

	private File file;

	private Instances csvData;

	public LoadCsv(File file) throws IOException {
		this.setFile(file);
		// load file
		CSVLoader loader = new CSVLoader();
		loader.setSource(this.file);
		this.setCsvData(loader.getDataSet());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Instances getCsvData() {
		return csvData;
	}

	public void setCsvData(Instances csvData) {
		this.csvData = csvData;
	}

}

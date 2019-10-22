package test.weka.api;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.Constants;
import helpful_classes.EnumSeparators;
import weka.api.library.LoadCsv;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Resample;

public class CreateTrainData {
	@Test
	public void createTrainingData() {
		try {
			final File csvFile = new File(Constants.SRC_MAIN_RESOURCES_PATH + "bladderCancer.csv");
			LoadCsv loadCsv = new LoadCsv(csvFile, EnumSeparators.TAB.getSeparator());
			Resample resample = new Resample();
			resample.setInvertSelection(false);
			resample.setNoReplacement(true);
			resample.setRandomSeed(1);
			resample.setSampleSizePercent(1);
			resample.setInputFormat(loadCsv.getCsvData());
			Instances trainData = Filter.useFilter(loadCsv.getCsvData(), resample);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(trainData);
			saver.setFile(new File(Constants.SRC_MAIN_RESOURCES_PATH + "trainData.arf"));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}

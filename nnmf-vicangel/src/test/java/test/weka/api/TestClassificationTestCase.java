package test.weka.api;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import helpful_classes.ClassifierSelectionImpl;
import helpful_classes.Constants;
import interfaces.ClassifierSelection;
import weka.api.library.LoadArff;
import weka.classifiers.AbstractClassifier;

public class TestClassificationTestCase {

	@Test
	public void testNaiveBayesClassification() {
		File file = new File(Constants.WEKA_FILES + "iris.arff");
		try {
			LoadArff loader = new LoadArff(file, 4);
			Assert.assertEquals("The excpected class should be: ", 4, loader.getStructure().classIndex());
			ClassifierSelection classifierSelection = new ClassifierSelectionImpl();
			AbstractClassifier classifier = classifierSelection.selectClassifier(Constants.NAIVE_BAYES, loader);
			System.out.println(classifier.getCapabilities());
			System.out.println(classifier);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}

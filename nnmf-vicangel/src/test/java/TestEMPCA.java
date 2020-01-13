import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.junit.Test;

import helpful_classes.Constants;
import scala.Tuple2;
import weka.api.library.LoadArff;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/*
 * The standard Scala backend is a Java VM. Scala classes are Java classes, and vice versa.
 * You can call the methods of either language from methods in the other one.
 * You can extend Java classes in Scala, and vice versa.
 * The main limitation is that some Scala features do not have equivalents in Java, for example traits.
 */

public class TestEMPCA {
	@Test
	public void test() throws IOException {

		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndControlProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Instances data = arffLoader.getDataSet();
		@SuppressWarnings("unchecked")
		List<Tuple2<Integer, Double>>[] javaList = new ArrayList[data.numInstances()];
		for (int i = 0; i < data.numInstances(); i++) {
			javaList[i] = new ArrayList<Tuple2<Integer, Double>>();
		}
		int insCount = 0;
		Enumeration<Instance> instEnumeration = data.enumerateInstances();
		Enumeration<Attribute> attEnumeration = data.enumerateAttributes();
		while (instEnumeration.hasMoreElements()) {
			Instance current = instEnumeration.nextElement();
			while (attEnumeration.hasMoreElements()) {
				Attribute attribute = attEnumeration.nextElement();
				int attIndex = attribute.index();
				javaList[insCount].add(new Tuple2<Integer, Double>(attIndex, current.value(attIndex)));
			}
			insCount++;
		}
		@SuppressWarnings("unchecked")
		scala.collection.immutable.List<Tuple2<Object, Object>>[] scalaList = (scala.collection.immutable.List<Tuple2<Object, Object>>[]) javaList;

		// EMPCA empca = new EMPCA(scalaList, 50);
	}

}

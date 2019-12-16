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

/*
 * The standard Scala backend is a Java VM. Scala classes are Java classes, and vice versa. 
 * You can call the methods of either language from methods in the other one. 
 * You can extend Java classes in Scala, and vice versa. 
 * The main limitation is that some Scala features do not have equivalents in Java, for example traits.
 */

public class TestEMPCA {
	@Test
	public void test() throws IOException {

		File level2File = new File(Constants.SRC_MAIN_RESOURCES_PATH + "PatientAndCïntrolProcessedLevelTwo.arff");
		LoadArff arffLoader = new LoadArff(level2File);
		arffLoader.setClassIndex(arffLoader.getStructure().attribute("SampleStatus").index());
		Instance current;
		int count = 0;

		// ArrayList<List<Tuple2<Integer,Double>>>[] genList = new
		// ArrayList[arffLoader.getDataSet().numInstances()];
		List<Tuple2<Integer, Double>>[] list = new ArrayList[arffLoader.getDataSet().numInstances()
				* arffLoader.getDataSet().numAttributes()];
		while ((current = arffLoader.getNextInstance(arffLoader.getStructure())) != null) {

			Enumeration<Attribute> enumeration = current.enumerateAttributes();
			int count1 = 0;
			while (enumeration.hasMoreElements()) {
				Attribute attribute = enumeration.nextElement();
				int attIndex = attribute.index();
				list[count] = new ArrayList<>();
				list[count1].add(new Tuple2<Integer, Double>(attIndex, current.value(attIndex)));
			}
			count++;
		}
		;
		// JavaConversions.asScalaSet(list).toList();
		// EMPCA empca = new EMPCA(list, 50);
	}
}

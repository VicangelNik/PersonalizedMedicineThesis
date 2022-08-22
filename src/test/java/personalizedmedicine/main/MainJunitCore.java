package personalizedmedicine.main;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.JUnitCore;

import personalizedmedicine.utiltestpackage.RingListener;

/**
 * The Class MainJunitCore.
 */
public class MainJunitCore {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String... args) {
		JUnitCore core = new JUnitCore();
		core.addListener(new RingListener());
		List<Class<?>> testCases = new ArrayList<>();
		testCases.add(TestListenerTestCase.class);
		testCases.forEach(classObject -> core.run(classObject));
	}

}

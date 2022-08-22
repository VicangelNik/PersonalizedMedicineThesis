import weka.options.OptionsGeneralBuilder;

/*
 *
 */

public class Main {

	public static void main(String[] args) {
		String options = new OptionsGeneralBuilder().withDebug(true).withNoCheckCapabillities(true).build();
		System.out.println(options);
	}
}

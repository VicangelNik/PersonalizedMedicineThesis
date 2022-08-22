package personalizedmedicine.weka.options;

import java.util.StringJoiner;

// builder design pattern
/**
 * The Class OptionsGeneralBuilder.
 */
public class OptionsGeneralBuilder extends OptionsBuilder {

	/** The debug. */
	private String debug = "";

	/** The no check capabillities. */
	private String noCheckCapabillities = "";

	/**
	 * With debug.
	 *
	 * @param debug the debug
	 * @return the options general builder
	 */
	public OptionsGeneralBuilder withDebug(boolean debug) {
		if (debug) {
			this.debug = OptionsParamtersEnum.DEBUG.getParameter();
		}
		return this;
	}

	/**
	 * With no check capabillities.
	 *
	 * @param doNotCheckCapabillities the do not check capabillities
	 * @return the options general builder
	 */
	public OptionsGeneralBuilder withNoCheckCapabillities(boolean doNotCheckCapabillities) {
		if (doNotCheckCapabillities) {
			noCheckCapabillities = OptionsParamtersEnum.DO_NOT_CHECK_CAPABILLITIES.getParameter();
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see weka.options.OptionsBuilder#build()
	 */
	@Override
	public String build() {
		StringJoiner joiner = new StringJoiner(" ");
		joiner.add(debug);
		joiner.add(noCheckCapabillities);
		return joiner.toString();
	}
}

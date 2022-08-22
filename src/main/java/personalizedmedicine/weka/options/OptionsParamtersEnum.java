package personalizedmedicine.weka.options;

/**
 * The Enum OptionsParamtersEnum.
 */
public enum OptionsParamtersEnum {

	/** The debug. */
	DEBUG("-output-debug-info"),
	/** The do not check capabillities. */
	DO_NOT_CHECK_CAPABILLITIES("-do-not-check-capabilities"),
	/** The batch size. */
	BATCH_SIZE("-batch-size"),
	/** The num decimal places. */
	NUM_DECIMAL_PLACES("-num-decimal-places"),
	/** The seed. */
	SEED("-S"),
	/** The e parameter. */
	E_PARAMETER("-E"),
	/** The f parameter. */
	F_PARAMETER("-F"),
	/** The n parameter. */
	N_PARAMETER("-N"),
	/** The o parameter. */
	O_PARAMETER("-O"),
	/** The p parameter. */
	P_PARAMETER("-P");

	/** The parameter. */
	private String parameter;

	/**
	 * Instantiates a new options paramters enum.
	 *
	 * @param parameter the parameter
	 */
	OptionsParamtersEnum(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
}

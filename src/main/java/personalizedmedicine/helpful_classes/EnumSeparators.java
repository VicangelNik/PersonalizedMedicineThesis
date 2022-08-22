package java.personalizedmedicine.helpful_classes;

/**
 * The Enum Separators.
 */
public enum EnumSeparators {

	/** The tab. */
	TAB("\t"),
	/** The comma. */
	COMMA(",");

	/** The separator. */
	private final String separator;

	/**
	 * Instantiates a new enum separators.
	 *
	 * @param separator the separator
	 */
	EnumSeparators(String separator) {
		this.separator = separator;
	}

	/**
	 * Gets the separator.
	 *
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
}

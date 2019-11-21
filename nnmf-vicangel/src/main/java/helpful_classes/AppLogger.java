package helpful_classes;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class AppLogger.
 */
public class AppLogger {

	/** The logger. */
	private Logger logger;

	/**
	 * Instantiates a new app logger.
	 *
	 * @param title       the title
	 * @param fileToWrite the file to write
	 */
	public AppLogger(String title, String fileToWrite) {
		logger = Logger.getLogger(title);
		try {

			// This block configure the logger with handler and formatter
			FileHandler fh = new FileHandler(Constants.loggerPath + fileToWrite);
			// In project.log will be logged everything on the project
			FileHandler fh1 = new FileHandler(Constants.loggerPath + "projectLog.log");
			logger.addHandler(fh);
			logger.addHandler(fh1);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			// prevent to write to console output
			logger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger.
	 *
	 * @param logger the new logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}

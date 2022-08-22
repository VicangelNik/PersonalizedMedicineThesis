package java.personalizedmedicine.helpful_classes;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0
/**
 * The Class AppLogger.
 */
public class AppLogger {

	/** The app logger. */
	private static volatile AppLogger appLogger;

	/** The logger. */
	private static Logger logger;

	/** The file handler. */
	private static FileHandler fileHandler = null;

	/**
	 * Instantiates a new app logger.
	 */
	private AppLogger() {
		super();
		// Prevent form the reflection api.
		if (appLogger != null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
		makeLogger();
	}

	/**
	 * Gets the single instance of AppLogger.
	 *
	 * @return single instance of AppLogger
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AppLogger getInstance() {
		if (appLogger == null) { // if there is no instance available... create new one
			synchronized (AppLogger.class) { // Check for the second time.
				// if there is no instance available... create new one
				if (appLogger == null) {
//					try {
//						Files.createDirectories(Paths.get(Constants.loggerPath));
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					appLogger = new AppLogger();
				}
			}
		}
		return appLogger;
	}

	/**
	 * Make logger.
	 *
	 * @param title the title
	 */

	/**
	 * Instantiates a new app logger.
	 *
	 * @param title       the title
	 * @param fileToWrite the file to write
	 * @return
	 */
	private static void makeLogger() {
		logger = Logger.getLogger(Constants.loggerPath);
		try {
			// In project.log will be logged everything on the project
			FileHandler fh = new FileHandler(Constants.loggerPath, false);
			fileHandler = fh;
			logger.addHandler(fh);
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

	public static void setAppLoggerNull() {
		if (fileHandler != null) {
			fileHandler.flush();
			fileHandler.close();
			appLogger.getLogger().removeHandler(fileHandler);
			appLogger.getLogger().setLevel(Level.OFF);
		}
		appLogger = null;
	}
}

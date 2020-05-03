package dimensionality_reduction_methods;

import java.util.logging.Level;

import abstract_classes.DimensionalityReduction;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.IDimensionalityReductionSelection;
import weka.core.Instances;

/**
 * The Class DimensionalityReductionSelection.
 */
public class DimensionalityReductionChooser implements IDimensionalityReductionSelection {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.IDimensionalityReductionSelection#dimensionalityReductionSelector(
	 * java.lang.String, weka.core.Instances, boolean, java.lang.String[])
	 */
	@Override
	public Instances dimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options) {
		try {
			switch (selection) {
			case Constants.PCA: {
				DimensionalityReduction reductionMethod = new PCAWeka();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((PCAWeka) reductionMethod).dimReductionMethod(options);
			}
			case Constants.EMPCA: {
				DimensionalityReduction reductionMethod = new ExpectationMaximizationPCA();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((ExpectationMaximizationPCA) reductionMethod).dimReductionMethod(options);
			}
			case Constants.ISOMAP: {
				DimensionalityReduction reductionMethod = new IsomapSmile();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((IsomapSmile) reductionMethod).dimReductionMethod(options);
			}
			case Constants.LLE: {
				DimensionalityReduction reductionMethod = new LLESmile();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((LLESmile) reductionMethod).dimReductionMethod(options);
			}
			case Constants.AUTOENCODER_WEKA: {
				DimensionalityReduction reductionMethod = new AutoencoderWeka();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((AutoencoderWeka) reductionMethod).dimReductionMethod(options);
			}
			default: {
				throw new IllegalArgumentException("Invalid selection");
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.getLogger().log(Level.SEVERE, "DimensionalityReductionSelector in in error: {0}", e);
		}
		return null;
	}

	/**
	 * Sets the values to dimensionality reduction.
	 *
	 * @param reductionMethod the reduction method
	 * @param dataset         the dataset
	 * @param debug           the debug
	 */
	private static void setValuesToDimensionalityReduction(DimensionalityReduction reductionMethod, Instances dataset,
			boolean debug) {
		reductionMethod.setDataset(dataset);
		reductionMethod.setDebug(debug);
	}
}

package dimensionality_reduction_methods;

import java.util.logging.Level;

import abstract_classes.DimensionalityReduction;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.DimensionalityReductionSelectionInterface;
import weka.core.Instances;

/**
 * The Class DimensionalityReductionSelection.
 */
public class DimensionalityReductionSelection implements DimensionalityReductionSelectionInterface {

	/** The logger. */
	private static AppLogger logger = AppLogger.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.DimensionalityReductionSelectionInterface#
	 * DimensionalityReductionSelector(java.lang.String, weka.core.Instances,
	 * boolean, java.lang.String[])
	 */
	@Override
	public Instances DimensionalityReductionSelector(String selection, Instances dataset, boolean debug,
			String[] options) {
		try {
			DimensionalityReduction reductionMethod;
			switch (selection) {
			case Constants.PCA: {
				reductionMethod = new PrincipalComponentAnalysisWeka();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((PrincipalComponentAnalysisWeka) reductionMethod).dimReductionMethod(options);
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
	private void setValuesToDimensionalityReduction(DimensionalityReduction reductionMethod, Instances dataset,
			boolean debug) {
		reductionMethod.setDataset(dataset);
		reductionMethod.setDebug(debug);
	}
}

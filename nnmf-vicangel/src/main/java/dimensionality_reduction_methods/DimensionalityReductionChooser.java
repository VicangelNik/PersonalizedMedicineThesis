package dimensionality_reduction_methods;

import java.util.logging.Level;

import abstract_classes.DimensionalityReduction;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.DimensionalityReductionSelection;
import smile.manifold.IsoMap;
import smile.manifold.LLE;
import utilpackage.TransformToFromWeka;
import utilpackage.WekaUtils;
import weka.core.Instances;

/**
 * The Class DimensionalityReductionSelection.
 */
public class DimensionalityReductionChooser implements DimensionalityReductionSelection {

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
				return doIsomap(dataset, options);
			}
			case Constants.LLE: {
				return doLLE(dataset, options);
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
	 * Do isomap.
	 *
	 * @param originalDataset the original dataset
	 * @param options         the options
	 * @return the instances
	 */
	private Instances doIsomap(Instances originalDataset, String[] options) {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		// third option should be whether C-Isomap or standard algorithm
		// the last 2 options will be always the dataset name and the name of the class
		double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
		IsoMap myIsomap = new IsoMap(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]),
				Boolean.parseBoolean(options[2]));
		double[][] coordinates = myIsomap.getCoordinates();
		return TransformToFromWeka.manifoldsToWeka(coordinates, options[3],
				WekaUtils.getDatasetClassValues(originalDataset), options[4]);
	}

	/**
	 * Do LLE.
	 *
	 * @param originalDataset the original dataset
	 * @param options         the options
	 * @return the instances
	 */
	private Instances doLLE(Instances originalDataset, String[] options) {
		// first option should be the number of expected dimensions
		// second option should be the k nearest neighbors
		double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
		LLE lle = new LLE(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]));
		double[][] coordinates = lle.getCoordinates();
		return TransformToFromWeka.manifoldsToWeka(coordinates, "lleDataset",
				WekaUtils.getDatasetClassValues(originalDataset), "class");
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

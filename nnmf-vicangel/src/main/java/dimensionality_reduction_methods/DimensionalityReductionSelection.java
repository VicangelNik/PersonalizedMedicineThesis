package dimensionality_reduction_methods;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;

import abstract_classes.DimensionalityReduction;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import helpful_classes.AppLogger;
import helpful_classes.Constants;
import interfaces.DimensionalityReductionSelectionInterface;
import scala.Tuple2;
import smile.manifold.IsoMap;
import smile.manifold.LLE;
import utilpackage.TransformToFromWeka;
import utilpackage.Utils;
import utilpackage.WekaUtils;
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
			switch (selection) {
			case Constants.PCA: {
				DimensionalityReduction reductionMethod = new PrincipalComponentAnalysisWeka();
				setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
				return ((PrincipalComponentAnalysisWeka) reductionMethod).dimReductionMethod(options);
			}
			case Constants.EMPCA: {
				return doEmpca(dataset, options);
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
		double data[][] = TransformToFromWeka.transformWekaToManifolds(originalDataset);
		IsoMap myIsomap = new IsoMap(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]),
				Boolean.parseBoolean(options[2]));
		double[][] coordinates = myIsomap.getCoordinates();
		return TransformToFromWeka.manifoldsToWeka(coordinates, "isomapDataset",
				WekaUtils.getDatasetClassValues(originalDataset), "class");
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
	private void setValuesToDimensionalityReduction(DimensionalityReduction reductionMethod, Instances dataset,
			boolean debug) {
		reductionMethod.setDataset(dataset);
		reductionMethod.setDebug(debug);
	}

	/**
	 * Do empca.
	 *
	 * @param originalDataset the original dataset
	 * @param options         the options
	 * @return the instances
	 */
	private static Instances doEmpca(Instances originalDataset, String[] options) {
		// first option should be the number of expecting principal components
		// second option should be the desirable number of em iterations
		List<ArrayList<Feature>> empcaInput = TransformToFromWeka.createEMPCAInputFromWekaV2(originalDataset);
		scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala
				.convert((ArrayList<ArrayList<Feature>>) empcaInput);
		// the second parameter is the number of the principal components we want as
		// result. Due to a hack it will return always minus 10 from the expected.
		EMPCA empca = new EMPCA(convertedToScalaList, Integer.parseInt(options[0]));
		DoubleMatrix2D c = empca.performEM(Integer.parseInt(options[1]));
		Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
		Utils.writeEigensToFile(Constants.loggerPath + "output.log", eigenValueAndVectors);
		// OUTPUT TO WEKA PART
		return TransformToFromWeka.eigensToWeka(eigenValueAndVectors._2, "empcaDataset",
				WekaUtils.getDatasetClassValues(originalDataset));
	}

}

package dimensionality_reduction_methods;

import abstract_classes.DimensionalityReduction;
import interfaces.DimensionalityReductionInterface;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.MLPAutoencoder;

/**
 * The Class AutoencoderWeka.
 */
public class AutoencoderWeka extends DimensionalityReduction implements DimensionalityReductionInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.DimensionalityReductionInterface#dimReductionMethod(java.lang.
	 * String[])
	 */
	@Override
	public Instances dimReductionMethod(String[] options) throws Exception {
		MLPAutoencoder mlpAutoencoder = new MLPAutoencoder();
		mlpAutoencoder.setOptions(options);
		mlpAutoencoder.setInputFormat(this.getDataset());
		mlpAutoencoder.setDebug(this.isDebug());
		return Filter.useFilter(this.getDataset(), mlpAutoencoder);
	}
}
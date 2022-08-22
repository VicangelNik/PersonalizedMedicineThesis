package java.personalizedmedicine.dimensionality_reduction_methods;

import java.personalizedmedicine.interfaces.IDimensionalityReduction;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.MLPAutoencoder;

/**
 * The Class AutoencoderWeka.
 */
public class AutoencoderWeka extends DimensionalityReduction implements IDimensionalityReduction {

	
	/* (non-Javadoc)
	 * @see interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
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

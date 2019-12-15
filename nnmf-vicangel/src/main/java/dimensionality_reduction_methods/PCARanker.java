package dimensionality_reduction_methods;

import abstract_classes.DimensionalityReduction;
import interfaces.DimensionalityReductionInterface;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Instances;

// TODO: Auto-generated Javadoc
/**
 * The Class PCARanker.
 */
public class PCARanker extends DimensionalityReduction implements DimensionalityReductionInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * interfaces.DimensionalityReductionInterface#dimReductionMethod(java.lang.
	 * String[])
	 */
	@Override
	public Instances dimReductionMethod(String[] options) throws Exception {
		// Performs a principal components analysis.
		PrincipalComponents pcaEvaluator = new PrincipalComponents();
		pcaEvaluator.setOptions(options);
		// Ranking the attributes.
		Ranker ranker = new Ranker();
		// Specify the number of attributes to select from the ranked list.
		ranker.setNumToSelect(3);
		AttributeSelection selector = new AttributeSelection();
		selector.setSearch(ranker);
		selector.setEvaluator(pcaEvaluator);
		selector.SelectAttributes(this.getDataset());

		// Transform data into eigenvector basis.
		return selector.reduceDimensionality(this.getDataset());
	}

}

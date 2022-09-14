package personalizedmedicine.dimensionality_reduction_methods;

import personalizedmedicine.interfaces.IDimensionalityReduction;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

/**
 * http://weka.sourceforge.net/doc.dev/weka/filters/unsupervised/attribute/PrincipalComponents.html
 */
public class PCAWeka extends DimensionalityReduction implements IDimensionalityReduction {

    /*
     * (non-Javadoc)
     *
     * @see
     * interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
     */
    @Override
    public Instances dimReductionMethod(String[] options) throws Exception {
        PrincipalComponents principalComponents = new PrincipalComponents();
        principalComponents.setOptions(options);
        principalComponents.setInputFormat(this.getDataset());
        principalComponents.setDebug(this.isDebug());
        return Filter.useFilter(this.getDataset(), principalComponents);
    }
}

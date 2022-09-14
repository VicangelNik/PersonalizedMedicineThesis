package personalizedmedicine.dimensionality_reduction_methods;

import personalizedmedicine.interfaces.IDimensionalityReduction;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.WekaUtils;
import smile.manifold.LLE;
import weka.core.Instances;

public class LLESmile extends DimensionalityReduction implements IDimensionalityReduction {

    /* (non-Javadoc)
     * @see interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
     */
    @Override
    public Instances dimReductionMethod(String[] options) {
        // first option should be the number of expected dimensions
        // second option should be the k nearest neighbors
        // the last 2 options will be always the dataset name and the name of the class
        final double[][] data = TransformToFromWeka.transformWekaToManifolds(this.getDataset());
        LLE lle = LLE.of(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]));
        return TransformToFromWeka.manifoldsToWeka(lle.coordinates, options[2],
                                                   WekaUtils.getDatasetClassValues(this.getDataset()), options[3]);
    }
}

package personalizedmedicine.dimensionality_reduction_methods;

import personalizedmedicine.interfaces.IDimensionalityReduction;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.WekaUtils;
import smile.manifold.IsoMap;
import weka.core.Instances;

public class IsomapSmile extends DimensionalityReduction implements IDimensionalityReduction {

    /*
     * (non-Javadoc)
     *
     * @see
     * interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
     */
    @Override
    public Instances dimReductionMethod(String[] options) {
        // first option should be the number of expected dimensions
        // second option should be the k nearest neighbors
        // third option should be whether C-Isomap or standard algorithm
        // the last 2 options will be always the dataset name and the name of the class
        final double[][] data = TransformToFromWeka.transformWekaToManifolds(this.getDataset());
        final IsoMap myIsomap = IsoMap.of(data, Integer.parseInt(options[0]), Integer.parseInt(options[1]),
                                          Boolean.parseBoolean(options[2]));
        return TransformToFromWeka.manifoldsToWeka(myIsomap.coordinates, options[3],
                                                   WekaUtils.getDatasetClassValues(this.getDataset()), options[4]);
    }
}

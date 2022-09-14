package personalizedmedicine.dimensionality_reduction_methods;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.interfaces.IDimensionalityReductionSelection;
import weka.core.Instances;

@Slf4j
public class DimensionalityReductionChooser implements IDimensionalityReductionSelection {

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
                    val reductionMethod = new PCAWeka();
                    setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
                    return reductionMethod.dimReductionMethod(options);
                }
                case Constants.EMPCA: {
                    val reductionMethod = new ExpectationMaximizationPCA();
                    setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
                    return reductionMethod.dimReductionMethod(options);
                }
                case Constants.ISOMAP: {
                    val reductionMethod = new IsomapSmile();
                    setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
                    return reductionMethod.dimReductionMethod(options);
                }
                case Constants.LLE: {
                    val reductionMethod = new LLESmile();
                    setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
                    return reductionMethod.dimReductionMethod(options);
                }
                case Constants.AUTOENCODER_WEKA: {
                    val reductionMethod = new AutoencoderWeka();
                    setValuesToDimensionalityReduction(reductionMethod, dataset, debug);
                    return reductionMethod.dimReductionMethod(options);
                }
                default: {
                    throw new IllegalArgumentException("Invalid selection");
                }
            }
        } catch (Exception e) {
            log.error("DimensionalityReductionSelector in in error: {0}", e);
            throw new RuntimeException(e);
        }
    }

    private static void setValuesToDimensionalityReduction(final DimensionalityReduction reductionMethod,
                                                           final Instances dataset, final boolean debug) {
        reductionMethod.setDataset(dataset);
        reductionMethod.setDebug(debug);
    }
}

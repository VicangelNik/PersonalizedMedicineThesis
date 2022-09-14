package personalizedmedicine.dimensionality_reduction_methods;

import lombok.Getter;
import lombok.Setter;
import weka.core.Instances;

@Getter
@Setter
public abstract class DimensionalityReduction {
    private Instances dataset;
    private boolean debug;
}

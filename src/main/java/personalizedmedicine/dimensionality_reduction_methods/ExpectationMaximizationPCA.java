package personalizedmedicine.dimensionality_reduction_methods;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import org.scify.EMPCA.EMPCA;
import org.scify.EMPCA.Feature;
import org.scify.EMPCA.JavaPCAInputToScala;
import personalizedmedicine.interfaces.IDimensionalityReduction;
import personalizedmedicine.utilpackage.TransformToFromWeka;
import personalizedmedicine.utilpackage.WekaUtils;
import scala.Tuple2;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class ExpectationMaximizationPCA extends DimensionalityReduction implements IDimensionalityReduction {

    /* (non-Javadoc)
     * @see interfaces.IDimensionalityReduction#dimReductionMethod(java.lang.String[])
     */
    @Override
    public Instances dimReductionMethod(String[] options) {
        // first option should be the number of expecting principal components
        // second option should be the desirable number of em iterations
        // the last 2 options will be always the dataset name and the name of the class
        List<ArrayList<Feature>> empcaInput = TransformToFromWeka.createEMPCAInputFromWekaV2(this.getDataset());
        scala.collection.immutable.List<Tuple2<Object, Object>>[] convertedToScalaList = JavaPCAInputToScala.convert(
                (ArrayList<ArrayList<Feature>>) empcaInput);
        // the second parameter is the number of the principal components we want as
        // result. Due to a hack it will return always minus 10 from the expected.
        EMPCA empca = new EMPCA(convertedToScalaList, Integer.parseInt(options[0]));
        DoubleMatrix2D c = empca.performEM(Integer.parseInt(options[1]));
        Tuple2<double[], DoubleMatrix2D> eigenValueAndVectors = empca.doEig(c);
        // Utils.writeEigensToFile(Constants.loggerPath + "output.log", eigenValueAndVectors);
        // OUTPUT TO WEKA PART
        return TransformToFromWeka.eigensToWeka(eigenValueAndVectors._2, options[2],
                                                WekaUtils.getDatasetClassValues(this.getDataset()), options[3]);
    }
}

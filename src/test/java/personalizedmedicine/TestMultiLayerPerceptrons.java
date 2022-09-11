package personalizedmedicine;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import personalizedmedicine.dimensionality_reduction_methods.DimensionalityReductionChooser;
import personalizedmedicine.helpful_classes.Constants;
import personalizedmedicine.utilpackage.Utils;
import personalizedmedicine.utilpackage.WekaUtils;
import personalizedmedicine.weka.api.library.WekaFileConverter;
import weka.core.Instances;

import java.io.File;

import static personalizedmedicine.helpful_classes.Constants.classRealName;
import static personalizedmedicine.helpful_classes.Constants.completeFileName;

class TestMultiLayerPerceptrons {

    @Test
    void TestAutoencoderWekaTetCase() throws Exception {
        val level2File = new File(completeFileName);
        final Instances originalDataset = WekaUtils.getOriginalData(level2File, classRealName);
        // this command return the logical processors (threads)
        final String cores = String.valueOf(Runtime.getRuntime().availableProcessors());
        // DIMENSIONALITY REDUCTION
        val dimensionalityReductionSelection = new DimensionalityReductionChooser();
        final String[] options = weka.core.Utils.splitOptions(
                "-N 1 -L 0.01 -O 1.0E-6 -G -P " + cores + " -E " + cores + " -S 1 -seed 1");
        // Get current time
        final long start = System.nanoTime();
        final Instances dataset = dimensionalityReductionSelection.dimensionalityReductionSelector(
                Constants.AUTOENCODER_WEKA, originalDataset, true, options);
        Utils.printExecutionTime(start, System.nanoTime());
        // here we save the new data in an arff file
        val wekaFileConverterImpl = new WekaFileConverter();
        wekaFileConverterImpl.arffSaver(dataset, Constants.SRC_MAIN_RESOURCES_PATH + "autoencoderData.arff");
        Assertions.assertNotNull(dataset);
    }
}

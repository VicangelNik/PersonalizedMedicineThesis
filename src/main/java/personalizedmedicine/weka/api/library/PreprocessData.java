package personalizedmedicine.weka.api.library;

import personalizedmedicine.interfaces.ΙPreprocessData;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

/**
 * The Class PreprocessData.
 */
public class PreprocessData implements ΙPreprocessData {

	/** The dataset. */
	private Instances dataset;

	/**
	 * Instantiates a new preprocess data.
	 *
	 * @param dataset the dataset
	 */
	public PreprocessData(Instances dataset) {
		this.dataset = dataset;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see helpful_classes.PreprocessData#removeType(weka.core.Instances, int)
	 */
	@Override
	public Instances removeFeaturesByType(int tagId) throws Exception {
		RemoveType removeType = new RemoveType();
		SelectedTag tag = new SelectedTag(tagId, RemoveType.TAGS_ATTRIBUTETYPE);
		removeType.setAttributeType(tag);
		// The setInputFormat(Instances) method always has to be the last call before
		// the filter is applied, e.g., with Filter.useFilter(Instances,Filter)
		removeType.setInputFormat(dataset);
		return Filter.useFilter(dataset, removeType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see helpful_classes.PreprocessData#removeFeature(weka.core.Instances,
	 * java.lang.String)
	 */
	@Override
	public Instances removeFeature(String rangeList, boolean invert) throws Exception {
		Remove remove = new Remove();
		// invert selection is set first because it is used in the source code for the
		// other sets.
		remove.setInvertSelection(invert);
		remove.setAttributeIndices(rangeList);
		// The setInputFormat(Instances) method always has to be the last call before
		// the filter is applied, e.g., with Filter.useFilter(Instances,Filter)
		remove.setInputFormat(dataset);
		return Filter.useFilter(dataset, remove);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.PreprocessData#removeMissingValues(weka.core.Instances)
	 */
	@Override
	public Instances removeMissingValues() throws Exception {
		ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
		// The setInputFormat(Instances) method always has to be the last call before
		// the filter is applied, e.g., with Filter.useFilter(Instances,Filter)
		replaceMissingValues.setInputFormat(dataset);
		return Filter.useFilter(dataset, replaceMissingValues);
	}

	/**
	 * Sets the dataset.
	 *
	 * @param dataset the new dataset
	 */
	public void setDataset(Instances dataset) {
		this.dataset = dataset;
	}

	/**
	 * Gets the dataset.
	 *
	 * @return the dataset
	 */
	public Instances getDataset() {
		return dataset;
	}
}

package weka.api.library;

import interfaces.PreprocessData;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveType;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

/**
 * The Class PreprocessDataImpl.
 */
public class PreprocessDataImpl implements PreprocessData {

	/*
	 * (non-Javadoc)
	 *
	 * @see helpful_classes.PreprocessData#removeType(weka.core.Instances, int)
	 */
	@Override
	public Instances removeFeaturesByType(Instances data, int tagId) throws Exception {
		RemoveType removeType = new RemoveType();
		removeType.setInputFormat(data);
		SelectedTag tag = new SelectedTag(tagId, RemoveType.TAGS_ATTRIBUTETYPE);
		removeType.setAttributeType(tag);
		return Filter.useFilter(data, removeType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see helpful_classes.PreprocessData#removeFeature(weka.core.Instances,
	 * java.lang.String)
	 */
	@Override
	public Instances removeFeature(Instances data, String rangeList) throws Exception {
		Remove remove = new Remove();
		remove.setAttributeIndices(rangeList);
		remove.setInputFormat(data);
		return Filter.useFilter(data, remove);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see interfaces.PreprocessData#removeMissingValues(weka.core.Instances)
	 */
	@Override
	public Instances removeMissingValues(Instances data) throws Exception {
		ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
		replaceMissingValues.setInputFormat(data);
		return Filter.useFilter(data, replaceMissingValues);
	}
}

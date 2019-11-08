package weka.api.library;

import helpful_classes.PreprocessData;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveType;

// TODO: Auto-generated Javadoc
/**
 * The Class PreprocessDataImpl.
 */
public class PreprocessDataImpl implements PreprocessData{

	/* (non-Javadoc)
	 * @see helpful_classes.PreprocessData#removeType(weka.core.Instances, int)
	 */
	@Override
	public Instances removeType(Instances data, int tagId) throws Exception {
		RemoveType removeType = new RemoveType();
		removeType.setInputFormat(data);
		SelectedTag tag = new SelectedTag(tagId, RemoveType.TAGS_ATTRIBUTETYPE);
		removeType.setAttributeType(tag);
		return Filter.useFilter(data, removeType);
	}

	/* (non-Javadoc)
	 * @see helpful_classes.PreprocessData#removeFeature(weka.core.Instances, java.lang.String)
	 */
	@Override
	public Instances removeFeature(Instances data, String rangeList) throws Exception {
		Remove remove = new Remove();
		remove.setAttributeIndices(rangeList);
		remove.setInputFormat(data);
		return Filter.useFilter(data, remove);
	}

}

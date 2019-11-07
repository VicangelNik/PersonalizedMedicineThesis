/*
 * 
 */
package weka.api.library;

import interfaces.PreprocessData;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveType;

// TODO: Auto-generated Javadoc
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
	public Instances removeType(Instances data, int tagId) throws Exception {
		RemoveType removeType = new RemoveType();
		removeType.setInputFormat(data);
		SelectedTag tag = new SelectedTag(tagId, RemoveType.TAGS_ATTRIBUTETYPE);
		removeType.setAttributeType(tag);
		return Filter.useFilter(data, removeType);
	}

}

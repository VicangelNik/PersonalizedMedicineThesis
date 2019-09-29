package helpful_classes;

import java.util.List;

public class MultiKey {

	private List<String> featureType;
	private String featureName;

	public MultiKey(String featureName, List<String> featureType) {
		super();
		this.featureType = featureType;
		this.featureName = featureName;
	}

	public List<String> getFeatureType() {
		return featureType;
	}

	public void setFeatureType(List<String> featureType) {
		this.featureType = featureType;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}

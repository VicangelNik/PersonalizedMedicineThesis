/*
 *
 */
package dimensionality_reduction_methods;

/*
 *
 */
import smile.manifold.IsoMap;

// TODO: Auto-generated Javadoc
/**
 * The Class IsoMapVic.
 */
public class IsoMapVic extends IsoMap {

	/** The dataset. */
	private double dataset[][];

	/** The dimension of the manifold. */
	private static int d;

	/** The k-nearest neighbor. */
	private static int k;

	/**
	 * Instantiates a new iso map vic.
	 *
	 * @param data    the dataset.
	 * @param d       the dimension of the manifold.
	 * @param k       k-nearest neighbor.
	 * @param cIsomap the c isomap
	 */
	public IsoMapVic(double[][] data, int d, int k, boolean cIsomap) {
		super(data, d, k, cIsomap);
		dataset = data;
		IsoMapVic.d = d;
		IsoMapVic.k = k;
	}

	/**
	 * Gets the dataset.
	 *
	 * @return the dataset
	 */
	public double[][] getDataset() {
		return dataset;
	}

	/**
	 * Sets the dataset.
	 *
	 * @param dataset the new dataset
	 */
	public void setDataset(double[][] dataset) {
		this.dataset = dataset;
	}

	/**
	 * Gets the dimension of the manifold.
	 *
	 * @return the dimension of the manifold
	 */
	public int getD() {
		return d;
	}

	/**
	 * Sets the dimension of the manifold.
	 *
	 * @param d the new dimension of the manifold
	 */
	public static void setD(int d) {
		IsoMapVic.d = d;
	}

	/**
	 * Gets the k-nearest neighbor.
	 *
	 * @return the k-nearest neighbor
	 */
	public int getK() {
		return k;
	}

	/**
	 * Sets the k-nearest neighbor.
	 *
	 * @param k the new k-nearest neighbor
	 */
	public static void setK(int k) {
		IsoMapVic.k = k;
	}
}

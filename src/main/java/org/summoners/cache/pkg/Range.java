package org.summoners.cache.pkg;

/**
 * A range between two long values.
 * @author Brittan Thomas
 */
public class Range {
	
	/**
	 * Instantiates a new range.
	 *
	 * @param minimum
	 *            the minimum value or lower bound
	 * @param maximum
	 *            the maximum value or upper bound
	 */
	public Range(long minimum, long maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	/**
	 * The minimum value or lower bound of this range.
	 */
	private long minimum;
	
	/**
	 * Gets the minimum value or lower bound of this range.
	 *
	 * @return the minimum value or lower bound of this range
	 */
	public long getMinimum() {
		return minimum;
	}
	
	/**
	 * Sets the minimum value or lower bound of this range.
	 *
	 * @param minimum
	 *            the new minimum value or lower bound of this range
	 */
	public void setMinimum(long minimum) {
		this.minimum = minimum;
	}
	
	/**
	 * The maximum value or upper bound of this range.
	 */
	private long maximum;
	
	/**
	 * Gets the maximum value or upper bound of this range.
	 *
	 * @return the maximum value or upper bound of this range
	 */
	public long getMaximum() {
		return maximum;
	}
	
	/**
	 * Sets the maximum value or upper bound of this range.
	 *
	 * @param maximum
	 *            the new maximum value or upper bound of this range
	 */
	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}
	
	/**
	 * Gets the span of this range.
	 *
	 * @return the span of this range
	 */
	public long getSpan() {
		return maximum - minimum;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + minimum + ", " + maximum + ")";
    }
}

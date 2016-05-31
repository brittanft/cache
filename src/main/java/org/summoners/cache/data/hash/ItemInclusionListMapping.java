package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum ItemInclusionListMapping implements AttributeMapping {
	ITEM0(2515855226L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private ItemInclusionListMapping(long hash) {
		this.hash = hash;
		this.attribute = AttributeMapping.properify(name());
	}

	/**
	 * The hashed value of this mapped attribute.
	 */
	private long hash;

	/* (non-Javadoc)
	 * @see org.summoners.io.cache.data.hash.AttributeMapping#getHash()
	 */
	@Override
	public long getHash() {
		return hash;
	}

	/**
	 * The properly formatted description of this attribute.
	 */
	private String attribute;

	/* (non-Javadoc)
	 * @see org.summoners.io.cache.data.hash.AttributeMapping#getAttribute()  
	 */
	@Override
	public String getAttribute() {
		return attribute;
	}

	/* (non-Javadoc)
	 * @see org.summoners.io.cache.data.hash.AttributeMapping#getCategory()
	 */
	@Override
	public String getCategory() {
		return "ItemInclusionList";
	}

}
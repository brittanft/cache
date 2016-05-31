package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum BuildsMapping implements AttributeMapping {
	ITEM1(1652767513L),
	ITEM2(1652767514L),
	ITEM3(1652767515L),
	ITEM4(1652767516L),
	ITEM5(1652767517L),
	ITEM6(1652767518L),
	ITEM7(1652767519L),
	ITEM8(1652767520L),
	ITEM9(1652767521L),
	ITEM10(2036632407L),
	ITEM11(2036632408L),
	ITEM12(2036632409L),
	ITEM13(2036632410L),
	ITEM14(2036632411L),
	ITEM15(2036632412L),
	ITEM16(2036632413L),
	ITEM17(2036632414L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private BuildsMapping(long hash) {
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
		return "Builds";
	}

}
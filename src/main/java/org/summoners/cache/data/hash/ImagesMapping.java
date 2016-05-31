package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum ImagesMapping implements AttributeMapping {
	E_LIFE(3834572118L),
	E_RATE(4157954362L),
	P_LIFE(3631684363L),
	P_LOCAL_ORIENT(2411663638L),
	P_RGBA(4003454851L),
	P_SCALE(574165211L),
	P_SIMPLEORIENT(2838833158L),
	P_TEXTURE(2077370348L),
	P_TYPE(1419287433L),
	PASS(1414934403L),
	RENDERMODE(3850904779L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private ImagesMapping(long hash) {
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
		return "Images";
	}

}
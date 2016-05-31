package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum MeshSkin10Mapping implements AttributeMapping {
	CHAMPION_SKIN_I_D(1442723778L),
	ANIMATIONS(2458837360L),
	CHAMPION_SKIN_NAME(2473555762L),
	SELF_ILLUMINATION(4045022108L),
	SIMPLE_SKIN(3259296560L),
	SKELETON(4226211790L),
	SKIN_SCALE(4223011948L),
	TEXTURE(122452538L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private MeshSkin10Mapping(long hash) {
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
		return "MeshSkin10";
	}

}
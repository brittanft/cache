package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum MeshSkin7Mapping implements AttributeMapping {
	CHAMPION_SKIN_I_D(2723917598L),
	ANIMATIONS(825046220L),
	CHAMPION_SKIN_NAME(1204748430L),
	SELF_ILLUMINATION(2776214776L),
	SIMPLE_SKIN(1625505420L),
	SKELETON(1271406890L),
	SKIN_SCALE(3850445328L),
	TEXTURE(439951838L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private MeshSkin7Mapping(long hash) {
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
		return "MeshSkin7";
	}

}
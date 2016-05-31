package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum MeshSkin14Mapping implements AttributeMapping {
	CHAMPION_SKIN_I_D(2358249150L),
	ANIMATIONS(3338480748L),
	CHAMPION_SKIN_NAME(2115078190L),
	SELF_ILLUMINATION(3686544536L),
	SIMPLE_SKIN(4138939948L),
	SKELETON(3242322122L),
	SKIN_SCALE(2518237808L),
	TEXTURE(1404269118L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private MeshSkin14Mapping(long hash) {
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
		return "MeshSkin14";
	}

}
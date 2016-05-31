package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum MeshSkin1Mapping implements AttributeMapping {
	CHAMPION_SKIN_I_D(1350629540L),
	ANIMATIONS(3800548434L),
	CHAMPION_SKIN_NAME(1742464788L),
	SELF_ILLUMINATION(3313931134L),
	SIMPLE_SKIN(306040338L),
	SKELETON(599757744L),
	SKIN_SCALE(2112639242L),
	TEXTURE(664710616L),
	Y_OFFSET(3777620009L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private MeshSkin1Mapping(long hash) {
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
		return "MeshSkin1";
	}

}
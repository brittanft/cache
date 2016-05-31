package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum MeshSkinMapping implements AttributeMapping {
	CHAMPION_SKIN_I_D(1091449537L),
	SKIN_CLASSIFICATION(1101385219L),
	ANIMATIONS(4263852911L),
	ATTRIBUTE_FLAGS(535817099L),
	BRUSH_ALPHA_OVERRIDE(2212039952L),
	CAST_SHADOWS(757333300L),
	CHAMPION_SKIN_NAME(2142495409L),
	EMISSIVE_TEXTURE(1831801988L),
	EXTRA_CHARACTER_PRELOADS(3726555107L),
	FRESNEL(3400833179L),
	FRESNEL_BLUE(343518805L),
	FRESNEL_GREEN(455913160L),
	FRESNEL_RED(3966094422L),
	GLOSS_TEXTURE(3060448337L),
	GLOW_FACTOR(4289923804L),
	ICON_AVATAR(895532722L),
	IS_OPAQUE(4118570059L),
	MATERIAL_OVERRIDE0_GLOSS_TEXTURE(212624590L),
	MATERIAL_OVERRIDE0_SUBMESH(964876944L),
	MATERIAL_OVERRIDE0_TEXTURE(3502738494L),
	MATERIAL_OVERRIDE1_SUBMESH(3700722767L),
	MATERIAL_OVERRIDE1_TEXTURE(1943617021L),
	MATERIAL_OVERRIDE2_SUBMESH(2141601294L),
	MATERIAL_OVERRIDE2_TEXTURE(384495548L),
	MATERIAL_OVERRIDE_PRIORITY(2068574711L),
	OVERRIDE_BOUNDING_BOX(1786440603L),
	PARTICLE_OVERRIDE__CHAMPION_KILL_DEATH_PARTICLE(3294654406L),
	PARTICLE_OVERRIDE__DEATH_PARTICLE(4276189325L),
	REFLECTION_FRESNEL(874689392L),
	REFLECTION_FRESNEL_BLUE(3577271338L),
	REFLECTION_FRESNEL_GREEN(2953575987L),
	REFLECTION_FRESNEL_RED(1557586241L),
	REFLECTION_MAP(3732685137L),
	REFLECTION_OPACITY_DIRECT(3180921225L),
	REFLECTION_OPACITY_GLANCING(2478112913L),
	SELF_ILLUMINATION(3713961755L),
	SIMPLE_SKIN(769344815L),
	SKELETON(1895303501L),
	SKIN_SCALE(4100866733L),
	SUBMESHES_TO_HIDE(708066840L),
	TEXTURE(2640183547L),
	TEXTURE_LOW(2632864121L),
	USES_SKIN_V_O(2625724482L),
	V_O_OVERRIDE(669070277L),
	WEIGHT(693474744L),
	Y_OFFSET(1458125644L),
	ACTIVITY(3670335215L),
	FILTERWITHBUFFEREDINPUT(3117300877L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private MeshSkinMapping(long hash) {
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
		return "MeshSkin";
	}

}
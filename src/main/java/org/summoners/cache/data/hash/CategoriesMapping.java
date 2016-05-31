package org.summoners.cache.data.hash;

/**
 * Representation of an attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public enum CategoriesMapping implements AttributeMapping {
	ACTIVE(482800660L),
	ARMOR(533725361L),
	ARMOR_PENETRATION(1113666618L),
	ATTACK_SPEED(2168244529L),
	AURA(4260376497L),
	BOOTS(3643346355L),
	CONSUMABLE(3723078833L),
	COOLDOWN_REDUCTION(1836107546L),
	CRITICAL_STRIKE(4138078719L),
	DAMAGE(2378441949L),
	GOLD_PER(220647055L),
	HEALTH(3444378282L),
	HEALTH_REGEN(4123527635L),
	JUNGLE(2462451563L),
	INTERNAL(3519994987L),
	LIFE_STEAL(1861317573L),
	MAGIC_PENETRATION(1241846252L),
	MANA(577028501L),
	MANA_REGEN(2236684456L),
	MOVEMENT(1939191901L),
	NONBOOTS_MOVEMENT(2434154129L),
	LANE(4091408602L),
	ON_HIT(438673606L),
	SLOW(1056526575L),
	SPELL_VAMP(4007135656L),
	SPELL_BLOCK(2199193779L),
	SPELL_DAMAGE(771952745L),
	STEALTH(487824693L),
	TENACITY(4211725725L),
	TRINKET(853724105L),
	VISION(1466389974L);

	/**
	 * Instantiates a new attribute mapping.
	 * 
	 * @param hash
	 *            the hashed value of the attribute
	 */
	private CategoriesMapping(long hash) {
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
		return "Categories";
	}

}
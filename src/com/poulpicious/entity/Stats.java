package com.poulpicious.entity;

/**
 * 
 * @author yann
 *
 * This class contains all the stats of the player.
 * It currently contains only HEALTH, DAMAGES, and RESISTANCE.
 */
public class Stats {

	// Base values for each stat.
	private final float HEALTH_BASE = 100f;
	private final float DAMAGE_BASE = 5f;
	private final float RESIST_BASE = 0f;

	// Maximum value for each stat.
	private final float HEALTH_MAX = 250f;
	private final float DAMAGE_MAX = 15f;
	private final float RESIST_MAX = 80f;

	// How much a stat gets for when it gets a level (point).
	private final float HEALTH_GROWTH = 2f;
	private final float DAMAGE_GROWTH = 0.1f;
	private final float RESIST_GROWTH = 0.8f;

	// The actual stats.
	private Stat health;
	private Stat bulletDamage;
	private Stat resistance;

	// How much points do we have ?
	private int pointsToSpend;

	// The leveling code.
	private int level;
	private int experience;

	// The points are actually the same as levels for stats.
	public Stats(int level, int experience, int healthPoints, int damagePoints, int resistancePoints) {
		this.health = new Stat(HEALTH_BASE, HEALTH_MAX, HEALTH_GROWTH).grow(healthPoints);
		this.bulletDamage = new Stat(DAMAGE_BASE, DAMAGE_MAX, DAMAGE_GROWTH).grow(damagePoints);
		this.resistance = new Stat(RESIST_BASE, RESIST_MAX, RESIST_GROWTH).grow(resistancePoints);
		this.level = level;
		
		this.pointsToSpend = level - healthPoints - damagePoints - resistancePoints;
		this.experience = experience;
	}

	// Get some exp to the player
	public void giveExp(int amount) {
		this.experience += amount;

		int expNeeded = getExperienceNeeded(level + 1);
		while (this.experience > expNeeded) {
			levelUp();
			this.experience -= expNeeded;
		}
	}

	public void incrementStat(Stat stat) {
		if (this.pointsToSpend > 0) {
			stat.grow(1);
			this.pointsToSpend--;
		}
	}

	public void decrementStat(Stat stat) {
		if (stat.getLevel() > 0) {
			stat.grow(-1);
			this.pointsToSpend++;
		}
	}

	public void levelUp() {
		this.level++;
		this.pointsToSpend++;
	}

	public int getExperienceNeeded(int level) {
		return (level * level * level) - 5 * (level * level) + 23 * level;
	}

	public Stat getHealth() {
		return health;
	}

	public void setHealth(Stat health) {
		this.health = health;
	}

	public Stat getBulletDamage() {
		return bulletDamage;
	}

	public void setBulletDamage(Stat bulletDamage) {
		this.bulletDamage = bulletDamage;
	}

	public Stat getResistance() {
		return resistance;
	}

	public void setResistance(Stat resistance) {
		this.resistance = resistance;
	}

	public int getPointsToSpend() {
		return pointsToSpend;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

}

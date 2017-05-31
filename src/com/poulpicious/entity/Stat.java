package com.poulpicious.entity;

import java.util.HashMap;

/**
 * 
 * @author yann
 *
 * This class represents a stat (characteristic) of the player.
 */
public class Stat {

	private float maxValue; // Maximum value it can ever have
	private float value; // Current value
	private float growth; // Growth factor : how much it grows for each level
	
	private int level; // The current level

	private HashMap<String, Float> bonuses; // Did we get buffs ? Like an item giving us bonus (NOT IMPLEMENTED)
	private HashMap<String, Float> maluses; // Did we get buffs ? Like an item giving us malus (NOT IMPLEMENTED)

	public Stat(float value, float maxValue, float growth) {
		this.maxValue = maxValue;
		this.value = value;
		this.growth = growth;
		this.level = 0;

		this.bonuses = new HashMap<String, Float>();
		this.maluses = new HashMap<String, Float>();
	}

	// Add to the value and level up the stat.
	public Stat grow(int times) {
		this.add(growth * times);
		this.level += times;
		return this;
	}
	
	public Stat reduce(int times) {
		this.sub(growth * times);
		return this;
	}

	// Returns the percentage of the stat (how advanced is it compared to the max value)
	public float getRatio() {
		return value / maxValue;
	}

	public void restore() {
		this.value = maxValue;
	}

	public void sub(float r) {
		this.value -= r;
	}

	public void add(float a) {
		this.value += a;
	}

	public void set(float a) {
		this.value = a;
	}

	public void addBonus(String name, float value) {
		this.bonuses.put(name, value);
	}

	public void removeBonus(String name) {
		this.bonuses.remove(name);
	}

	public void addMalus(String name, float value) {
		this.maluses.put(name, value);
	}

	public void removeMalus(String name) {
		this.maluses.remove(name);
	}

	public float getValue() {
		return value;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public float getGrowth() {
		return growth;
	}

	public void setGrowth(float growth) {
		this.growth = growth;
	}

	public int getLevel() {
		return level;
	}

}

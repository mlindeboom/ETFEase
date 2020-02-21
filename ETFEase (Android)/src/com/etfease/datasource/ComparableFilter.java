package com.etfease.datasource;

public abstract class ComparableFilter<T extends Comparable<T>>
extends Filter<T> {
	private final T comparable;

	protected ComparableFilter(T comparable) {
		this.comparable = comparable;
	}

	@Override
	public boolean passes(T object) {
		return passes(object.compareTo(comparable));
	}

	
	
	protected abstract boolean passes(int result);
}
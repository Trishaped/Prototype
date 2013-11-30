package fr.umlv.waterfowl.dictionary;

import java.util.BitSet;

import fr.umlv.waterfowl.utils.BitSetConverter;

public class ContainerIdCount {
	private int count;
	private long id;
	BitSetConverter bitSetConverter = new BitSetConverter();
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ContainerIdCount incCount() {
		count++;
		return this;
	}
	public long getId() {
		return id;
	}
	public ContainerIdCount setId(long id) {
		this.id = id;
		return this;
	};
	public ContainerIdCount(long id) {
		this.id=id;
		count=1;
	}
	public ContainerIdCount(long id, int count) {
		this.id = id;
		this.count=count;
	}
	public ContainerIdCount() {
		count=1;
	}
	public String prettyPrint() {
		BitSet bs = bitSetConverter.convert((int)id);
		return id + "\t" +count+ " :: "+bs;
	}
}

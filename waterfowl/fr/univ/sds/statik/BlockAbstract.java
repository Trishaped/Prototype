package fr.univ.sds.statik;

public class BlockAbstract {
	private byte rank;
	private byte blockId;
	
	public BlockAbstract(byte rank, byte blockId) {
		this.rank= rank;
		this.blockId = blockId;
	}
	public byte getRank() {
		return rank;
	}
	public void setRank(byte rank) {
		this.rank = rank;
	}
	public byte getBlockId() {
		return blockId;
	}
	public void setBlockId(byte blockId) {
		this.blockId = blockId;
	}
	public void display() {
		System.out.println("("+rank +  ", "+blockId+")");
	}
}

package com.scene;

public class Tile
{
	private int parent;

	private int posX;

	private int posY;

	private short depth;

	private short mask = TileMask.TM_BLOCK;

	public Tile(int parent, int posX, int posY, short depth, short mask)
	{

	}

	public void init(int parent, int posX, int posY, short depth, short mask)
	{
		this.parent = parent;
		this.posX = posX;
		this.posY = posY;
		this.depth = depth;
		this.mask = mask;
	}

	public short getDepth()
	{
		return depth;
	}

	public int getParent()
	{
		return parent;
	}

	public boolean isWalkable()
	{
		return mask == TileMask.TM_NORMAL || mask == TileMask.TM_SHADOW;
	}

	public void setMask(short mask)
	{
		this.mask = mask;
	}
}

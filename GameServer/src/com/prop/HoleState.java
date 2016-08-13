package com.prop;

/**
 * 装备孔位的状态
 */
public interface HoleState {
	/**
	 * 已开启，未镶嵌
	 */
	int NOTINLAY = 0;
	
	/**
	 * 槽位1
	 */
	int HOLE_ONE = 1;
	
	/**
	 * 槽位2
	 */
	int HOLE_TWO = 2;
	
	/**
	 * 槽位3
	 */
	int HOLE_THREE = 3;
	
	/**
	 * 装备槽位的总个数
	 */
	int HOLE_COUNT = 3;
}
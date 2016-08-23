package com.db;

import com.util.GameLog;

public class DBObject {
	private short op = DBOption.NONE;

	public final void setOp(short option) {
		if (this.op == DBOption.INSERT && option == DBOption.UPDATE) {
			return;
		}
		this.op = option;
	}

	public final short getOp() {
		return this.op;
	}

	public boolean beginAdd() {
		if (getOp() == DBOption.INSERT) {
			setOp(DBOption.NONE);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitAdd(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.INSERT);
			GameLog.error("添加出错了，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}

	public boolean beginUpdate() {
		if (getOp() == DBOption.UPDATE) {
			setOp(DBOption.NONE);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitUpdate(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.UPDATE);
			GameLog.error("更新出错了，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}

	public boolean beginDelete() {
		if (getOp() == DBOption.DELETE) {
			setOp(DBOption.NONE);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitDelete(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.DELETE);
			GameLog.error("删除数据出错，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}
}
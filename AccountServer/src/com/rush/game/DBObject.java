package com.rush.game;

import com.rush.util.GameLog;

public class DBObject {
	private short op = DBOption.None;

	public final void setOp(short option) {
		if (this.op == DBOption.Insert && option == DBOption.Update) {
			return;
		}
		this.op = option;
	}

	public final short getOp() {
		return this.op;
	}

	public boolean beginAdd() {
		if (getOp() == DBOption.Insert) {
			setOp(DBOption.None);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitAdd(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.Insert);
			GameLog.error("添加出错了，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}

	public boolean beginUpdate() {
		if (getOp() == DBOption.Update) {
			setOp(DBOption.None);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitUpdate(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.Update);
			GameLog.error("更新出错了，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}

	public boolean beginDelete() {
		if (getOp() == DBOption.Delete) {
			setOp(DBOption.None);
			return true;
		}
		return false;
	}

	/**
	 * 大部分的情况这里应该是传入UserId，如果没有这个则可以考虑直接用数据库的Id
	 */
	public void commitDelete(long userId, boolean result) {
		if (!result) {
			setOp(DBOption.Delete);
			GameLog.error("删除数据出错，状态还原, UserId:  " + userId + "\n" + toString());
		}
	}
}
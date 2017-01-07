package com.store59.print.common.enums;

public enum AdOrderEnum implements EnumCode<Byte> {

	// 1-审核中，2-未支付，3-已发布，4-已取消，5-已过期，6-已完成
	/** 审核 */
	INIT((byte) 1),

	/** 未支付 */
	UNPAY((byte) 2),

	/** 已发布 */
	PUBLISHED((byte) 3),

	/** -已取消 */
	CANCELED((byte) 4),

	/** 已过期 */
	DATEOUT((byte) 5),

	/** -已完成 */
	FINISHED((byte) 6);

	private final Byte code;

	/**
	 * @param code
	 */
	private AdOrderEnum(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

}

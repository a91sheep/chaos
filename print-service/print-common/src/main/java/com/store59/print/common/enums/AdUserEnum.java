package com.store59.print.common.enums;

public enum AdUserEnum implements EnumCode<Byte> {

	/** 待审核 */
	INIT((byte) 0),

	/** 审核通过 */
	CONFIRMED((byte) 1),

	/** 审核不通过 */
	SENDING((byte) 2);

	private final Byte code;

	/**
	 * @param code
	 */
	private AdUserEnum(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

}

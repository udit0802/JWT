package com.jwt.exception;

import com.jwt.vo.Status;

public class ApplicationException extends Exception{

	private final Status status;

	public ApplicationException(Status status, String message) {
		super(message);
		this.status = status;
	}

	public ApplicationException(Status status, Throwable t) {
		super(t);
		this.status = status;
	}

	public ApplicationException(Status status) {
		super();
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
}

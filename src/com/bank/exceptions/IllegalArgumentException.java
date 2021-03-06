package com.bank.exceptions;

public class IllegalArgumentException extends InteractionException {

  /**
   * set the UID of the IllegalArgumentException.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize the exception.
   */
  public IllegalArgumentException() {
  }

  /** 
   * Initialize the exception to store the error message.
   * @param message the message to hint the user
   */
  public IllegalArgumentException(String message) {
    super(message);
  }

  /**
   * This is automatically generated by the eclipse.
   * 
   * @param cause super class of the exception
   */
  public IllegalArgumentException(Throwable cause) {
    super(cause);
  }

  /**
   * This is automatically generated by the eclipse.
   * 
   * @param message error message
   * @param cause super class of the exception
   */
  public IllegalArgumentException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * This is automatically generated by the eclipse.
   * 
   * @param message error message
   * @param cause super class of the exception
   * @param enableSuppression determines whether or not suppression is enabled
   * @param writableStackTrace the trace of the error
   */
  public IllegalArgumentException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}

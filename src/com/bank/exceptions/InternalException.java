package com.bank.exceptions;

public class InternalException extends Exception {

  /**
   * Initialize the internal exception.
   */
  public InternalException() {
    super();
  }

  /**
   * Initialize the exception to let it be able to contain hint word.
   * @param string the error message of the exception
   */
  public InternalException(String string) {
    super(string);
  }

  /**
   * set the UID of the Internal exception.
   */
  private static final long serialVersionUID = 1L;

}

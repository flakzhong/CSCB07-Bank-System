package com.bank.exceptions;

public class UnexposedException extends InternalException {

  /**
   * set the UID of the UnexposedException exception.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Initialize the UnexposedException.
   */
  public UnexposedException() {
    super();
  }

  /**
   * Initialize the UnexposedException to let it be able to contain hint word.
   * @param string the error message of the exception
   */
  public UnexposedException(String string) {
    super(string);
  }

}

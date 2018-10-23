package com.bank.exceptions;

public class AuthenticationException extends InteractionException {

  /**
   * set the UID of the AuthenticationException.
   */
  private static final long serialVersionUID = 1L;

  /**
   * initialize the exception.
   */
  public AuthenticationException() {}

  /**
   * a constructor to initialize the exception with a string argument which tells the user error
   * message.
   * 
   * @param arg0 the error message
   */
  public AuthenticationException(String arg0) {
    super(arg0);
  }

  /**
   * a constructor to initialize the exception with throwable argument.
   * 
   * @param arg0 the throwable argument indicating the type of the error
   */
  public AuthenticationException(Throwable arg0) {
    super(arg0);

  }

  /**
   * a constructor to initialize the exception with string and throwable argument.
   * 
   * @param arg0 the exception to store the error message to hint the user.
   * @param arg1 the throwable argument indicating the type of the error
   */
  public AuthenticationException(String arg0, Throwable arg1) {
    super(arg0, arg1);

  }

  /**
   * a constructor to initialize the exception with string and throwable argument.
   * @param arg0 the exception to store the error message to hint the user.
   * @param arg1 the throwable argument indicating the type of the error
   * @param arg2 bool1
   * @param arg3 bool2
   */
  public AuthenticationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);

  }

}

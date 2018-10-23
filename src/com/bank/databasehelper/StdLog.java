package com.bank.databasehelper;

/**
 * The abstract class for recording error message. When we don't want to invoke exception to record
 * error, which will interrupt the control flow, then we will use this.
 * 
 * @author jinende
 *
 */
public abstract class StdLog {
  /**
   * (Record) Output the error message.
   * 
   * @param inf The message to record.
   */
  public abstract void outputMsg(String inf);
}

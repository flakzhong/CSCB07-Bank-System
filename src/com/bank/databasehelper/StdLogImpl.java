package com.bank.databasehelper;

/**
 * Simple implementation.
 * 
 * @author jinende
 *
 */
public class StdLogImpl extends StdLog {


  public static final StdLog LOGGER = new StdLogImpl();

  private StdLogImpl() {}

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.databasehelper.StdLog#outputMsg(java.lang.String)
   */
  @Override
  public void outputMsg(String inf) {
    System.out.println(inf);
  }

}

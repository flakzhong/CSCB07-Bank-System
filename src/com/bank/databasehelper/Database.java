package com.bank.databasehelper;

import com.bank.datainterface.DataOperator;


public class Database implements DataOperator {
  /**
   * The Only Database, singleton pattern.
   */
  public static final DataOperator DATABASE = new Database(
      new DatabaseInsert(new DatabaseUpdate(new DatabaseSelect(new DatabaseBottom()))));


  private DataOperator below;
  private DataOperator upper;

  /**
   * Initialization for the decorator pattern.
   * 
   * @param op The dataOperator to be decorated.
   */
  private Database(DataOperator op) {
    // Every aspect of dataoperator consists into a circle.
    // It will insert itself into the dataoperator.
    // Imagine a ring.
    DataOperator pre = op.getUpper();
    pre.setBelow(this);
    op.setUpper(this);
    this.below = op;
    this.upper = pre;


  }



  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataOperator#setUpper(com.bank.datainterface.DataOperator)
   */
  @Override
  public void setUpper(DataOperator upper) {
    this.upper = upper;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataOperator#getUpper()
   */
  @Override
  public DataOperator getUpper() {
    return this.upper;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataOperator#setBelow(com.bank.datainterface.DataOperator)
   */
  @Override
  public void setBelow(DataOperator below) {
    this.below = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataOperator#getBelow()
   */
  @Override
  public DataOperator getBelow() {
    return this.below;
  }



}

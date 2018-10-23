package com.bank.datainterface;

import com.bank.databasehelper.StdLogImpl;

/**
 * The interface for all the DataOperator. All the DataOperator has to be a type constructor which
 * maps a DataOperator into another DataOperator. Except DatabaseBottom. It's decorator pattern.
 * 
 * @author jinende
 *
 */
public interface DataOperator {
  /**
   * Cast 'op' to DataSelector.
   * 
   * @param op To be cast
   * @return The DataSelector.
   */
  public static DataSelector select(DataOperator op) {
    return (DataSelector) changeAspect(DataSelector.class, op);
  }

  /**
   * Cast 'op' to DataInserter.
   * 
   * @param op to be cast
   * @return The DataInserter.
   */
  public static DataInserter insert(DataOperator op) {
    return (DataInserter) changeAspect(DataInserter.class, op);
  }


  /**
   * Cast 'op' to DataUpdator.
   * 
   * @param op to be cast
   * @return The DataUpdator.
   */
  public static DataUpdator update(DataOperator op) {
    return (DataUpdator) changeAspect(DataUpdator.class, op);
  }

  /**
   * Cast 'op' to DataIniter.
   * 
   * @param op to be cast
   * @return The DataUpdator.
   */
  public static DataIniter initer(DataOperator op) {
    return (DataIniter) changeAspect(DataIniter.class, op);
  }

  /**
   * Cast the current DataOperator into 'type'.
   * 
   * @param type A type extends DataOperator
   * @param op The DataOperator to be cast
   * @return A DataOperator with type 'type'
   */
  public static DataOperator changeAspect(Class<? extends DataOperator> type, DataOperator op) {
    if (type.isInstance(op)) {
      return op;
    }
    DataOperator origin = op.getUpper();
    if (type.isInstance(origin)) {
      return origin;
    }
    while (op != origin) {
      if (type.isInstance(op)) {
        return op;
      }
      op = op.getBelow();
    }

    StdLogImpl.LOGGER.outputMsg("Issues.");

    return null;

  }

  /**
   * Set the above DataOperator.
   * 
   * @param upper The DataOperator which takes the current object as argument to map.
   */
  public void setUpper(DataOperator upper);


  /**
   * Get the DataOperator that maps the current object.
   * 
   * @return The DataOperator that maps the current object.
   */
  public DataOperator getUpper();

  /**
   * Set the below DataOperator.
   * 
   * @param below The DataOperator which is mapped by the current object.
   */
  public void setBelow(DataOperator below);

  /**
   * Get the DataOperator that is mapped by the current object.
   * 
   * @return The DataOperator that is mapped by the current object.
   */
  public DataOperator getBelow();

}

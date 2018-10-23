package com.bank.databasehelper;

import com.bank.database.DatabaseDriver;
import com.bank.database.InitializeDatabase;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InternalException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * DatabaseDrive Helper.
 * 
 * @author jinende
 *
 */
public class DatabaseDriverHelper extends DatabaseDriver {
  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);


  /**
   * A wrapper for DatabaseDriver.connectOrCreateDatabase().
   * 
   * @return Connection.
   */
  protected static Connection connectOrCreateDataBase() {
    return DatabaseDriver.connectOrCreateDataBase();
  }

  /**
   * This method will try to initialize the database.
   */
  public static boolean initializeDatabase() {
    int roleNumber = 0;
    try {
      roleNumber = DatabaseSelectHelper.getRoles().size();
    } catch (InternalException e) {
      InitializeDatabase.initialize();
      return true;
    }

    if (roleNumber == 0) {
      InitializeDatabase.initialize();
      return true;
    } else {
      return false;
    }

  }

  /**
   * This method will reinitialize the database.
   * 
   * @return the boolean that whether the process have succeeded
   * @throws InternalException there is something wrong dealing with the database
   */
  public static boolean reInitializeDatabase() throws InternalException {
    // initialize the connection
    Connection connection = null;
    // call the reinitialize method
    try {
      connection = DatabaseDriver.reInitialize();
      connection.close();
    } catch (SQLException e) {
      throw new InternalException("Cannot ReInitialize database.");
    }
    return true;
  }
}

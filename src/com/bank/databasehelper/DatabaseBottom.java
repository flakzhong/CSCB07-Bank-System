package com.bank.databasehelper;

import com.bank.datainterface.DataIniter;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;

public class DatabaseBottom implements DataIniter {

  private DataOperator below;
  private DataOperator upper;

  private static DataInserter DatabaseInsertHelper = null;

  private static final String trivialBackUpPath = "database.bak";
  private static final String databasePath = "bank.db";

  private static final BigDecimal interestRate = new BigDecimal(0.2);

  /**
   * The DatabaseBottom.
   */
  DatabaseBottom() {
    // If will become a circle to itself.
    this.below = this;
    this.upper = this;
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

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataIniter#initialize()
   */
  @Override
  public boolean initialize() throws InteractionException, InternalException {
    return DatabaseDriverHelper.initializeDatabase();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataIniter#reInitialize()
   */
  @Override
  public boolean reInitialize() throws InteractionException, InternalException {
    if (DatabaseInsertHelper == null) {
      DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);
    }
    return (DatabaseDriverHelper.reInitializeDatabase()
        && initializeDatabaseBottom());
  }

  private static boolean initializeRoles()
      throws InteractionException, InternalException {
    for (Roles eachRole : Roles.values()) {
      DatabaseInsertHelper.insertRole(eachRole.toString());
    }

    return true;
  }

  private static boolean initializeAccountTypes()
      throws InteractionException, InternalException {

    for (AccountTypes acctype : AccountTypes.values()) {
      DatabaseInsertHelper.insertAccountType(acctype.name(), interestRate);
    }
    return true;

  }

  private static boolean initializeDatabaseBottom()
      throws InteractionException, InternalException {
    return initializeRoles() && initializeAccountTypes();
  }

  private static boolean fileCopy(String src, String dst) throws InternalException {
    FileOutputStream output = null;
    FileInputStream input = null;
    FileChannel outputChannel = null;
    FileChannel inputChannel = null;
    try {
      output = new FileOutputStream(dst);
      input = new FileInputStream(src);
      outputChannel = output.getChannel();
      inputChannel = input.getChannel();
      inputChannel.transferTo(0, inputChannel.size(), outputChannel);
      inputChannel.close();
      outputChannel.close();
      input.close();
      output.close();
    } catch (IOException e) {
      try {
        if (output != null) {
          output.close();
        }
        if (input != null) {
          input.close();
        }
        if (outputChannel != null) {
          outputChannel.close();
        }
        if (inputChannel != null) {
          inputChannel.close();
        }
      } catch (IOException f) {
        ;
      }
      throw new InternalException(e.getMessage());
    }
    return true;

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataIniter#trivialBackup()
   */
  @Override
  public boolean trivialBackup() throws InternalException {
    return fileCopy(databasePath, trivialBackUpPath);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataIniter#trivialRecover()
   */
  @Override
  public boolean trivialRecover() throws InternalException {
    return fileCopy(trivialBackUpPath, databasePath);
  }



}

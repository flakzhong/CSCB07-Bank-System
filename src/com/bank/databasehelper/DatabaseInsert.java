package com.bank.databasehelper;

import com.bank.database.DatabaseInsertException;
import com.bank.database.DatabaseInserter;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInsert extends DatabaseInserter implements DataInserter {


  private DataOperator below;
  private DataOperator upper;


  /**
   * Decorator pattern. DatabaseInsert.
   * 
   * @param op The below DataOperator.
   */
  DatabaseInsert(DataOperator op) {
    // Every aspect of dataoperator consists into a circle.
    // It will insert itself into the dataoperator.
    // Imagine a ring.
    DataOperator pre = op.getUpper();
    pre.setBelow(this);
    op.setUpper(this);
    this.below = op;
    this.upper = pre;
  }

  @Override
  public void setUpper(DataOperator upper) {
    this.upper = upper;
  }

  @Override
  public DataOperator getUpper() {
    return this.upper;
  }

  @Override
  public void setBelow(DataOperator below) {
    this.below = this;
  }

  @Override
  public DataOperator getBelow() {
    return this.below;
  }

  /**
   * Use this to insert an account to the database.
   * 
   * @param name the name of the account
   * @param balance the balance of the new account
   * @param typeId the typeId of the account
   * @return accountId if successful.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is functional error in the database
   */
  public int insertAccount(String name, BigDecimal balance, int typeId)
      throws InteractionException, InternalException {
    // check whether the input name is valid
    // the balance should be a non-negative number
    if (name == null || name.equals("")
        || balance.compareTo(new BigDecimal(0)) < 0) {
      throw new InteractionException("Invalid input.");
    } else {
      int id = -1;
      Connection connection = null;
      connection = DatabaseDriverHelper.connectOrCreateDataBase();
      // then try to call the method provided in the database
      // you have to make sure that it will not be changed
      // if there is error then raise the internalException
      try {
        // if we can not make the connection, just throw the internal Exception
        if (connection == null) {
          throw new InternalException();
        }
        id = DatabaseInserter.insertAccount(name, balance, typeId, connection);
      } catch (DatabaseInsertException e) {
        throw new InternalException(e.getMessage());
      }
      try {
        // then try to close the database
        // if there is something wrong with closing the database
        // then print the message to the user
        connection.close();
      } catch (SQLException e) {
        throw new InternalException(e.getMessage());
      }
      return id;
    }
  }

  /**
   * Use this to insert a new account type to the database.
   * 
   * @param name the name of the account type
   * @param interestRate the interest rate of the account
   * @return typeId if successful.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is functional error in the database
   */

  public int insertAccountType(String name, BigDecimal interestRate)
      throws InteractionException, InternalException {
    // check whether the input name is valid or not
    // if the name is invalid then raise the interaction error
    if (name == null || name.equals("")
        || interestRate.compareTo(new BigDecimal("0")) < 0
        || interestRate.compareTo(new BigDecimal("0.2")) < 0) {
      throw new InteractionException("Invalid input.");
    } else {
      int id = -1;
      Connection connection = null;
      connection = DatabaseDriverHelper.connectOrCreateDataBase();
      // then try to call the method provided in the database
      // you have to make sure that it will not be changed
      // if there is error then raise the internalException
      try {
        // if we can not make the connection, just throw the internal Exception
        if (connection == null) {
          throw new InternalException();
        }
        id = DatabaseInserter.insertAccountType(name, interestRate, connection);
      } catch (DatabaseInsertException e) {
        throw new InternalException(e.getMessage());
      }
      try {
        // then try to close the database
        // if there is something wrong with closing the database
        // then print the message to the user
        connection.close();
      } catch (SQLException e) {
        throw new InternalException(e.getMessage());
      }
      return id;
    }
  }


  /**
   * Use this to insert a new user to the database.
   * 
   * @param name the name of the user
   * @param age the age of the user.
   * @param address the address of the user.
   * @param roleId the id of the role of the user.
   * @param password the password set by user
   * @return userId if successful.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is functional error in the database
   */
  public int insertNewUser(String name, int age, String address, int roleId, String password)
      throws InteractionException, InternalException {
    if (name == null || name.equals("")
        || age <= 0
        || address == null
        || password == null) {
      throw new InteractionException("Invalid input.");
    } else {
      int id = -1;
      Connection connection = null;
      connection = DatabaseDriverHelper.connectOrCreateDataBase();
      // then try to call the method provided in the database
      // you have to make sure that it will not be changed
      // if there is error then raise the internalException
      try {
        // if we can not make the connection, just throw the internal Exception
        if (connection == null) {
          throw new InternalException();
        }
        id = DatabaseInserter.insertNewUser(name, age, address, roleId, password, connection);
      } catch (DatabaseInsertException e) {
        throw new InternalException(e.getMessage());
      }
      try {
        // then try to close the database
        // if there is something wrong with closing the database
        // then print the message to the user
        connection.close();
      } catch (SQLException e) {
        throw new InternalException(e.getMessage());
      }
      return id;
    }
  }

  /**
   * Use this to insert a new role to the database.
   * 
   * @param role the name of the role
   * @return roleId if successful
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is functional error in the database
   */

  public int insertRole(String role) throws InteractionException, InternalException {
    if (role == null) {
      throw new InteractionException("Invalid input.");
    } else {
      int id = -1;
      Connection connection = null;
      connection = DatabaseDriverHelper.connectOrCreateDataBase();
      // then try to call the method provided in the database
      // you have to make sure that it will not be changed
      // if there is error then raise the internalException
      try {
        // if we can not make the connection, just throw the internal Exception
        if (connection == null) {
          throw new InternalException();
        }
        id = DatabaseInserter.insertRole(role, connection);
      } catch (DatabaseInsertException e) {
        throw new InternalException(e.getMessage());
      }
      try {
        // then try to close the database
        // if there is something wrong with closing the database
        // then print the message to the user
        connection.close();
      } catch (SQLException e) {
        throw new InternalException(e.getMessage());
      }
      return id;
    }
  }

  /**
   * Use this to make a account belong to the given user.
   * 
   * @param userId the id of the user
   * @param accountId the id of the account
   * 
   * @return accountId the id of the account
   * @throws InternalException if there is functional error in the database
   */
  public int insertUserAccount(int userId, int accountId) throws InternalException {
    int id = -1;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // then try to call the method provided in the database
    // you have to make sure that it will not be changed
    // if there is error then raise the internalException
    try {
      // if we can not make the connection, just throw the internal Exception
      if (connection == null) {
        throw new InternalException();
      }
      id = DatabaseInserter.insertUserAccount(userId, accountId, connection);
    } catch (DatabaseInsertException e) {
      throw new InternalException(e.getMessage());
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    }
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.datainterface.DataInserter#insertMessage(int, java.lang.String)
   */
  @Override
  public int insertMessage(int userId, String message)
      throws InternalException, InteractionException {
    int id = -1;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // then try to call the method provided in the database
    // you have to make sure that it will not be changed
    // if there is error then raise the internalException
    try {
      // if we can not make the connection, just throw the internal Exception
      if (connection == null) {
        throw new InternalException();
      }
      id = DatabaseInserter.insertMessage(userId, message, connection);
    } catch (DatabaseInsertException e) {
      throw new InternalException(e.getMessage());
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    }
    return id;

  }
}

package com.bank.databasehelper;

import com.bank.database.DatabaseUpdater;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.datainterface.DataUpdator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUpdate extends DatabaseUpdater implements DataUpdator {

  private DataOperator below;
  private DataOperator upper;

  // Error Messenger
  private static StdLog errmsger = StdLogImpl.LOGGER;


  private DataSelector databaseSelectHelper;

  /**
   * Decorator pattern. DatabaseUpdate.
   * 
   * @param op The below DataOperator.
   */
  DatabaseUpdate(DataOperator op) {
    // Every aspect of dataoperator consists into a circle.
    // It will insert itself into the dataoperator.
    // Imagine a ring.
    DataOperator pre = op.getUpper();
    pre.setBelow(this);
    op.setUpper(this);
    this.below = op;
    this.upper = pre;
    this.databaseSelectHelper = DataOperator.select(this);
  }

  /**
   * Set the above DataOperator.
   * 
   * @param upper The DataOperator which takes the current object as argument to map.
   */
  @Override
  public void setUpper(DataOperator upper) {
    this.upper = upper;
  }

  /**
   * Get the DataOperator that maps the current object.
   * 
   * @return The DataOperator that maps the current object.
   */
  @Override
  public DataOperator getUpper() {
    return this.upper;
  }

  /**
   * Set the below DataOperator.
   * 
   * @param below The DataOperator which is mapped by the current object.
   */
  @Override
  public void setBelow(DataOperator below) {
    this.below = this;
  }

  /**
   * Get the DataOperator that is mapped by the current object.
   * 
   * @return The DataOperator that is mapped by the current object.
   */
  @Override
  public DataOperator getBelow() {
    return this.below;
  }



  /**
   * Return true if input is one of the role.
   * 
   * @param r The name of a role.
   * @return True if it exists.
   */
  private static boolean oneOfRole(String r) {
    // loop thorough all the role in the Role
    // to check whether the input string r is
    // one of the role name
    for (Roles eachrole : Roles.values()) {
      // if so, return ture
      if (eachrole.toString().equals(r)) {
        return true;
      }
    }
    // else, return false
    return false;
  }

  /**
   * Update the name of a role.
   * 
   * @param name The name of the role.
   * @param id The id of the role.
   * @return the boolean that represent whether we insert the role correctly.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateRoleName(String name, int id)
      throws InternalException, InteractionException {
    // Check validity
    // the input name should not be empty and x is one of the role in the enum
    new CheckValidity<String>(name).valid(x -> x != null,
        new InteractionException("Invalid input : NULL")).valid(x -> oneOfRole(x),
            new InteractionException("Inexistsent Name"));
    // the input id should be a non-negative number
    // and also when get the role from the database
    // and this will not affect the final project
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid Id")).valid(
            x -> !databaseSelectHelper.getRole(x).equals(""),
            new InteractionException("Inexistent Id"));
    // initialize the connection and then try to make the connection to the datbase
    // if this process return a null as a connection
    // then raise the error
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
    try {
      // try to close the database
      // if there is error in closing the database
      // then just raise the error
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }



  }

  /**
   * Update the User's name of a user.
   * 
   * @param name The new name of the user.
   * @param id the id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateUserName(String name, int id)
      throws InteractionException, InternalException {
    // Check validity
    // the input name should not be null
    // we can get the user detail from the database
    new CheckValidity<String>(name).valid(x -> x != null,
        new InteractionException("Invalid Inpu bt : Null"));
    // check whether we can get the user id from the database
    // if not, raise the interaction exception
    new CheckValidity<Integer>(id).valid(x -> databaseSelectHelper.getUserDetails(x) != null,
        new InteractionException("Invalid Id: inexistent. "));
    // initialize the connetion
    Connection connection = null;
    // check whether the connection is valid or not
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    // use the complete the store the boolean whether update is succeeded
    boolean complete = DatabaseUpdater.updateUserName(name, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }

  }

  /**
   * Update the age of the user.
   * 
   * @param age The new age of the user.
   * @param id The new id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateUserAge(int age, int id)
      throws InternalException, InteractionException {
    // the age should be a non-negative number
    new CheckValidity<Integer>(age).valid(x -> x >= 0,
        new InteractionException("Invalid age."));
    // try to check the input id is valid
    new CheckValidity<Integer>(id).valid(x -> databaseSelectHelper.getUserDetails(x) != null,
        new InteractionException("Invalid Id."));
    // initialize the connection
    Connection connection = null;
    // connect to the database
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    boolean complete = DatabaseUpdater.updateUserAge(age, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }

  }

  /**
   * Update the Role of the User.
   * 
   * @param roleId The new roleId, must be one of role table.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */

  @SuppressWarnings("finally")
  public boolean updateUserRole(int roleId, int id)
      throws InteractionException, InternalException {
    // the role id should be a non-negative number
    new CheckValidity<Integer>(roleId).valid(x -> x >= 0,
        new InteractionException("Invalid RoleId")).valid(
            x -> !databaseSelectHelper.getRole(x).equals(""),
            new InteractionException("Inexistent RoleId"));
    // then use the id should be non-negative either
    // the id should refer to one of the role details in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid UserId")).valid(
            x -> databaseSelectHelper.getUserDetails(x) != null,
            new InteractionException("Inexistent UserId"));
    // initialize the connection to be none
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // initialize the connection
    if (connection == null) {
      throw new InternalException();
    }
    // try to use the method given in the updateUserRole
    boolean complete = DatabaseUpdater.updateUserRole(roleId, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }
  }

  /**
   * Update the user's address.
   * 
   * @param address The address of the user.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateUserAddress(String address, int id)
      throws InteractionException, InternalException {
    // the address should not be empty
    // then street address should also less than 100
    new CheckValidity<String>(address).valid(x -> x != null,
        new InteractionException("Invalid Input: Null")).valid(x -> x.length() <= 100,
            new InteractionException("Address length must be not greater than 100."));
    // the input userid should be non-negative either
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid input: UserId < 0")).valid(
            x -> databaseSelectHelper.getUserDetails(x) != null,
            new InteractionException("Inexistent UserId"));
    // try to initialize the connetion
    Connection connection = null;
    // then try to connect to the database
    // if the connection still be null,
    // then throw the internalExceptionError
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    boolean complete = DatabaseUpdater.updateUserAddress(address, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }
  }

  /**
   * Update the name of the account.
   * 
   * @param name The new name of the account.
   * @param id The id of the account.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateAccountName(String name, int id)
      throws InteractionException, InternalException {
    // the input name should not be empty
    new CheckValidity<String>(name).valid(x -> x != null,
        new InteractionException("Invalid Input : Null"));
    // the input id should be non-negative
    // the input id should refer to one of the account
    // in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid input: AccountId < 0")).valid(
            x -> databaseSelectHelper.getAccountDetails(x) != null,
            new InteractionException("Inexistent AccountId"));
    // initialize the connection
    Connection connection = null;
    // try to connect to the database
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    boolean complete = DatabaseUpdater.updateAccountName(name, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return complete;
    }

  }

  /**
   * Update the account balance.
   * 
   * @param balance The balance.
   * @param id The account Id.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateAccountBalance(BigDecimal balance, int id)
      throws InteractionException, InternalException {
    // the balance should be in the range of 0 to 1
    new CheckValidity<BigDecimal>(balance).valid(x -> x != null,
        new InteractionException("Invalid Input: balance is null"));
    // then check the validity of the id
    // the id should be larger than 0
    // the id should also refer to one of the type in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid Account Id: smaller than zero")).valid(
            x -> databaseSelectHelper.getAccountDetails(x) != null,
            new InteractionException(" Invalid Account Id: Inexistent."));
    // try to connect to the database
    Connection connection = null;
    // if the connection is still empty
    // then raise the exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    balance = balance.setScale(2, BigDecimal.ROUND_HALF_UP);
    boolean complete = DatabaseUpdater.updateAccountBalance(balance, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }
  }

  /**
   * Update the account type.
   * 
   * @param typeId The id of the type.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateAccountType(int typeId, int id)
      throws InteractionException, InternalException {
    // the type id should be non-negative
    // the type id should refer to one type of the account name
    new CheckValidity<Integer>(typeId).valid(x -> x >= 0,
        new InteractionException("Invalid type Id: smaller than zero")).valid(
            x -> !databaseSelectHelper.getAccountTypeName(x).equals(""),
            new InteractionException("Inexistent type Id"));
    // check whether the id is non-negative
    // check whether the id can refer to one kind of the account in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid Account Id: smaller than zero")).valid(
            x -> databaseSelectHelper.getAccountDetails(x) != null,
            new InteractionException(" Invalid Account Id: Inexistent."));
    // try to connect to the database
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountType(typeId, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }


  }

  /**
   * This method is used to check whether the input name has been in the enum.
   * 
   * @param name the name that need to check whether is located in the enum
   * @return true if name is in enum, false otherwise
   */
  private static boolean oneOfAccountTypeName(String name) {
    // loop thorough the whole enum to make sure that the name is in the datbase
    // or not
    // if so, return true
    // else, return false
    for (AccountTypes ty : AccountTypes.values()) {
      if (ty.toString().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Update the name of the account type.
   * 
   * @param name The new name.
   * @param id The id of the account type.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateAccountTypeName(String name, int id)
      throws InteractionException, InternalException {
    // the name should not be invalid
    // the name should be one of the name in the enum
    new CheckValidity<String>(name).valid(x -> x != null,
        new InteractionException("Invalid Input: name is null")).valid(x -> oneOfAccountTypeName(x),
            new InteractionException("Invalid input: inexistent Account type name"));
    // the id should be none invalid
    // the id should refer to one of the account in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid type Id: smaller than zero")).valid(
            x -> !databaseSelectHelper.getAccountTypeName(x).equals(""),
            new InteractionException("Inexistent type Id"));
    // try to connet to the database
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountTypeName(name, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }

  }

  /**
   * Update the account type's interest rate.
   * 
   * @param interestRate The new interest rate.
   * @param id The account type id.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  @SuppressWarnings("finally")
  public boolean updateAccountTypeInterestRate(BigDecimal interestRate, int id)
      throws InteractionException, InternalException {
    // the interest rate should between the 0 and 0.2
    new CheckValidity<BigDecimal>(interestRate).valid(x -> x != null,
        new InteractionException("Invalid input: interestRate is null")).valid(
            x -> interestRate.compareTo(BigDecimal.ZERO) >= 0,
            new InteractionException("Invalid input: interest rate must be >= 0 <= 0.2")).valid(
                x -> interestRate.compareTo(new BigDecimal("0.2")) <= 0,
                new InteractionException("Invalid input: interest rate must be >= 0 <= 0.2"));
    // the id should be non-negative
    // the id should refer to one of the true type in the database
    new CheckValidity<Integer>(id).valid(x -> x >= 0,
        new InteractionException("Invalid input: account type id smaller than 0")).valid(
            x -> databaseSelectHelper.getAccountTypeName(x) != null,
            new InteractionException("Inexistent account type id"));
    // try to connect to the database
    Connection connection = null;
    // try to update the data using the corresponding method
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateAccountTypeInterestRate(interestRate, id,
        connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }
  }

  @SuppressWarnings("finally")
  @Override
  public boolean updateUserPassword(String password, int id)
      throws InteractionException, InternalException {
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserPassword(password, id, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }


  }

  @SuppressWarnings("finally")
  @Override
  public boolean updateUserMessageState(int userMessageId)
      throws InteractionException, InternalException {
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdater.updateUserMessageState(userMessageId, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }
  }

  /**
   * Update the state of the user message to unviewed.
   * 
   * @param userMessageId the id of the message that has been viewed.
   * @param connection connection to the database.
   * @return true if successful, false o/w.
   */
  protected static boolean unviewed(int id, Connection connection) {
    String sql = "UPDATE USERMESSAGES SET VIEWED = ? WHERE ID = ?";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, 0);
      preparedStatement.setInt(2, id);
      preparedStatement.executeUpdate();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Make message viewed.
   * 
   * @param userMessageId The id of the message.
   * @return true if success.
   * @throws InteractionException When unexpected input.
   * @throws InternalException When unexpected happened.
   */
  @SuppressWarnings("finally")
  public boolean updateUserMessageunviewed(int userMessageId)
      throws InteractionException, InternalException {
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    boolean complete = DatabaseUpdate.unviewed(userMessageId, connection);
    // try to close the database
    // if there is error just print the error message
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(),
          e.getMessage()));
    } finally {
      return complete;
    }
  }


}

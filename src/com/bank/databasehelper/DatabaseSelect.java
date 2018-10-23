package com.bank.databasehelper;

import com.bank.database.DatabaseSelector;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.tools.Dynatuple;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSelect extends DatabaseSelector implements DataSelector {


  private DataOperator below;
  private DataOperator upper;

  // Standard error log
  private static StdLog errmsger = StdLogImpl.LOGGER;


  /**
   * Decorator pattern. DatabaseSelect.
   * 
   * @param op The below DataOperator.
   */
  DatabaseSelect(DataOperator op) {
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


  /**
   * Use this the get the role name of a role.
   * 
   * @param id the id of the role
   * @return the name of the role, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public String getRole(int id) throws InternalException {
    // initialize the role to store the role from the database
    // then make connection to the database
    String role = null;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we fail to make the connection, then throw the internal exception
    if (connection == null) {
      throw new InternalException();
    }
    // then try to use the method provided in the database selector
    // to make sure that it can be fixed and print the hint message to the
    // screen
    try {
      role = DatabaseSelector.getRole(id, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    // then try to close the database
    // if there is something wrong with closing the database
    // then print the message to the user
    try {
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      // since at this case, we successfully extract the role
      // then we should return the role
      return role;
    }
  }

  /**
   * Use this to get the hashed password of a user.
   * 
   * @param userId the id of the user
   * @return the hashed password of the suer, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public String getPassword(int userId) throws InternalException {
    // initialize the hash password to store the password got from the database
    // try to make the connection to the database
    String hashPassword = null;
    Connection connection = null;
    // if we can not make the connection, just throw the internal Exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    // then try to extract the password from the database
    // if we have the sql exception
    // just raise the internal Exception
    try {
      hashPassword = DatabaseSelector.getPassword(userId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    // try to close the database
    // if there is error just get the error message
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      // return the hash password regardless the error in closing the database
      return hashPassword;
    }
  }

  /**
   * Use this to get detail informations about a user.
   * 
   * @param userId the id of the user
   * @return a User which contains all of the information about the user, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public Dynatuple<String, Dynatuple<Integer, String>> getUserDetails(int userId)
      throws InternalException {
    // initialize the tuple to store the information of the given userId
    Dynatuple<String, Dynatuple<Integer, String>> user = null;

    Connection connection = null;
    // if we can not make the connection, just throw the internal Exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    try {
      // try to get the result set from the database using the method
      // provide in the database
      // get all the information including the name, age, address
      // from the tuple
      // use the dynatuple to store all the information
      ResultSet results = DatabaseSelector.getUserDetails(userId, connection);
      results.next();
      String name = results.getString("NAME");
      int age = results.getInt("AGE");
      String address = results.getString("ADDRESS");
      Dynatuple<Integer, String> info = new Dynatuple<Integer, String>(age, address);
      user = new Dynatuple<String, Dynatuple<Integer, String>>(name, info);
    } catch (SQLException e) {
      // if there is error, then throw the internalException
      // print the error message and print the trace information
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return user;
    }
  }

  /**
   * Use this to get all of the accout ids of a user.
   * 
   * @param userId the id of the user
   * @return a list containing all the account ids
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public List<Integer> getAccountIds(int userId) throws InternalException {
    List<Integer> listIds = new ArrayList<Integer>();
    Connection connection = null;
    // if we can not make the connection, just throw the internal Exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    try {
      // try to use the getAccountId method provided in the database
      // if there is error, just throw the error
      ResultSet results = DatabaseSelector.getAccountIds(userId, connection);
      while (results.next()) {
        listIds.add(results.getInt("ACCOUNTID"));
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return listIds;
    }
  }

  /**
   * Use this to get all of the informations about a account.
   * 
   * @param accountId the id of the account
   * @return a Account which contains all the information about the account, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public Dynatuple<String, BigDecimal> getAccountDetails(int accountId)
      throws InternalException {
    // initialize the tuple to store for the information that extracted from the database
    // then we can move to the next stage
    Dynatuple<String, BigDecimal> acc = null;
    Connection connection = null;
    // if we can not make the connection, just throw the internal Exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    try {
      // try to get the information from the database with the help of
      // the provided method in the database
      // then store the name, balance to the Dynatuple
      // the tuple is function like a storage of the whole information for
      // one account
      ResultSet results = DatabaseSelector.getAccountDetails(accountId, connection);
      results.next();
      String name = results.getString("NAME");
      BigDecimal balance = new BigDecimal(results.getString("BALANCE"));
      acc = new Dynatuple<String, BigDecimal>(name, balance);
    } catch (SQLException e) {
      // if there is error calling the provided method
      // then throw the internalException
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return acc;
    }
  }


  /**
   * Use this to get the balance of a account.
   * 
   * @param accountId the id of the account
   * @return the balance of the account, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public BigDecimal getBalance(int accountId) throws InternalException {
    // initialize the balance to store the balance from the database
    BigDecimal balance = null;
    // then try to initialize the connection
    Connection connection = null;
    // if we can not make the connection, just throw the internal Exception
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    if (connection == null) {
      throw new InternalException();
    }
    try {
      balance = DatabaseSelector.getBalance(accountId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return balance;
    }
  }

  /**
   * Use this to get the interest rate of a account type.
   * 
   * @param accountType the id of the account type
   * @return the interest rate of the account type, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public BigDecimal getInterestRate(int accountType) throws InternalException {
    // initialize the interestRate to store the information for the interestRate
    BigDecimal interestRate = null;
    // initialize the connection to null
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    try {
      interestRate = DatabaseSelector.getInterestRate(accountType, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return interestRate;
    }
  }

  /**
   * Use this to get all the account type ids in the database.
   * 
   * @return a list containing all the type ids in the database
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public List<Integer> getAccountTypesIds() throws InternalException {
    List<Integer> ids = new ArrayList<>();
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    try {
      ResultSet results = DatabaseSelector.getAccountTypesId(connection);
      while (results.next()) {
        ids.add(results.getInt("ID"));
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return ids;
    }
  }

  /**
   * Use this to get the name of a account type.
   * 
   * @param accountTypeId the id of the account type
   * @return the name of the account type, null if nor succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public String getAccountTypeName(int accountTypeId) throws InternalException {
    // initialize the result to store the information from the database
    String result = null;
    // initialize the conention
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    try {
      result = DatabaseSelector.getAccountTypeName(accountTypeId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return result;
    }
  }

  /**
   * Use this to get all of the role ids in the database.
   * 
   * @return a list containing all of the role ids in the database
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public List<Integer> getRoles() throws InternalException {
    // initialize the List which is used for storing the role ids
    List<Integer> ids = new ArrayList<>();
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    try {
      ResultSet results = DatabaseSelector.getRoles(connection);
      while (results.next()) {
        ids.add(results.getInt("ID"));
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return ids;
    }
  }

  /**
   * Use this to get the account type of a account.
   * 
   * @param accountId the id of the account
   * @return the account type id of the account, -1 otherwise
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public int getAccountType(int accountId) throws InternalException {
    // initialize the account type to -1
    // which means that if we can not extract the value correctly
    int result = -1;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    // try to get the AccountType
    try {
      result = DatabaseSelector.getAccountType(accountId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return result;
    }
  }

  /**
   * Use this to get the role of a user.
   * 
   * @param userId the id of the user
   * @return the role id of the user, -1 otherwise
   * @throws InternalException if there is something wrong to deal with the database
   */
  @SuppressWarnings("finally")
  public int getUserRole(int userId) throws InternalException {
    // result is equal to -1
    // if there is something wrong then just return the -1
    // then we can find this in the bank to do further
    int result = -1;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    // try to get the UserRole
    try {
      result = DatabaseSelector.getUserRole(userId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }
    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return result;
    }
  }

  @SuppressWarnings("finally")
  @Override
  public List<Integer> getAllMessage(int userId)
      throws InternalException, InteractionException {
    List<Integer> allMessage = new ArrayList<>();

    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    // try to get the UserRole
    try {
      ResultSet results = DatabaseSelector.getAllMessages(userId, connection);
      while (results.next()) {
        allMessage.add(results.getInt("ID"));
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }

    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return allMessage;
    }
  }

  @SuppressWarnings("finally")
  @Override
  public String getSpecficMessage(int messageId)
      throws InternalException, InteractionException {
    String result = "";
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }

    try {
      result = DatabaseSelector.getSpecificMessage(messageId, connection);
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }

    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return result;
    }
  }


  /**
   * Get a specific message view state from the database.
   * 
   * @param messageId the id of the message.
   * @param connection connection to the database.
   * @return the message from the database as a string.
   * @throws SQLException if something goes wrong.
   */
  protected static int getSpecificMessageView(int messageId, Connection connection)
      throws SQLException {
    String sql = "SELECT VIEWED FROM USERMESSAGES WHERE ID = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setInt(1, messageId);
    return preparedStatement.executeQuery().getInt("VIEWED");
  }

  /**
   * Get the view status of the message.
   * 
   * @param userMessageId The id of the message of the User.
   * @return true if viewed.
   * @throws InteractionException When unexpected input.
   * @throws InternalException When unexpected happened.
   */

  @SuppressWarnings("finally")
  public boolean getSpecificView(int userMessageId)
      throws InteractionException, InternalException {
    boolean result = true;
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }

    try {
      int tem = DatabaseSelect.getSpecificMessageView(userMessageId, connection);
      if (tem == 1) {
        result = true;
      } else {
        result = false;
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }

    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return result;
    }
  }

  @SuppressWarnings("finally")
  @Override
  public List<Integer> getAllUserIds() throws InternalException, InteractionException {
    List<Integer> allUserId = new ArrayList<>();
    Connection connection = null;
    connection = DatabaseDriverHelper.connectOrCreateDataBase();
    // if we can not make the connection, just throw the internal Exception
    if (connection == null) {
      throw new InternalException();
    }
    // try to get the UserRole
    try {
      ResultSet results = DatabaseSelector.getUsersDetails(connection);
      while (results.next()) {
        allUserId.add(results.getInt("ID"));
      }
    } catch (SQLException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        errmsger.outputMsg("Error. Recommend Restart the System.");
      }
    }

    try {
      // then try to close the database
      // if there is something wrong with closing the database
      // then print the message to the user
      connection.close();
    } catch (SQLException e) {
      errmsger.outputMsg(String.format("%s: %d \n %s",
          e.getClass().toString(),
          e.getErrorCode(), e.getMessage()));
    } finally {
      return allUserId;
    }
  }


}

package com.bank.datainterface;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.tools.Dynatuple;
import java.math.BigDecimal;
import java.util.List;

/**
 * The DataSelector interface for account and user. Only supporting selecting.
 * 
 * @author jinende
 *
 */
public interface DataSelector extends DataOperator {
  /**
   * Use this the get the role name of a role.
   * 
   * @param id the id of the role
   * @return the name of the role, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public String getRole(int id) throws InternalException;

  /**
   * Use this to get the hashed password of a user.
   * 
   * @param userId the id of the user
   * @return the hashed password of the suer, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public String getPassword(int userId) throws InternalException;

  /**
   * Use this to get detail informations about a user.
   * 
   * @param userId the id of the user
   * @return a User which contains all of the information about the user, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public Dynatuple<String, Dynatuple<Integer, String>> getUserDetails(int userId)
      throws InternalException;

  /**
   * Use this to get all of the accout ids of a user.
   * 
   * @param userId the id of the user
   * @return a list containing all the account ids
   * @throws InternalException if there is something wrong to deal with the database
   */
  public List<Integer> getAccountIds(int userId) throws InternalException;

  /**
   * Use this to get all of the informations about a account.
   * 
   * @param accountId the id of the account
   * @return a Account which contains all the information about the account, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public Dynatuple<String, BigDecimal> getAccountDetails(int accountId) throws InternalException;

  /**
   * Use this to get the balance of a account.
   * 
   * @param accountId the id of the account
   * @return the balance of the account, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public BigDecimal getBalance(int accountId) throws InternalException;

  /**
   * Use this to get the interest rate of a account type.
   * 
   * @param accountType the id of the account type
   * @return the interest rate of the account type, null if not succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public BigDecimal getInterestRate(int accountType) throws InternalException;

  /**
   * Use this to get all the account type ids in the database.
   * 
   * @return a list containing all the type ids in the database
   * @throws InternalException if there is something wrong to deal with the database
   */
  public List<Integer> getAccountTypesIds() throws InternalException;

  /**
   * Use this to get the name of a account type.
   * 
   * @param accountTypeId the id of the account type
   * @return the name of the account type, null if nor succeed
   * @throws InternalException if there is something wrong to deal with the database
   */
  public String getAccountTypeName(int accountTypeId) throws InternalException;

  /**
   * Use this to get all of the role ids in the database.
   * 
   * @return a list containing all of the role ids in the database
   * @throws InternalException if there is something wrong to deal with the database
   */
  public List<Integer> getRoles() throws InternalException;

  /**
   * Use this to get the account type of a account.
   * 
   * @param accountId the id of the account
   * @return the account type id of the account, -1 otherwise
   * @throws InternalException if there is something wrong to deal with the database
   */
  public int getAccountType(int accountId) throws InternalException;

  /**
   * Use this to get the role of a user.
   * 
   * @param userId the id of the user
   * @return the role id of the user, -1 otherwise
   * @throws InternalException if there is something wrong to deal with the database
   */
  public int getUserRole(int userId) throws InternalException;

  /**
   * Get all the message id of the user.
   * 
   * @return context.
   * @throws InternalException if unexpected happened.
   * @throws InteractionException if unexpected input.
   */
  public List<Integer> getAllMessage(int userId)
      throws InternalException, InteractionException;

  /**
   * Check if specific message is viewed.
   * 
   * @param messageId the id of the message.
   * @return true if viewed.
   * @throws InternalException if unexpected happened.
   */
  public String getSpecficMessage(int messageId)
      throws InternalException, InteractionException;

  /**
   * get all the user ids of the database.
   * 
   * @return the list of id of all the user id.
   * @throws InternalException if unexpected happened.
   * @throws InteractionException If unexpected input.
   */
  public List<Integer> getAllUserIds()
      throws InternalException, InteractionException;

}

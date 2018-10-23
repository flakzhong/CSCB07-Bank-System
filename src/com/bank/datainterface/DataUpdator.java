package com.bank.datainterface;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import java.math.BigDecimal;

/**
 * The DataUpdator for account and user. Only supporting update information.
 * 
 * @author jinende
 *
 */
public interface DataUpdator extends DataOperator {
  /**
   * Update the name of a role.
   * 
   * @param name The name of the role.
   * @param id The id of the role.
   * @return the boolean that represent whether we insert the role correctly.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateRoleName(String name, int id) throws InternalException, InteractionException;

  /**
   * Update the User's name of a user.
   * 
   * @param name The new name of the user.
   * @param id the id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateUserName(String name, int id) throws InteractionException, InternalException;

  /**
   * Update the age of the user.
   * 
   * @param age The new age of the user.
   * @param id The new id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateUserAge(int age, int id) throws InternalException, InteractionException;

  /**
   * Update the Role of the User.
   * 
   * @param roleId The new roleId, must be one of role table.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateUserRole(int roleId, int id) throws InteractionException, InternalException;

  /**
   * Update the user's address.
   * 
   * @param address The address of the user.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateUserAddress(String address, int id)
      throws InteractionException, InternalException;

  /**
   * Update the name of the account.
   * 
   * @param name The new name of the account.
   * @param id The id of the account.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateAccountName(String name, int id)
      throws InteractionException, InternalException;

  /**
   * Update the account balance.
   * 
   * @param balance The balance.
   * @param id The account Id.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateAccountBalance(BigDecimal balance, int id)
      throws InteractionException, InternalException;

  /**
   * Update the account type.
   * 
   * @param typeId The id of the type.
   * @param id The id of the user.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateAccountType(int typeId, int id)
      throws InteractionException, InternalException;

  /**
   * Update the account type's interest rate.
   * 
   * @param interestRate The new interest rate.
   * @param id The account type id.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateAccountTypeInterestRate(BigDecimal interestRate, int id)
      throws InteractionException, InternalException;

  /**
   * Update the name of the account type.
   * 
   * @param name The new name.
   * @param id The id of the account type.
   * @return True if success.
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException if there is error to deal with the database
   */
  public boolean updateAccountTypeName(String name, int id)
      throws InteractionException, InternalException;

  /**
   * Update the user's password to given user with id.
   * 
   * @param password the new password.
   * @param id The new id.
   * @return true if success.
   * @throws InteractionException if unexpected input.
   * @throws InternalException If unexpected happened.
   */
  public boolean updateUserPassword(String password, int id)
      throws InteractionException, InternalException;

  /**
   * Update the message status to viewed.
   * 
   * @param userMessageId The id of message.
   * @return true if success.
   * @throws InteractionException if unexpected input.
   * @throws InternalException If unexpected happened.
   */
  public boolean updateUserMessageState(int userMessageId)
      throws InteractionException, InternalException;
}

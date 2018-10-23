package com.bank.datainterface;

import com.bank.database.DatabaseInsertException;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import java.math.BigDecimal;

/**
 * The interface for Account and User. Only supporting inserting into key-value map.
 * 
 * @author jinende
 *
 */
public interface DataInserter extends DataOperator {
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
      throws InteractionException, InternalException;


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
      throws InteractionException, InternalException;

  /**
   * Use this to insert a new user.
   * 
   * @param name the user's name.
   * @param age the user's age.
   * @param address the user's address.
   * @param roleId the user's role.
   * @param password the user's password (not hashsed).
   * @return the account id
   * @throws DatabaseInsertException if there is a failure on the insert
   */
  public int insertNewUser(String name, int age, String address, int roleId, String password)
      throws InteractionException, InternalException;

  /**
   * Use this to insert new roles into the database.
   * 
   * @param role the new role to be added.
   * @return the id of the role that was inserted.
   * @throws DatabaseInsertException on failure.
   */
  public int insertRole(String role) throws InteractionException, InternalException;

  /**
   * Use this to make a account belong to the given user.
   *
   * @param userId the id of the user
   * @param accountId the id of the account
   *
   * @return accountId the id of the account
   * @throws InternalException if there is functional error in the database
   */
  public int insertUserAccount(int userId, int accountId) throws InternalException;


  /**
   * Insert a new Message.
   * 
   * @param userId as Id of receiver.
   * @param message as the context of message.
   * @return id the the new message.
   * @throws InternalException if unexpected happened.
   * @throws InteractionException If unexpected input.
   */
  public int insertMessage(int userId, String message)
      throws InternalException, InteractionException;

}

package com.bank.bank;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;
import java.math.BigDecimal;
import java.util.List;

/**
 * The terminal for admin.
 * 
 * @author jinende
 *
 */
public interface AdminTerminal {
  /**
   * Return the id of the newly-created admin.
   * 
   * @param name the name of the admin
   * @param age the age of the admin
   * @param address the address of the admin
   * @param password the password of the admin
   * @return the user id of the newly-created admin
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public int makeNewAdmin(String name, int age, String address, String password)
      throws InternalException, InteractionException;

  /**
   * Return a list of users.
   * 
   * @return a list of users.
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public List<User> allUsers() throws InternalException, InteractionException;

  /**
   * Return a list of customers.
   * 
   * @return a list of customers
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public List<Customer> allCustomers() throws InternalException, InteractionException;

  /**
   * Return a list of admins.
   * 
   * @return a list of admin
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public List<Admin> allAdmins() throws InternalException, InteractionException;

  /**
   * Return a list of tellers.
   * 
   * @return a list of tellers
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public List<Teller> allTellers() throws InternalException, InteractionException;

  /**
   * This method will return all the balance of a customer that was stored in the bank.
   * 
   * @param userId the id of the user
   * @return the Bigdecimal represents sum of all the balance from all the accounts
   * @throws InternalException the userId do not exisgt in the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public BigDecimal balanceOfaCustomer(int userId) throws InternalException, InteractionException;

  /**
   * This method will return the sum of balance of all the account stored in the bank.
   * 
   * @return the BigDeciaml that represent the sum of all the balance in the bank
   * @throws InternalException the user input the invalid input
   * @throws InteractionException there is something wrong dealing the database
   */
  public BigDecimal balanceOfAllAcount() throws InternalException, InteractionException;

  /**
   * This method will promote a teller to the admin.
   * 
   * @param tellerId of the teller that will be promoted
   * @return whether the process has succeeded
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public boolean promoteTeller(int tellerId) throws InternalException, InteractionException;

  /**
   * This method will extract the message of the user with the given message id from the database.
   * 
   * @param messageId the id of the new message
   * @return the string that represent the message
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the message with given id does not stored in the database
   */
  public String readMessage(int messageId) throws InternalException, InteractionException;

  /**
   * This method will return a list of all the message id in the database.
   * 
   * @return the list of integer that represents the id of the all message from the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the datbase
   */
  public List<Integer> listAllMessages() throws InternalException, InteractionException;

  /**
   * This method will return all message with the given id that store in the database.
   * 
   * @param messageId the id of the message stored in the database
   * @return the string of the message stored in the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the message of given message id does not exist in the database
   */
  public String viewMessage(int messageId) throws InternalException, InteractionException;

  /**
   * This method will mutate the status of the message from unviewed to viewed.
   * 
   * @param messageId the id of the message that will need to modified the status
   * @return the boolean that represent whether the process has succeeded or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the message with the given messageid does not exist in the
   *         database
   */
  public boolean updateMessagestatus(int messageId) throws InternalException, InteractionException;

  /**
   * This message will leave the message to the user with the given userid.
   * 
   * @param userId the id of message receiver
   * @param message the to the receiver
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user does not exist in the database
   */
  public Message leaveMessage(int userId, String message)
      throws InternalException, InteractionException;

  /**
   * Try to serialize the database.
   * 
   * @return boolean whether the process is succeed or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public boolean databaseDaemon()
      throws InternalException, InteractionException;

  /**
   * Try to deserialize the database.
   * 
   * @return boolean whether the process is succeed or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public boolean databaseRecover()
      throws InternalException, InteractionException;

  public boolean transitAccountType(int accountId, AccountTypes accounttype)
      throws InternalException, InteractionException;
}

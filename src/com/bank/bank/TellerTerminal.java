package com.bank.bank;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.generics.AccountTypes;
import java.math.BigDecimal;
import java.util.List;

/**
 * The terminal for teller.
 * 
 * @author jinende
 *
 */
public interface TellerTerminal extends Atm {
  /**
   * This method is used for create new account.
   * 
   * @param name the name of the new account
   * @param balance the balance of the new account
   * @param type the type of the new account
   * @return the id of the new account that has been created
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public int makeNewAccount(String name, BigDecimal balance, AccountTypes type)
      throws InternalException, InteractionException;

  /**
   * This method is used for creating the new customer.
   * 
   * @param name the name of the new customer
   * @param age the age of the new customer
   * @param address the address of the new customer
   * @param password the password of the new customer
   * @return the id of the new customer stored in the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public int makeNewCustomer(String name, int age, String address, String password)
      throws InternalException, InteractionException;

  /**
   * This method is used for giving the interests to the account with given accout id.
   * 
   * @param accountId the id of the account that will be given interest
   * @return true if the process function successfully, false otherwise
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public boolean giveInterest(int accountId) throws InternalException, InteractionException;


  /**
   * This method will return all the balance of the current customer.
   * 
   * @return the BigDecimal that represent the sum of the current customer
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public BigDecimal allBalanceOfCurrentCusomter()
      throws InternalException, InteractionException;

  /**
   * This method will set the Age of the current customer.
   * 
   * @param newage the new age of the current customer
   * @return the boolean represent whether the process has succeeded or not
   * @throws InternalException there is something dealing with the database
   * @throws InteractionException the new age is not a valid age
   */
  public boolean setCustomerAge(int newage)
      throws InternalException, InteractionException;

  /**
   * This method will set the Address of the current customer.
   * 
   * @param newAddress for the customer
   * @return the boolean represent whether the process has succeeded or not
   * @throws InternalException there is something dealing with the database
   * @throws InteractionException the new age is not a valid age
   */
  public boolean setCustomerAddress(String newAddress)
      throws InternalException, InteractionException;

  /**
   * This method will update the customer name to the new given name.
   * 
   * @param newName the new name for the customer
   * @return the boolean represent whether the process has succeeded or not
   * @throws InternalException there is something dealing with the database
   * @throws InteractionException the new age is not a valid age
   */
  public boolean setCustomerName(String newName)
      throws InternalException, InteractionException;

  /**
   * This method will update the customer password to the new given password.
   * 
   * @param password the new password
   * @return the boolean represent whether the process has succeeded or not
   * @throws InternalException there is something dealing with the database
   * @throws InteractionException the new age is not a valid age
   */
  public boolean setCustomerPassword(String password)
      throws InternalException, InteractionException;

  /**
   * This method will return all the message that owing to the teller.
   * 
   * @return the list of the integer that 
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public List<Integer> listAllMessageOfCurrentTeller()
      throws InternalException, InteractionException;

  /**
   * This method will return a list of Message of the current Customer.
   * 
   * @return the list of the Message
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public List<Message> listAllMessageOfCurrentCustomer()
      throws InternalException, InteractionException;

  /**
   * This method will get the specific message from the database.
   * 
   * @param messageId the id of the message that need to be extracted
   * @return the String that is the content of the string
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the message with the given id does not exist
   */
  public String checkMessage(int messageId)
      throws InternalException, InteractionException;

  /**
   * This method will send the message to the user with the given userid.
   * 
   * @param userId the id of the receiver
   * @param message the string of the content
   * @return the message class represent the sent message
   * @throws UnexposedException there is something wrong dealing 
   * @throws InteractionException the user with given userId was not stored in the database
   * @throws InternalException there is something wrong dealing with the database
   */
  public Message leaveMessage(int userId, String message)
      throws UnexposedException, InteractionException, InternalException;

  /**
   * This method will return the string of the messageID with the given messageid.
   */
  public String readMessage(int messageId)
      throws InternalException, InteractionException;



}

package com.bank.bank;

import com.bank.account.Account;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.user.Customer;
import java.math.BigDecimal;
import java.util.List;

/**
 * The terminal for Atm.
 * 
 * @author jinende
 *
 */
public interface Atm {
  /**
   * This method will return the list of account that owned by the current customer.
   * 
   * @return the list of accout that belongs to the current customer
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public List<Account> listAccounts() throws InternalException, InteractionException;

  /**
   * This method will let the current customer to deposit money to store in the database.
   * 
   * @param amount the amount of money the customer will save
   * @param accountId the id the accountID
   * @return true if the process function successfully, false otherwise
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public boolean makeDeposit(BigDecimal amount, int accountId)
      throws InternalException, InteractionException;

  /**
   * This method will let the current customer to withdraw the money that stored in the database.
   * 
   * @param amount the amount of the money the customer want to withdraw
   * @param accountId the account id of the account that will be performed the process
   * @return true if the process function successfully, false otherwise
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public boolean makeWithdrawl(BigDecimal amount, int accountId)
      throws InternalException, InteractionException;

  /**
   * This function will check the balance of the account with the given accountid.
   * 
   * @param accountId the id of the account that will be checked
   * @return the money that store in the account with the given account id
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public BigDecimal checkBalance(int accountId) throws InternalException, InteractionException;

  /**
   * This method will authenticate the customer with the given customer id and password.
   * 
   * @param customerId the customer that will be checked
   * @param password the password that need to be checked
   * @return true if the password match the one stored in the database, false otherwise
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public boolean authenticateCustomer(int customerId, String password)
      throws InternalException, InteractionException;

  /**
   * This method will deauthenticate the current customer.
   * 
   * @return ture is the process function successfully, false otherwise
   * @throws InternalException there is something wrong dealing with the database
   */
  public boolean deauthenticateCustomer() throws InternalException;

  /**
   * This method will return the get the current customer.
   * 
   * @return a customer from the Customer class
   * @throws InternalException there is something wrong dealing with the database
   */
  public Customer getCurrentCustomer() throws InternalException;

  /**
   * This message will extract the message with the given messageId from the database.
   * 
   * @param messageId the id of the message that will be withdrawn
   * @return the content of the message
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the messageId does not stored in the database yet
   */
  public String readMessage(int messageId) throws InternalException, InteractionException;

  /**
   * This method will return a list that contains integer that represent all the id of the
   * message that was stored in the database.
   * 
   * @return the list of integer that represent all the id of the message in the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is somethig wrong dealing with the database
   */
  public List<Integer> listAllMessages() throws InternalException, InteractionException;

}

package com.bank.bank;

import com.bank.account.Account;
import com.bank.databasehelper.CheckValidity;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;
import com.bank.user.Customer;
import com.bank.user.User;
import java.math.BigDecimal;
import java.util.List;

public class AtmImpl implements Atm {

  private Customer currentCustomer;

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#listAccounts()
   */
  @Override
  public List<Account> listAccounts() throws InternalException, InteractionException {
    // make sure that the customer is in the database
    CheckValidity.assert_prop(this.currentCustomer != null,
        new InteractionException("Please Log in."));
    // then get the account of the customer
    return this.currentCustomer.getAccounts();
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#makeDeposit(java.math.BigDecimal, int)
   */
  @Override
  public boolean makeDeposit(BigDecimal amount, int accountId)
      throws InternalException, InteractionException {
    // check whether the current customer has logged in
    CheckValidity.assert_prop(this.currentCustomer != null,
        new InteractionException("Please Log in."));
    // if so, then get the account of his/her account with the given account id
    Account operatedAccount = Account.getAccount(this.currentCustomer, accountId);
    String message =
        String.format("Your account (ID: %d) has been depositted $ %s.",
            accountId, amount.toString());

    return (operatedAccount.depositMoney(amount)
        && (new Message(this.getCurrentCustomer().getUserId(), message) != null));
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#makeWithdrawl(java.math.BigDecimal, int)
   */
  @Override
  public boolean makeWithdrawl(BigDecimal amount, int accountId)
      throws InternalException, InteractionException {
    // check whether the current customer has logged in
    CheckValidity.assert_prop(this.currentCustomer != null,
        new InteractionException("Please Log in."));
    // if so, then withdraw the money from the atm and call the withdraw function
    Account operatedAccount = Account.getAccount(this.currentCustomer, accountId);
    String message =
        String.format("Your account (ID: %d) has been withdrawn $ %s.",
            accountId, amount.toString());

    return (operatedAccount.withdrawalMoney(amount)
        && (new Message(this.getCurrentCustomer().getUserId(), message) != null));
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#checkBalance(int)
   */
  @Override
  public BigDecimal checkBalance(int accountId) throws InternalException, InteractionException {
    // check whether the current customer has logged in
    CheckValidity.assert_prop(this.currentCustomer != null,
        new InteractionException("Please Log in."));
    // if so, then check the money from the database
    Account operatedAccount = Account.getAccount(this.currentCustomer, accountId);
    return operatedAccount.getBalance();
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#authenticateCustomer(int, java.lang.String)
   */
  @Override
  public boolean authenticateCustomer(int customerId, String password)
      throws InternalException, InteractionException {
    // check whether the current customer has logged in
    CheckValidity.assert_prop(this.currentCustomer == null,
        new InteractionException("Please log out first."));
    // if so, then try to authenticate the current customer
    this.currentCustomer = (Customer) User.getUser(customerId, password, Roles.CUSTOMER);
    return true;
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#deauthenticateCustomer()
   */
  @Override
  public boolean deauthenticateCustomer() throws InternalException {
    // just set the current customer to be null
    this.currentCustomer = null;
    return true;
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#getCurrentCustomer()
   */
  @Override
  public Customer getCurrentCustomer() throws InternalException {
    // check whether the current customer has logged in
    CheckValidity.assert_prop(this.currentCustomer != null,
        new InternalException("Unexpected Error."));
    // if so, just return the customer stored in the atm
    return this.currentCustomer;
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#readMessage(int)
   */
  @Override
  public String readMessage(int messageId) throws InternalException, InteractionException {
    return this.getCurrentCustomer().readMessage(messageId);
  }

  /* (non-Javadoc)
   * @see com.bank.bank.Atm#listAllMessages()
   */
  @Override
  public List<Integer> listAllMessages() throws InternalException, InteractionException {
    return this.getCurrentCustomer().getAllMessageIds();
  }



}

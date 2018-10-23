package com.bank.bank;

import com.bank.account.Account;
import com.bank.account.RestrictedSaving;
import com.bank.databasehelper.CheckValidity;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Fx;
import com.bank.generics.Roles;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Teller Terminal Implements.
 * 
 * @author 72948
 *
 */
public class TellerTerminalImpl extends AtmImpl implements TellerTerminal {

  private Teller currentTeller;

  public TellerTerminalImpl(int tellerId, String password)
      throws InternalException, InteractionException {
    this.currentTeller = (Teller) User.getUser(tellerId, password, Roles.TELLER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#makeNewAccount (java.lang.String, java.math.BigDecimal,
   * com.bank.generics.AccountTypes)
   */
  @Override
  public int makeNewAccount(String name, BigDecimal balance, AccountTypes type)
      throws InternalException, InteractionException {
    // create the new account
    Account newAccount = Account.createAccount(this.getCurrentCustomer(), name, type);
    // give the money to the newly-created account
    newAccount.depositMoney(balance);
    return newAccount.getAccountId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#makeNewCustomer (java.lang.String, int, java.lang.String,
   * java.lang.String)
   */
  @Override
  public int makeNewCustomer(String name, int age, String address, String password)
      throws InternalException, InteractionException {
    // create the new customer
    User customer = Customer.createUser(name, age, address, password, Roles.CUSTOMER);
    // and then just authenticate the customer with the password that the customer just input
    this.authenticateCustomer(customer.getUserId(), password);
    return customer.getUserId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#giveInterest(int)
   */
  @Override
  public boolean giveInterest(int accountId) throws InternalException, InteractionException {
    // then try to get the acount from the database
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));
    Account newAccount = Account.getAccount(this.getCurrentCustomer(), accountId);
    // then give the interest
    this.getCurrentCustomer().newMessage("You have been given a Interest on account with id: "
        + accountId);
    return newAccount.setBalance(
        newAccount.getBalance().multiply(newAccount.getInterest().add(BigDecimal.ONE)));

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.AtmImpl#makeWithdrawl(java.math.BigDecimal, int)
   */
  @Override
  public boolean makeWithdrawl(BigDecimal amount, int accountId)
      throws InternalException, InteractionException {
    // check whether whether the current customer has loggin in
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));
    // if so then withdraw the money(the teller can help to withdraw all kinds of money)
    Account operatedAccount = Account.getAccount(this.getCurrentCustomer(), accountId);
    if (operatedAccount.getAccountType() != AccountTypes.RESTRICTED) {
      CheckValidity.assert_prop(operatedAccount.withdrawalMoney(amount),
          new InteractionException("Withdrawal cannot be done."));
    } else {
      CheckValidity.assert_prop(
          ((RestrictedSaving) operatedAccount).withdrawalMoney(amount, this.currentTeller),
          new InteractionException("Withdrawal cannot be done."));
    }
    String message =
        String.format("Your account (ID: %d) has been withdrawned $ %s.", accountId,
            amount.toString());
    this.leaveMessage(this.getCurrentCustomer().getUserId(), message);
    return true;
  }

  /**
   * A trivial mapping allowing two exception invoked.
   * 
   * @param orgList The mapped list
   * @param mapping The mapping function
   * @return A list after mapping
   * @throws InternalException If unexpected happen
   * @throws InteractionException If unexpected input
   */
  private static <Q, S> ArrayList<Q> mapWithException(List<S> orgList, Fx<S, Q> mapping)
      throws InternalException, InteractionException {
    ArrayList<Q> retList = new ArrayList<Q>(orgList.size());
    for (S eachOrg : orgList) {
      retList.add(mapping.fx(eachOrg));
    }
    return retList;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#allBalanceOfCurrentCusomter()
   */
  @Override
  public BigDecimal allBalanceOfCurrentCusomter() throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));
    Customer customer = this.getCurrentCustomer();
    ArrayList<BigDecimal> allbalances =
        mapWithException(customer.getAccounts(), x -> x.getBalance());
    return allbalances.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#setCustomerAge(int)
   */
  @Override
  public boolean setCustomerAge(int newage) throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));

    return this.getCurrentCustomer().setAge(newage);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#setCustomerAddress(java.lang.String)
   */
  @Override
  public boolean setCustomerAddress(String newAddress)
      throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));

    return this.getCurrentCustomer().setAddress(newAddress);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#setCustomerName(java.lang.String)
   */
  @Override
  public boolean setCustomerName(String newName) throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));

    return this.getCurrentCustomer().setName(newName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#setCustomerPassword(java.lang.String)
   */
  @Override
  public boolean setCustomerPassword(String password)
      throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));

    return this.getCurrentCustomer().setPassword(password);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#listAllMessageOfCurrentCustomer()
   */
  @Override
  public List<Message> listAllMessageOfCurrentCustomer()
      throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));

    List<Integer> msgIds = this.getCurrentCustomer().getAllMessageIds();
    return mapWithException(msgIds,
        x -> Message.getMessage(this.getCurrentCustomer().getUserId(), x));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#leaveMessage(int, java.lang.String)
   */
  @Override
  public Message leaveMessage(int userId, String message)
      throws UnexposedException, InteractionException, InternalException {
    CheckValidity.assert_prop(User.getUserRole(userId) == Roles.CUSTOMER,
        new InteractionException("Teller can only message to Customers."));
    return new Message(userId, message);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#checkMessage(int)
   */
  @Override
  public String checkMessage(int messageId) throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.getCurrentCustomer() != null,
        new InteractionException("Please Log in."));
    return this.getCurrentCustomer().readMessage(messageId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.AtmImpl#readMessage(int)
   */
  @Override
  public String readMessage(int messageId) throws InternalException, InteractionException {
    return this.currentTeller.readMessage(messageId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.bank.TellerTerminal#listAllMessageOfCurrentTeller()
   */
  @Override
  public List<Integer> listAllMessageOfCurrentTeller()
      throws InternalException, InteractionException {
    return this.currentTeller.getAllMessageIds();
  }



}

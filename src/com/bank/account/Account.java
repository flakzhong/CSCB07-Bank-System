package com.bank.account;

import com.bank.bankdata.AccountTypesMap;
import com.bank.databasehelper.CheckValidity;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.datainterface.DataUpdator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.user.Admin;
import com.bank.user.User;
import java.math.BigDecimal;

/**
 * abstract Class Account. The prototype and infrastructure of all types of account. When there is
 * an instance of account, it means that account is authenticated and is a 'handler'.
 * 
 * 
 * @author jinende
 *
 */
public abstract class Account {
  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);

  private static final DataUpdator DatabaseUpdateHelper = DataOperator.update(Database.DATABASE);

  private static final DataInserter DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);

  @SuppressWarnings("unused")
  private User user;
  private int accountId;

  /**
   * This method will help to insert the whole information of the account into the database.
   * 
   * @param user the user that own the account
   * @param name the name of the account
   * @param accounttype the type of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  protected Account(User user, String name, AccountTypes accounttype)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(user.getUserRole() == Roles.CUSTOMER,
        new InteractionException("We are now currently only supporting customer's account."));
    this.accountId = DatabaseInsertHelper.insertAccount(name, BigDecimal.ZERO,
        AccountTypesMap.MAP.getMapping(accounttype));
    DatabaseInsertHelper.insertUserAccount(user.getUserId(), this.getAccountId());
    this.user = user;
  }

  /**
   * This method will try to check whether the user has the account with the given account id.
   * 
   * @param user the user who is owning or going to own the account
   * @param accountId the account id that need to check
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  protected Account(User user, int accountId, AccountTypes accounttype)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(user.getUserRole() == Roles.CUSTOMER,
        new InteractionException("We are now currently only supporting customer's account."));
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getAccountType(accountId) == AccountTypesMap.MAP.getMapping(
            accounttype),
        new InternalException("Unexpected Exception: Inconsistent Accounttype."));
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getAccountIds(user.getUserId()).contains(accountId),
        new InteractionException("You don't have access to this account."));
    this.user = user;
    this.accountId = accountId;

  }

  protected Account(String name, AccountTypes accounttype)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(name != null, new InteractionException("Name could not be null."));
    this.user = null;
    this.accountId = DatabaseInsertHelper.insertAccount(name, BigDecimal.ZERO,
        AccountTypesMap.MAP.getMapping(accounttype));
  }

  protected Account(Admin admin, int accountId, AccountTypes accounttype)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(admin != null, new InteractionException("Unauthenticated Admin."));
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getAccountType(accountId) == AccountTypesMap.MAP.getMapping(
            accounttype),
        new InternalException("Unexpected Exception: Inconsistent Accounttype."));
    this.user = null;
    this.accountId = accountId;
  }

  /**
   * This method is going to create a new account by calling the corresponding in the given type.
   * 
   * @param user the user who is going to own the given account
   * @param name the name of the account
   * @param type the type of the account
   * @return the Account that store the corresponding information inputted by the user
   * @throws InteractionException the user input the invalid input
   * @throws InternalException something wrong dealing with the database
   */
  public static Account createAccount(User user, String name, AccountTypes type)
      throws InteractionException, InternalException {
    return type.createAccount().fx(user).fx(name);
  }


  /**
   * This method is going to create a new account by calling the corresponding in the given type.
   * 
   * @param name the name of the account
   * @param type the type of the type
   * @return the constructor
   * @throws InteractionException the input is invalid
   * @throws InternalException something wrong dealing with the database
   */
  public static Account createAccount(String name, AccountTypes type)
      throws InteractionException, InternalException {
    return type.createAccountWithNoUser().fx(name);
  }

  /**
   * This method will get the account with accountID from the user.
   * 
   * @param user the user who may have the account
   * @param accountId the accountId we want to check out
   * @return the Account if the account was stored at that user's(both accountId and user is
   *         correct)
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public static Account getAccount(User user, int accountId)
      throws InteractionException, InternalException {

    int accountTypeId = DatabaseSelectHelper.getAccountType(accountId);

    AccountTypes type = AccountTypes.mapAccountType(accountTypeId);

    return type.getAccount().fx(user).fx(accountId);
  }

  /**
   * This method will return the account that the admin want to get this will pass tp the
   * DatabaseSelectHelper.
   * 
   * @param admin the admin who want to extract the account
   * @param accountId the id of the account that want to extract
   * @return Account that with the accountId
   * @throws InteractionException the account with accountId does not exist in the database
   * @throws InternalException there is something wrong dealing with the database
   */
  public static Account getAccountByAdmin(Admin admin, int accountId)
      throws InteractionException, InternalException {

    int accountTypeId = DatabaseSelectHelper.getAccountType(accountId);

    AccountTypes type = AccountTypes.mapAccountType(accountTypeId);

    return type.getAccountByAdmin().fx(admin).fx(accountId);
  }

  /**
   * This method will return the account id for this account.
   * 
   * @return the accountid stored in this class
   */
  public int getAccountId() {
    return this.accountId;
  }

  /**
   * Get the name of the account from the database.
   * 
   * @return the name of the account stored in the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public String getName() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getAccountDetails(this.getAccountId()).left();
  }

  /**
   * set the name of the account into the database.
   * 
   * @param name the name of the account
   * @return boolean whether the rename process has succeeded or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public boolean setName(String name) throws InternalException, InteractionException {
    return DatabaseUpdateHelper.updateAccountName(name, this.getAccountId());

  }

  /**
   * Get the balance from the database.
   * 
   * @return the BigDecimal that represents the balance of the account
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public BigDecimal getBalance() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getBalance(this.getAccountId());
  }

  /**
   * Try to withdraw money from the given account type.
   * 
   * @param amount amount of money that will be withdraw
   * @return boolean to represent whether it is successful to withdraw the money
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public boolean withdrawalMoney(BigDecimal amount) throws InternalException, InteractionException {
    CheckValidity.assert_prop(amount.compareTo(BigDecimal.ZERO) > 0,
        new InteractionException("Amount has to be positive."));
    CheckValidity.assert_prop(this.inBalanceRange(this.getBalance().subtract(amount)),
        new InteractionException(
            String.format("You cannot withdraw %s amount of money", amount.toString())));
    return DatabaseUpdateHelper.updateAccountBalance(this.getBalance().subtract(amount),
        this.getAccountId());

  }

  /**
   * This method try to deposit the money into the database.
   * 
   * @param amount the amount of money that will be deposited into the database
   * @return the boolean to represnet whether update the date of the balance succeed or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public boolean depositMoney(BigDecimal amount) throws InternalException, InteractionException {
    CheckValidity.assert_prop(amount.compareTo(BigDecimal.ZERO) >= 0,
        new InteractionException("Amount has to be positive."));
    CheckValidity.assert_prop(this.inBalanceRange(this.getBalance().add(amount)),
        new InteractionException(
            String.format("You cannot deposit %s amount of money", amount.toString())));
    return DatabaseUpdateHelper.updateAccountBalance(this.getBalance().add(amount),
        this.getAccountId());
  }

  /**
   * This method will set the Balance of the account.
   * 
   * @param amount the BigDecimal for the new balance
   * @return whether the process has succeeded or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException The amount that the user input is invalid
   */
  public boolean setBalance(BigDecimal amount) throws InternalException, InteractionException {
    CheckValidity.assert_prop(this.inBalanceRange(this.getBalance().add(amount)),
        new InteractionException("Invalid Amount."));
    return DatabaseUpdateHelper.updateAccountBalance(amount, this.getAccountId());
  }


  /**
   * This method will return the type of the account.
   * 
   * @return the type of the account
   */
  public abstract AccountTypes getAccountType();

  /**
   * This method will change the current account type to the new account type and return the new
   * account type.
   * 
   * @param accounttype the new account type of the account
   * @return the acount which is of the new account type
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException The admin does not exist or the accounttype does not stored in the
   *         database
   */
  public Account returnAccountWithNewType(Admin admin, AccountTypes accounttype)
      throws InternalException, InteractionException {
    // Using the databaseUpdateHelper to update the corrseponding accounttypeId of that account
    Account changedAccount = null;
    DatabaseUpdateHelper.updateAccountType(AccountTypesMap.MAP.getMapping(accounttype),
        this.getAccountId());
    try {
      changedAccount = Account.getAccountByAdmin(admin, this.accountId);
    } catch (InteractionException | InternalException e) {
      throw new InternalException("Unexpected Exception.");
    }
    this.accountId = -1;
    return changedAccount;
  }

  /**
   * This method will get the interest from the database.
   * 
   * @return the interest of the account
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public BigDecimal getInterest() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getInterestRate(
        AccountTypesMap.MAP.getMapping(this.getAccountType()));
  }

  public boolean inBalanceRange(BigDecimal balance) throws InternalException {
    return this.getAccountType().inBalanceRange(balance);
  }



}

package com.bank.account;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.User;

public class BalanceOwingAccount extends Account {

  /**
   * This method will initialize a new chequing account.
   * 
   * @param user the user who is going to own this account
   * @param name the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public BalanceOwingAccount(User user, String name)
      throws InteractionException, InternalException {
    super(user, name, AccountTypes.BALANCEOWING);

  }

  /**
   * This method will initialize a new chequing account.
   * 
   * @param user the user who is going to own the account
   * @param accountId the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public BalanceOwingAccount(User user, int accountId)
      throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.BALANCEOWING);
  }

  /**
   * This method will set the name of the balance owning account.
   * 
   * @param name the name of the balance owing account
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public BalanceOwingAccount(String name) throws InteractionException, InternalException {
    super(name, AccountTypes.BALANCEOWING);
  }

  /**
   * This method will initialize the BalanceOwingAccount as a admin.
   * 
   * @param admin the admin that process this step
   * @param accountId the id of the account that will be performed the process on
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public BalanceOwingAccount(Admin admin, int accountId)
      throws InteractionException, InternalException {
    super(admin, accountId, AccountTypes.BALANCEOWING);
  }

  /**
   * This method return the type of the chequing account as CHEQUING.
   */
  @Override
  public AccountTypes getAccountType() {
    return AccountTypes.BALANCEOWING;
  }



}

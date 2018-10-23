package com.bank.account;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.User;

public class ChequingAccount extends Account {
  /**
   * This method will initialize a new chequing account.
   * 
   * @param user the user who is going to own this account
   * @param name the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public ChequingAccount(User user, String name) throws InteractionException, InternalException {
    super(user, name, AccountTypes.CHEQUING);
  }

  /**
   * This method will initialize a new chequing account.
   * 
   * @param user the user who is going to own the account
   * @param accountId the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public ChequingAccount(User user, int accountId) throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.CHEQUING);
  }

  /**
   * This method will mutate the name of the Chequing.
   * 
   * @param name the new name of the chequing
   * @throws InteractionException the user input is invalid
   * @throws InternalException there is something wrong dealing with the dagtabase
   */
  public ChequingAccount(String name) throws InteractionException, InternalException {
    super(name, AccountTypes.CHEQUING);

  }

  /**
   * Get the account with the authentication of the admin.
   * 
   * @param admin the authenticated admin who have able to view all the account
   * @param accountId the account id that need to be checked
   * @throws InteractionException the accountId does not exist at all
   * @throws InternalException there is something wrong dealing with the database
   */
  public ChequingAccount(Admin admin, int accountId)
      throws InteractionException, InternalException {
    super(admin, accountId, AccountTypes.CHEQUING);
  }

  /**
   * this method return the type of the chequing account as CHEQUING.
   */
  @Override
  public AccountTypes getAccountType() {
    return AccountTypes.CHEQUING;
  }



}

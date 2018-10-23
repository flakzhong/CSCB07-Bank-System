package com.bank.account;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.User;

public class SavingAccount extends Account {

  /**
   * This method will initialize a new saving account.
   * 
   * @param user the user who is going to own this account
   * @param name the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public SavingAccount(User user, String name) throws InteractionException, InternalException {
    super(user, name, AccountTypes.SAVING);
  }

  /**
   * This method will initialize a new saving account.
   * 
   * @param user the user who is going to own the account
   * @param accountId the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public SavingAccount(User user, int accountId) throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.SAVING);
  }

  /**
   * This name will update the name of the account to the given name.
   * 
   * @param name the new name of the account
   * @throws InteractionException the user input is invalid
   * @throws InternalException there is soemthing wrong dealing with the database
   */
  public SavingAccount(String name) throws InteractionException, InternalException {
    super(name, AccountTypes.SAVING);

  }

  /**
   * This method will let the authenticated admin extract the detail from the saving account.
   * 
   * @param admin the authenticated admin
   * @param accountId the id of account that need to be checked the information
   * @throws InteractionException the accountId do not exist in the database
   * @throws InternalException Theres is something wrong dealing with database
   */
  public SavingAccount(Admin admin, int accountId) throws InteractionException, InternalException {
    super(admin, accountId, AccountTypes.SAVING);
  }

  /**
   * this method return the type of the chequing account as SAVING.
   */
  @Override
  public AccountTypes getAccountType() {

    return AccountTypes.SAVING;
  }



}

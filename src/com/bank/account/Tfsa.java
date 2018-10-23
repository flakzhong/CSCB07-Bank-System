package com.bank.account;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.User;
import java.math.BigDecimal;

public class Tfsa extends Account {


  /**
   * This method will initialize a new Tfsa account.
   * 
   * @param user the user who is going to own this account
   * @param name the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Tfsa(User user, String name) throws InteractionException, InternalException {
    super(user, name, AccountTypes.TFSA);
  }

  /**
   * This method will initialize a new Tfsa account.
   * 
   * @param user the user who is going to own the account
   * @param accountId the name of the account
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Tfsa(User user, int accountId) throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.TFSA);
  }

  /**
   * Modify the name of the account to the new given name.
   * 
   * @param name the new name of the Tfsa
   * @throws InteractionException the user input the invalid name
   * @throws InternalException there is something wrong dealing with the database
   */
  public Tfsa(String name) throws InteractionException, InternalException {
    super(name, AccountTypes.TFSA);

  }


  /**
   * This methid is for the authenticated admin to extract the detail of the account with the given.
   * account id
   * 
   * @param admin the authenticated user
   * @param accountId the account that need to get the detail from
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Tfsa(Admin admin, int accountId) throws InteractionException, InternalException {
    super(admin, accountId, AccountTypes.TFSA);
  }

  /**
   * this method return the type of the chequing account as TFSA.
   */
  @Override
  public AccountTypes getAccountType() {
    return AccountTypes.TFSA;
  }

  /**
   * This method will compare the balance stored in the account to zero.
   */
  @Override
  public boolean inBalanceRange(BigDecimal balance) {
    return (balance.compareTo(BigDecimal.ZERO) >= 0);
  }



}

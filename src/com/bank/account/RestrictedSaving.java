package com.bank.account;

import com.bank.databasehelper.CheckValidity;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.Admin;
import com.bank.user.Teller;
import com.bank.user.User;
import java.math.BigDecimal;

public class RestrictedSaving extends Account {
  /**
   * This method will initialize a new restricted saving account.
   * 
   * @param user the user who is going to own this account
   * @param name the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public RestrictedSaving(User user, String name) throws InteractionException, InternalException {
    super(user, name, AccountTypes.RESTRICTED);
  }

  /**
   * This method will initialize a new restricted saving account.
   * 
   * @param user the user who is going to own the account
   * @param accountId the name of the account
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public RestrictedSaving(User user, int accountId) throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.RESTRICTED);
  }

  /**
   * This method will mutate the name of the Saving.
   * 
   * @param name the new name of the RestrictedSaving
   * @throws InteractionException the name is not valid
   * @throws InternalException there is somethign wrong dealing with the database
   */
  public RestrictedSaving(String name) throws InteractionException, InternalException {
    super(name, AccountTypes.RESTRICTED);

  }

  /**
   * This function will let the authenticated amdin have the ability to view the details of the
   * admin.
   * 
   * @param admin the authenticate admin who want to check the detail information of the account
   * @param accountId the accountid that will be checked
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public RestrictedSaving(Admin admin, int accountId)
      throws InteractionException, InternalException {
    super(admin, accountId, AccountTypes.RESTRICTED);
  }

  @Override
  /**
   * This function will withdraw the money from this account with the given amohnt of money
   */
  public boolean withdrawalMoney(BigDecimal amount) throws InternalException, InteractionException {
    throw new InteractionException("Please withdraw money from a Teller");
  }

  /**
   * This method will let the teller to help to extract the money in the account.
   * 
   * @param amount amount of money that will be extracted
   * @param teller the teller who is going to help store the money
   * @return the boolean whether the withdrawal process is succeeded or not
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public boolean withdrawalMoney(BigDecimal amount, Teller teller)
      throws InternalException, InteractionException {
    CheckValidity.assert_prop(teller != null, new InteractionException("Unauthorized Teller."));
    return super.withdrawalMoney(amount);
  }


  /**
   * This method will return the account type of the RESTRICITED.
   */
  @Override
  public AccountTypes getAccountType() {
    return AccountTypes.RESTRICTED;
  }



}

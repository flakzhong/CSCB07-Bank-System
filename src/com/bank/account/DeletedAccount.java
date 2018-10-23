package com.bank.account;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.user.User;
import java.math.BigDecimal;

public class DeletedAccount extends Account {

  /**
   * This method will put the given account to the deleted group which will not longer be used. in
   * the future
   * 
   * @param user the user who own the account that is going to be deleted
   * @param accountId the account id of the account that going to be deleted
   * @throws InteractionException the user input the invalid input the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  protected DeletedAccount(User user, int accountId)
      throws InteractionException, InternalException {
    super(user, accountId, AccountTypes.DELETEAT);
    throw new InteractionException("Inexistent Account.");
  }

  /**
   * This method will return the type as null.
   */
  @Override
  public AccountTypes getAccountType() {
    return null;
  }

  @Override
  public boolean inBalanceRange(BigDecimal balance) {
    return false;
  }

}

package com.bank.bankdata;

import com.bank.account.Account;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.tools.Dynatuple;
import java.math.BigDecimal;

public class AccountImage implements Image<Account> {

  // So that on different platform, everything still works, you can check the version of computer
  // version
  private static final long serialVersionUID = 8019871353822944656L;

  // The only field it has
  private Dynatuple<String, Dynatuple<BigDecimal, AccountTypes>> field;

  /**
   * Generate a Image of Account, by the instance of account
   * 
   * @param account Origin Account
   * @throws InternalException if unexpected happen.
   * @throws InteractionException If unexpected input.
   */
  public AccountImage(Account account) throws InternalException, InteractionException {
    this.field =
        new Dynatuple<>(account.getName(),
            new Dynatuple<>(account.getBalance(), account.getAccountType()));

  }


  /**
   * Warning, in a re-generation of Account, it's not supposed to be invoked twice.
   * 
   * @return Account, the newly created one in the database
   * @throws InternalException If unexpected happened.
   * @throws InteractionException If unexpected input.
   */
  @Override
  public Account injective() throws InternalException, InteractionException {
    Account acc = Account.createAccount(this.field.left(), this.field.right().right());
    BigDecimal amount = this.field.right().left();
    if (amount.compareTo(BigDecimal.ZERO) >= 0) {
      acc.depositMoney(this.field.right().left());
    } else {
      acc.withdrawalMoney(amount.abs());
    }
    return acc;
  }

}

package com.bank.generics;

import com.bank.account.Account;
import com.bank.account.BalanceOwingAccount;
import com.bank.account.ChequingAccount;
import com.bank.account.DeletedAccount;
import com.bank.account.RestrictedSaving;
import com.bank.account.SavingAccount;
import com.bank.account.Tfsa;
import com.bank.bankdata.AccountTypesMap;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.user.Admin;
import com.bank.user.User;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * AccountTypes with all types of accounts, along with its helper function.
 * 
 * @author jinende
 *
 */
public enum AccountTypes implements FiniteEnum<AccountTypes>, Serializable {
  CHEQUING, SAVING, TFSA, RESTRICTED, DELETEAT, BALANCEOWING;

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);

  /**
   * Map each account type to corresponding Class. Reflect.
   * 
   * @param accounttype The accounttype desired to be mapped.
   * @return The class.
   * @throws UnexposedException If unexpected happend.
   */
  public static Class<? extends Account> mapAccountType(AccountTypes accounttype)
      throws UnexposedException {
    switch (accounttype) {
      case CHEQUING:
        return ChequingAccount.class;
      case SAVING:
        return SavingAccount.class;
      case TFSA:
        return Tfsa.class;
      case RESTRICTED:
        return RestrictedSaving.class;
      case DELETEAT:
        return DeletedAccount.class;
      case BALANCEOWING:
        return BalanceOwingAccount.class;
      default:
        break;
    }

    throw new UnexposedException("Type Unfound.");

  }


  /**
   * Map account type id to account type.
   * 
   * @param accounttypeId The id to be mapped.
   * @return The account type.
   * @throws InternalException If unexpected happen.
   */
  public static AccountTypes mapAccountType(int accounttypeId)
      throws InternalException {
    return TFSA.stringInjective(DatabaseSelectHelper.getAccountTypeName(accounttypeId));
  }


  /**
   * Map each type of account to its constructor.
   * 
   * @return The constructor.
   * @throws InternalException If unexpected happened.
   */
  public Fx<User, Fx<String, Account>> createAccount() throws InternalException {
    switch (this) {
      case CHEQUING:
        return (a -> b -> new ChequingAccount(a, b));
      case SAVING:
        return (a -> b -> new SavingAccount(a, b));
      case TFSA:
        return (a -> b -> new Tfsa(a, b));
      case RESTRICTED:
        return (a -> b -> new RestrictedSaving(a, b));
      case BALANCEOWING:
        return (a -> b -> new BalanceOwingAccount(a, b));
      default:
        throw new InternalException("Unexpected Behavior: Type Unfound.");
    }
  }

  /**
   * Map each type of account to its constructor.
   * 
   * @return The constructor.
   * @throws InternalException If unexpected happened.
   */
  public Fx<String, Account> createAccountWithNoUser() throws InternalException {
    switch (this) {
      case CHEQUING:
        return (a -> new ChequingAccount(a));
      case SAVING:
        return (a -> new SavingAccount(a));
      case TFSA:
        return (a -> new Tfsa(a));
      case RESTRICTED:
        return (a -> new RestrictedSaving(a));
      case BALANCEOWING:
        return (a -> new BalanceOwingAccount(a));
      default:
        throw new InternalException("Unexpected Behavior: Type Unfound.");
    }
  }


  /**
   * Map each type of account to its constructor.
   * 
   * @return The constructor.
   * @throws InternalException If unexpected happened.
   */
  public Fx<User, Fx<Integer, Account>> getAccount() throws InternalException {
    switch (this) {
      case CHEQUING:
        return (a -> b -> new ChequingAccount(a, b));
      case SAVING:
        return (a -> b -> new SavingAccount(a, b));
      case TFSA:
        return (a -> b -> new Tfsa(a, b));
      case RESTRICTED:
        return (a -> b -> new RestrictedSaving(a, b));
      case BALANCEOWING:
        return (a -> b -> new BalanceOwingAccount(a, b));
      default:
        throw new InternalException("Unexpected Behavior: Type Unfound.");
    }

  }


  /**
   * Get the account by admin authentication
   * 
   * @return A curried lambda expression, with admin Integer as input, return Account.
   * @throws InternalException When unexpected happened.
   */
  public Fx<Admin, Fx<Integer, Account>> getAccountByAdmin() throws InternalException {
    switch (this) {
      case CHEQUING:
        return (a -> b -> new ChequingAccount(a, b));
      case SAVING:
        return (a -> b -> new SavingAccount(a, b));
      case TFSA:
        return (a -> b -> new Tfsa(a, b));
      case RESTRICTED:
        return (a -> b -> new RestrictedSaving(a, b));
      case BALANCEOWING:
        return (a -> b -> new BalanceOwingAccount(a, b));
      default:
        throw new InternalException("Unexpected Behavior: Type Unfound.");
    }

  }

  /**
   * Check the balance, if it is in the account type's range.
   * 
   * @param balance The balance.
   * @return true if it is in.
   * @throws InternalException When type is not supposed to.
   */
  public boolean inBalanceRange(BigDecimal balance) throws InternalException {
    switch (this) {
      case CHEQUING:
        return (balance.compareTo(BigDecimal.ZERO) >= 0);
      case SAVING:
        return (balance.compareTo(BigDecimal.ZERO) >= 0);
      case TFSA:
        return (balance.compareTo(BigDecimal.ZERO) >= 0);
      case RESTRICTED:
        return (balance.compareTo(BigDecimal.ZERO) >= 0);
      case BALANCEOWING:
        return true;
      default:
        throw new InternalException("Unexpected Behavior: Type Unfound.");
    }
  }

  /**
   * Map account type to account type id.
   * 
   * @param accounttype The account type to be mapped.
   * @return The account type id
   * @throws InternalException If unexpected happen.
   */
  public static int mapAccountTypeId(AccountTypes accounttype) throws UnexposedException {
    return AccountTypesMap.MAP.getMapping(accounttype);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.generics.FiniteEnum#allValues()
   */
  @Override
  public AccountTypes[] allValues() {
    return AccountTypes.values();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.generics.FiniteEnum#stringInjective()
   */
  @Override
  public String stringInjective() {
    return this.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.generics.FiniteEnum#stringInjective(java.lang.String)
   */
  @Override
  public AccountTypes stringInjective(String x) throws UnexposedException {
    for (AccountTypes acc : this.allValues()) {
      if (acc.stringInjective().equals(x)) {
        return acc;
      }
    }
    throw new UnexposedException("Can't find corresponding string");
  }

}

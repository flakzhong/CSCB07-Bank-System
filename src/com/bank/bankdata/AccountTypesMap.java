package com.bank.bankdata;

import com.bank.databasehelper.Database;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import java.util.List;

/**
 * The AccountTypesMap. Map each Accounttypes to its id.
 * 
 * @author jinende
 *
 */
public class AccountTypesMap extends DatabaseMap<AccountTypes, Integer> {
  // Singleton pattern.
  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);


  public static final AccountTypesMap MAP = new AccountTypesMap();

  private AccountTypesMap() {
    super(AccountTypes.CHEQUING);
  }


  /**
   * Update the id of the AccountType enumerator.
   */
  @Override
  public boolean updateMapping() throws InternalException {
    List<Integer> allids = DatabaseSelectHelper.getAccountTypesIds();
    for (int i : allids) {
      this.getEnumMap()
          .put(this.nonempty().stringInjective(DatabaseSelectHelper.getAccountTypeName(i)), i);
    }
    return true;
  }
}

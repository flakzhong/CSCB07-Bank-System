package com.bank.bankdata;

import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.exceptions.InternalException;

// A UserAccount Relation.
// It is used so that we can make user account assocation, (the database table) appear in different
// platform

public class UserAccountRel {
  public static boolean assocUserAccount(int userId, int accountId) throws InternalException {
    DataInserter databaseInsertHelper = DataOperator.insert(Database.DATABASE);
    return databaseInsertHelper.insertUserAccount(userId, accountId) != -1;
  }
}

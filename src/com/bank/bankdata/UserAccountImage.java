package com.bank.bankdata;

import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.tools.Dynatuple;

/* The instance of Image<UserAccountRel>, in other word, Image<Dynatuple<Integer, Integer>> */
public class UserAccountImage implements Image<Dynatuple<Integer, Integer>> {

  // The reason of cross platform
  private static final long serialVersionUID = -7395491639522263506L;


  @SuppressWarnings("unused")
  private static final DataInserter DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);


  /* The user id, the account id */
  private Integer userId;
  private Integer accountId;


  /**
   * Construct a User Account Image by a User Account association.
   * 
   * @param userId the id of the user.
   * @param accountId The id of the account.
   * @throws InternalException when unexpected happened.
   * @throws InteractionException When unexpected input.
   */
  public UserAccountImage(Integer userId, Integer accountId)
      throws InternalException, InteractionException {
    this.userId = userId;
    this.accountId = accountId;
  }

  @Override
  public Dynatuple<Integer, Integer> injective() throws InternalException, InteractionException {
    Dynatuple<Integer, Integer> ret = new Dynatuple<>(this.userId, this.accountId);
    UserAccountRel.assocUserAccount(this.userId, this.accountId);
    return ret;
  }

}

package com.bank.user;

import com.bank.account.Account;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);

  private static final DataInserter DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);
  

  /**
   * Use the build function in the user class to initialize the Customer.
   * 
   * @param name the name of the teller
   * @param age the age of the teller
   * @param address the address of the teller
   * @param password the password of the user
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Customer(String name, int age, String address, String password)
      throws InteractionException, InternalException {
    super(name, age, address, password, Roles.CUSTOMER);

  }

  /**
   * This method is used to check the validity of the user.
   * 
   * @param id the id of the user
   * @param password the password that need to be checked
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Customer(int id, String password) throws InteractionException, InternalException {
    super(id, password, Roles.CUSTOMER);

  }


  /**
   * This method is used to check the validity of the customer.
   * 
   * @param id the id of the customer
   * @param admin the admin that is checking this method
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Customer(int id, Admin admin) throws InteractionException, InternalException {
    super(id, admin, Roles.CUSTOMER);

  }

  /**
   * This method will get out the account from the database.
   * 
   * @return list of accounts stored in the database
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public List<Account> getAccounts() throws InternalException, InteractionException {
    List<Integer> accountids = DatabaseSelectHelper.getAccountIds(this.getUserId());
    List<Account> accounts = new ArrayList<Account>(accountids.size());
    for (int accountid : accountids) {
      accounts.add(Account.getAccount(this, accountid));
    }
    return accounts;

  }

  /**
   * This method will try to insert the given account to the database.
   * 
   * @param account the account that the customer want to add to its list of account
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public void addAccount(Account account) throws InternalException, InteractionException {
    if (account != null) {
      DatabaseInsertHelper.insertUserAccount(this.getUserId(), account.getAccountId());
    } else {
      throw new InteractionException("Account cannot be null.");
    }
  }


  @Override
  /**
   * This method will return the type of the customer as CUSTOMER
   */
  public Roles getUserRole() {
    return Roles.CUSTOMER;
  }


}

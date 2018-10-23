package com.bank.user;

import com.bank.account.Account;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

  /**
   * Use the build function in the user class to initialize the Admin.
   * 
   * @param name the name of the teller
   * @param age the age of the teller
   * @param address the address of the teller
   * @param password the password of the user
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Admin(String name, int age, String address, String password)
      throws InteractionException, InternalException {
    super(name, age, address, password, Roles.ADMIN);

  }


  /**
   * This method is used to check the validity of the Admin.
   * 
   * @param id the id of the admin
   * @param password the password that need to be checked
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Admin(int id, String password) throws InteractionException, InternalException {
    super(id, password, Roles.ADMIN);

  }


  /**
   * This method is used to check the validity of the Admin.
   * 
   * @param id the id of the customer
   * @param admin the admin that is checking this method
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Admin(int id, Admin admin) throws InteractionException, InternalException {
    super(id, admin, Roles.ADMIN);

  }


  @Override
  /**
   * this method will return the type of admin class as ADMIN.
   * 
   */
  public Roles getUserRole() {
    return Roles.ADMIN;
  }


  /**
   * This method will return all of the user in the database.
   * 
   * @return List of all the user stored in the database
   * @throws InteractionException there is something dealing with the database
   * @throws InternalException there is something dealing with the database
   */
  public List<User> getAllUsers() throws InternalException, InteractionException {
    // loop from the 1
    // then try to get all the user in the database
    DataSelector databaseSelectHelper = DataOperator.select(Database.DATABASE);
    List<Integer> allUserIds = databaseSelectHelper.getAllUserIds();
    List<User> allUsers = new ArrayList<>(allUserIds.size());
    for (int userId : allUserIds) {
      allUsers.add(User.getUser(userId, this));
    }
    return allUsers;
  }



  /**
   * Get all the account in bank.
   * 
   * @return the list of Account.
   */
  @SuppressWarnings("finally")
  public List<Account> getAllAccounts() {
    // Many bugs: when introducing deleter, things will go mess.
    // loop from the 1
    // then try to get all the user in the database
    int id = 1;
    List<Account> accounts = new ArrayList<Account>();
    // while we can get the user, try to get the user
    // then when there is an error, return the list
    // of user we have got from the database
    try {
      while (true) {
        Account acc = Account.getAccountByAdmin(this, id);


        if (acc.getAccountType() != AccountTypes.DELETEAT) {
          accounts.add(acc);
        }

        id++;
      }
    } finally {
      return accounts;
    }

  }
}

package com.bank.user;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;

public class Teller extends User {

  /**
   * Use the build function in the user class to initialize the teller.
   * 
   * @param name the name of the teller
   * @param age the age of the teller
   * @param address the address of the teller
   * @param password the password of the user
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Teller(String name, int age, String address, String password)
      throws InteractionException, InternalException {
    super(name, age, address, password, Roles.TELLER);

  }


  /**
   * Call the corresponding method in the user class.
   * 
   * @param id the id of the teller
   * @param password the password of the teller that need to be checked
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Teller(int id, String password) throws InteractionException, InternalException {
    super(id, password, Roles.TELLER);

  }

  /**
   * Call the corresponding method in the user class.
   * 
   * @param id the id of the teller
   * @param admin that want to check the
   * @throws InteractionException the user input invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  public Teller(int id, Admin admin) throws InteractionException, InternalException {
    super(id, admin, Roles.TELLER);
  }

  /**
   * This method will return the type of the teller as TELLER.
   */
  @Override
  public Roles getUserRole() {
    return Roles.TELLER;
  }

}

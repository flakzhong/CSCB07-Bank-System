package com.bank.user;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;

public class DeletedUser extends User {

  /**
   * a constructor to initialize a deleted user.
   * 
   * @param id user id
   * @param password user password
   * @throws InteractionException exception thrown when wrong information is given
   * @throws InternalException exception thrown when something went wrong in the databse
   */
  protected DeletedUser(int id, String password) throws InteractionException, InternalException {
    super(id, password, Roles.DELETEDUSER);
    throw new InteractionException("Deleted User.");
  }

  @Override
  /**
   * return the user role.
   * 
   */
  public Roles getUserRole() {
    // TODO Auto-generated method stub
    return null;
  }

}

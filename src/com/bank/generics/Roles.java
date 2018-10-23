package com.bank.generics;

import com.bank.bankdata.RolesMap;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.DeletedUser;
import com.bank.user.Teller;
import com.bank.user.User;
import java.io.Serializable;

/**
 * Roles with all the possible roles along with its heolper function.
 * 
 * @author jinende
 *
 */
public enum Roles implements FiniteEnum<Roles>, Serializable {
  CUSTOMER, TELLER, ADMIN, DELETEDUSER;

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);



  private Roles() {}

  /**
   * Map Roles to Class. Reflect.
   * 
   * @param roletype The role you want to map.
   * @return A class representing the role.
   * @throws UnexposedException If unexpected happen.
   */
  public static Class<? extends User> mapRole(Roles roletype) throws UnexposedException {
    switch (roletype) {
      case CUSTOMER:
        return Customer.class;
      case TELLER:
        return Teller.class;
      case ADMIN:
        return Admin.class;
      case DELETEDUSER:
        return DeletedUser.class;
      default:
        break;
    }

    throw new UnexposedException("Type Unfound.");
  }


  /**
   * Mapping roleId to roles.
   * 
   * @param roleId the id of the role that will be mapped to the database
   * @return the int that map the role from the database
   * @throws UnexposedException can not found the constructor in the class
   * @throws InternalException there is something wrong dealing with the database
   */
  public static Roles mapRole(int roleId) throws UnexposedException, InternalException {
    return CUSTOMER.stringInjective(DatabaseSelectHelper.getRole(roleId));
  }
  
  /**
   * Mapping each role to constructor.
   * 
   * @return The constructor, curried.
   * @throws UnexposedException If unepected happen.
   */
  public Fx<String, Fx<Integer, Fx<String, Fx<String, User>>>> createUser()
      throws UnexposedException {
    switch (this) {
      case CUSTOMER:
        return (a -> b -> c -> d -> new Customer(a, b, c, d));
      case TELLER:
        return (a -> b -> c -> d -> new Teller(a, b, c, d));
      case ADMIN:
        return (a -> b -> c -> d -> new Admin(a, b, c, d));
      default:
        throw new UnexposedException("Type Unfound.");
    }
  }


  /**
   * Mapping each role to constructor.
   * 
   * @return The constructor, curried.
   * @throws UnexposedException If unepected happen.
   */
  public Fx<Integer, Fx<String, User>> getUser() throws UnexposedException {
    switch (this) {
      case CUSTOMER:
        return (a -> b -> new Customer(a, b));
      case TELLER:
        return (a -> b -> new Teller(a, b));
      case ADMIN:
        return (a -> b -> new Admin(a, b));
      default:
        throw new UnexposedException("Type Unfound.");
    }
  }

  /**
   * Mapping each role to constructor.
   * 
   * @return The constructor, curried.
   * @throws UnexposedException If unepected happen.
   */
  public Fx<Integer, Fx<Admin, User>> getUserByAdmin() throws UnexposedException {
    switch (this) {
      case CUSTOMER:
        return (a -> b -> new Customer(a, b));
      case TELLER:
        return (a -> b -> new Teller(a, b));
      case ADMIN:
        return (a -> b -> new Admin(a, b));
      default:
        throw new UnexposedException("Type Unfound.");
    }
  }


  /**
   * Mapping roletype to Roleid.
   * 
   * @param roletype the role you want to map.
   * @return The id of the role.
   * @throws UnexposedException If unexpected happen.
   */
  public static int mapRoleId(Roles roletype) throws UnexposedException {
    return RolesMap.MAP.getMapping(roletype);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.bank.generics.FiniteEnum#allValues()
   */
  @Override
  public Roles[] allValues() {
    return Roles.values();
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
  public Roles stringInjective(String x) throws UnexposedException {
    for (Roles r : this.allValues()) {
      if (r.stringInjective()
          .equals(x)) {
        return r;
      }
    }
    throw new UnexposedException("Can't find corresponding string");
  }



}

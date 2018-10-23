package com.bank.bankdata;

import com.bank.databasehelper.Database;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;
import java.util.List;

/**
 * The RolesMap. Mapping each Roles to its id.
 * 
 * @author jinende
 *
 */
public class RolesMap extends DatabaseMap<Roles, Integer> {

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);



  public static  final RolesMap MAP = new RolesMap();

  private RolesMap() {
    super(Roles.ADMIN);
  }

  
  /**
   * Update the id of the RoleType enumerator.
   */
  @Override
  public boolean updateMapping() throws InternalException {
    List<Integer> allids = DatabaseSelectHelper.getRoles();
    for (int i : allids) {
      this.getEnumMap()
          .put(this.nonempty()
              .stringInjective(DatabaseSelectHelper.getRole(i)), i);
    }
    return true;
  }






}

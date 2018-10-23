package com.bank.bankdata;

import com.bank.databasehelper.StdLog;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.generics.FiniteEnum;
import java.util.EnumMap;

/**
 * Generic DatabaseMap. SuperType of both accountType map and rolesmap. Can't map keys to null.
 * 
 * @author jinende
 *
 * @param <K> The keys. Has to be a finite set and non-empty set.
 * @param <V> The values.
 */
public abstract class DatabaseMap<K extends Enum<K> & FiniteEnum<K>, V> {
  // The error log
  private static StdLog errmsger;
  // The enum map to record mapping information
  private EnumMap<K, V> database;
  // one of the element of the K, as a proof that K is not empty.
  private K nonempty;



  /**
   * The constructor of all kinds of DatabaseMap.
   * 
   * @param nonEmpty One element of the K, A proof it is non-empty.
   */
  protected DatabaseMap(K nonEmpty) {
    database = new EnumMap<K, V>(nonEmpty.getDeclaringClass());
    this.nonempty = nonEmpty;

    try {
      this.updateMapping();
    } catch (InternalException e) {
      errmsger.outputMsg(String.format("%s : %s",
          e.getClass()
              .toString(),
          e.getMessage()));
    }


  }


  /**
   * Update the current mapping information.
   * 
   * @return True if success.
   * @throws UnexposedException If unexpected happen.
   */
  public abstract boolean updateMapping() throws InternalException;

  /**
   * Get the enummap of this DatabaseMapping. For UpdateMapping's use.
   * 
   * @return The enummap.
   */
  protected EnumMap<K, V> getEnumMap() {
    return this.database;
  }

  /**
   * Get the one element of the K.
   * 
   * @return The K.
   */
  protected K nonempty() {
    return this.nonempty;
  }

  /**
   * Return the value k is mapping to.
   * 
   * @param k The key.
   * @return The Value.
   * @throws UnexposedException If unepected happen.
   */
  public V getMapping(K k) throws UnexposedException {
    V value = this.database.get(k);
    if (value == null) {
      throw new UnexposedException("DatabaseMap: Mapping unfound.");
    }
    return value;
  }
}

package com.bank.bankdata;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import java.io.Serializable;


/**
 * Its existence is to ensure all the information in the database respect to a field has been stored
 * in the memory; so that serialization is valid. We have this interface only because we initially
 * design our user and account will not store any information except its id and its password so that
 * every thing is neat and don't need to maintain to ensure the correctness.
 * 
 * @author jinende
 *
 * @param <T> The type that should be serialized.
 */
public interface Image<T> extends Serializable {
  /**
   * A recovery function meant to recover data from its image. A one-to-one function. This
   * abstraction is meant to have 'side effect' in it (Which cannot be included in type signature).
   * (UserImage extends Image of User).injective will create a new user in database (AccountImage
   * extends Image of Account).injective will create a new account in database Same with
   * UserAccountImage
   * 
   * @return a recovered value
   * @throws InternalException when unexpected happen
   * @throws InteractionException when unexpected input
   */
  public T injective() throws InternalException, InteractionException;
}

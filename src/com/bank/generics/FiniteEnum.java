package com.bank.generics;

import com.bank.exceptions.UnexposedException;

/**
 * An interface(type class) indicating the decorated type can be seen as a set with a finite number
 * of element and meanwhile, it has to be one-to-one with a set of strings.
 * 
 * @author jinende
 *
 * @param <T> The decorated type.
 */
public interface FiniteEnum<T> {
  // If it has only finite element, it must can be list with all the element is has.
  public T[] allValues();

  // It must be one-to-one with string (injective).
  public String stringInjective();

  // It must be one-to-one with string, so inverse mapping has to be provided.
  public T stringInjective(String x) throws UnexposedException;

}

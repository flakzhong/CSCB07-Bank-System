package com.bank.databasehelper;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;

/**
 * The predicate : forall T: Type, T -> boolean.
 * 
 * @author jinende
 *
 * @param <T> The type T to be evaluated.
 */
@FunctionalInterface
public interface Predicate<T> {
  // The predicate
  boolean prd(T x) throws InternalException, InteractionException;
}
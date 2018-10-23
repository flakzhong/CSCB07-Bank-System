package com.bank.generics;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;

/**
 * A curried lambda.
 * 
 * @author jinende
 *
 * @param <X> domain.
 * @param <Y> co-domain.
 */
@FunctionalInterface
public interface Fx<X, Y> {
  // The function. Which can either throw internal exception or inteaction exception.
  public Y fx(X x) throws InternalException, InteractionException;
}

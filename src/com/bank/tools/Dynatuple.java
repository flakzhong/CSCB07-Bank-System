package com.bank.tools;

import com.bank.bankdata.Image;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;

/**
 * this class is a helper class to store the corresponding information.
 * 
 * @author Flak
 *
 * @param <T> left element
 * @param <K> right element
 */
public class Dynatuple<T, K> implements Image<Dynatuple<T, K>> {
  
  private static final long serialVersionUID = 1L;
  private T left;
  private K right;

  /**
   * initialize the tuple.
   * 
   * @param a left element
   * @param b right element
   */
  public Dynatuple(T a, K b) {
    this.left = a;
    this.right = b;
  }

  /**
   * return the left element.
   * 
   * @return left the left element
   */
  public T left() {
    return this.left;
  }

  /**
   * return the right element.
   * 
   * @return right the right element
   */
  public K right() {
    return this.right;
  }

  @Override
  public Dynatuple<T, K> injective() throws InternalException, InteractionException {
    return this;
  }
}

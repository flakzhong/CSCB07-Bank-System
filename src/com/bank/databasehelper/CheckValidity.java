package com.bank.databasehelper;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;



/**
 * A tool for checking if the argument satisfy certain requirement.
 * 
 * @author jinende
 *
 * @param <T> The type of the object to be decorated.
 */
public class CheckValidity<T> {

  // The object being tested
  private T target;

  // the initialization, specify which object to be tested.
  public CheckValidity(T x) {
    this.target = x;
  }

  /**
   * Assert the proposition.
   * 
   * @param v A boolean value, in expression form.
   * @param exc The exception to be thrown when v is false.
   * @throws Q The exception.
   */
  public static <Q extends Exception> void assert_prop(boolean v, Q exc) throws Q {
    if (!v) {
      throw exc;
    }
  }

  /**
   * Check if the object satisfy the predicate 'pred'
   * 
   * @param pred The predicate, a function with type : T -> boolean.
   * @param exc To be throw when predicate evaluated to false
   * @return CheckValidity itself to start another check
   * @throws InternalException When problem is raised by program
   * @throws InteractionException When problem is raised by the user
   */
  public <Q extends InternalException> CheckValidity<T> valid(Predicate<T> pred, Q exc)
      throws InternalException, InteractionException {
    if (!(pred.prd(target))) {
      throw exc;
    }
    return this;
  }

  /**
   * Check if the object satisfy the predicate 'pred'
   * 
   * @param pred The predicate, a function with type : T -> boolean.
   * @param exc To be throw when predicate evaluated to false
   * @return CheckValidity itself to start another check
   * @throws InternalException When problem is raised by program
   * @throws InteractionException When problem is raised by the user
   */
  public <Q extends InteractionException> CheckValidity<T> valid(Predicate<T> pred, Q exc)
      throws InternalException, InteractionException {
    if (!(pred.prd(target))) {
      throw exc;
    }
    return this;
  }

}

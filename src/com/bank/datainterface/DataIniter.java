package com.bank.datainterface;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;

public interface DataIniter extends DataOperator {
  // Initializer, Initialize the whole database with empty valid tables
  public boolean initialize() throws InteractionException, InternalException;

  // reinitializer, clear the table and initialize it
  public boolean reInitialize() throws InteractionException, InternalException;

  // Easy backup, by copying the local db to local as backup
  public boolean trivialBackup() throws InternalException;

  // Easy recover, by trivial recovering of local database
  public boolean trivialRecover() throws InternalException;
}

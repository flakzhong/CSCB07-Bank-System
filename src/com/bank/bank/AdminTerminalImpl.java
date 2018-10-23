package com.bank.bank;

import com.bank.account.Account;
import com.bank.bankdata.DatabaseImage;
import com.bank.databasehelper.CheckValidity;
import com.bank.datainterface.DataMeta;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Fx;
import com.bank.generics.Roles;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 72948.
 *
 */
public class AdminTerminalImpl implements AdminTerminal {

  private Admin admin;

  /**
   * A constructor to create a new admin object.
   * 
   * @param adminId the user id
   * @param password the password
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public AdminTerminalImpl(int adminId, String password)
      throws InternalException, InteractionException {
    this.admin = (Admin) User.getUser(adminId, password, Roles.ADMIN);
  }

  @Override
  /**
   * Return the id of the newly-created admin.
   * 
   * @param name the name of the admin
   * @param age the age of the admin
   * @param address the address of the admin
   * @param password the password of the admin
   * @return the user id of the newly-created admin
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  public int makeNewAdmin(String name, int age, String address, String password)
      throws InternalException, InteractionException {
    Admin admin = (Admin) User.createUser(name, age, address, password, Roles.ADMIN);
    return admin.getUserId();
  }


  /**
   * Return a list of users.
   * 
   * @return a list of users.
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  @Override
  public List<User> allUsers() throws InternalException, InteractionException {
    return this.admin.getAllUsers();
  }

  private static <T extends User> List<T> filtAndCast(List<User> list, Class<T> type)
      throws InternalException, InteractionException {
    return (list.stream().filter(x -> type.isInstance(x)).<T>map(x -> type.cast(x)).collect(
        Collectors.toList()));
  }


  /**
   * Return a list of customers.
   * 
   * @return a list of customers
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  @Override
  public List<Customer> allCustomers() throws InternalException, InteractionException {
    return AdminTerminalImpl.filtAndCast(this.allUsers(), Customer.class);
  }


  /**
   * Return a list of admins.
   * 
   * @return a list of admin
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  @Override
  public List<Admin> allAdmins() throws InternalException, InteractionException {
    return AdminTerminalImpl.filtAndCast(this.allUsers(), Admin.class);
  }


  /**
   * Return a list of tellers.
   * 
   * @return a list of tellers
   * @throws InternalException exception thrown when something wrong in the database
   * @throws InteractionException exception thrown when user typed something invalid
   */
  @Override
  public List<Teller> allTellers() throws InternalException, InteractionException {
    return AdminTerminalImpl.filtAndCast(this.allUsers(), Teller.class);
  }

  /**
   * A trivial mapping allowing two exception invoked.
   * 
   * @param orgList The mapped list
   * @param mapping The mapping function
   * @return A list after mapping
   * @throws InternalException If unexpected happen
   * @throws InteractionException If unexpected input
   */
  private static <Q, S> ArrayList<Q> mapWithException(List<S> orgList, Fx<S, Q> mapping)
      throws InternalException, InteractionException {
    ArrayList<Q> retList = new ArrayList<Q>(orgList.size());
    for (S eachOrg : orgList) {
      retList.add(mapping.fx(eachOrg));
    }
    return retList;
  }

  
  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#balanceOfaCustomer(int)
   */
  @Override
  public BigDecimal balanceOfaCustomer(int userId) throws InternalException, InteractionException {
    User user = User.getUser(userId, this.admin);
    CheckValidity.assert_prop(user.getUserRole() == Roles.CUSTOMER,
        new InteractionException("Please input customer ID."));
    Customer customer = (Customer) user;
    ArrayList<BigDecimal> allbalances =
        mapWithException(customer.getAccounts(), x -> x.getBalance());

    return allbalances.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

  }
  
  
  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#balanceOfAllAcount()
   */
  @Override
  public BigDecimal balanceOfAllAcount() throws InternalException, InteractionException {
    ArrayList<BigDecimal> allbalances =
        mapWithException(this.admin.getAllAccounts(), x -> x.getBalance());
    return allbalances.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#promoteTeller(int)
   */
  @Override
  public boolean promoteTeller(int tellerId) throws InternalException, InteractionException {
    User maybeTeller = User.getUser(tellerId, this.admin);
    CheckValidity.assert_prop(maybeTeller.getUserRole() == Roles.TELLER,
        new InteractionException("Please input the Id of a Teller!"));
    Teller teller = (Teller) maybeTeller;
    Admin newAdmin = (Admin) teller.returnUserWithNewRole(Roles.ADMIN, this.admin);
    newAdmin.newMessage("You have been promoted to an Admin.");
    return true;
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#viewMessage(int)
   */
  @Override
  public String viewMessage(int messageId)
      throws InternalException, InteractionException {
    return Message.getMessage(this.admin, messageId).getMessage();
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#updateMessagestatus(int)
   */
  @Override
  public boolean updateMessagestatus(int messageId) throws InternalException, InteractionException {
    return Message.getMessage(this.admin, messageId).viewed();
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#leaveMessage(int, java.lang.String)
   */
  @Override
  public Message leaveMessage(int userId, String message)
      throws InternalException, InteractionException {
    return (new Message(userId, message));
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#databaseDaemon()
   */
  @Override
  public boolean databaseDaemon() throws InternalException, InteractionException {
    DatabaseImage img = DataMeta.databaseRecord(this.admin);
    return DataMeta.dataImageSerialize(img);
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#databaseRecover()
   */
  @Override
  public boolean databaseRecover() throws InternalException, InteractionException {
    DatabaseImage img = DataMeta.dataImageDeserialize();
    DataMeta.databaseRecover(this.admin, img);
    System.exit(0);
    return true;
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#readMessage(int)
   */
  @Override
  public String readMessage(int messageId) throws InternalException, InteractionException {
    return this.admin.readMessage(messageId);
  }

  /* (non-Javadoc)
   * @see com.bank.bank.AdminTerminal#listAllMessages()
   */
  @Override
  public List<Integer> listAllMessages() throws InternalException, InteractionException {
    return this.admin.getAllMessageIds();
  }

  private Customer getOwningCustomer(int accountId) throws InternalException, InteractionException {
    for (User eachUser : this.admin.getAllUsers()) {
      if (eachUser.getUserRole() == Roles.CUSTOMER) {
        Customer eachCustomer = (Customer) eachUser;
        for (Account eachAccount : eachCustomer.getAccounts()) {
          if (eachAccount.getAccountId() == accountId) {
            return eachCustomer;
          }
        }
      }
    }
    throw new InternalException("Unexpected Error Occurred.");
  }

  @Override
  public boolean transitAccountType(int accountId, AccountTypes newAccountType)
      throws InternalException, InteractionException {
    Account account = Account.getAccountByAdmin(this.admin, accountId);
    AccountTypes orgType = account.getAccountType();
    if (newAccountType.inBalanceRange(account.getBalance())) {
      Account newAccount = account.returnAccountWithNewType(this.admin, newAccountType);
      String message =
          String.format("Your account (ID: %d) has been through a transition from %s to %s.",
              newAccount.getAccountId(),
              orgType.toString(), newAccountType.toString());
      this.leaveMessage(this.getOwningCustomer(accountId).getUserId(), message);
      return true;
    }
    throw new InteractionException("You cannot transit to such account type");



  }



}

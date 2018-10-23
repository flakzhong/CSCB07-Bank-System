package com.bank.user;

import com.bank.bank.Message;
import com.bank.bankdata.RolesMap;
import com.bank.databasehelper.CheckValidity;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.datainterface.DataUpdator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.exceptions.UnexposedException;
import com.bank.generics.Roles;
import com.bank.security.PasswordHelpers;
import java.util.List;

/**
 * The public abstract class User, The prototype and infrastructure of all types of user. When there
 * is an instance of User, it means that User is authenticated and is a 'handler'.
 * 
 * @author jinende
 *
 */
public abstract class User {
  private int userId;
  private String password;

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);

  private static final DataUpdator DatabaseUpdateHelper = DataOperator.update(Database.DATABASE);

  private static final DataInserter DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);

  /**
   * This method is used to insert the user, all of which input is valid into the database.
   * 
   * @param name the name of the user
   * @param age the age of the user
   * @param address the address of the user
   * @param password the password that the user prefer
   * @param roleType the role type of the user
   * @throws InteractionException there is invalid input
   * @throws InternalException there is something wrong dealing with the database
   */
  protected User(String name, int age, String address, String password, Roles roleType)
      throws InteractionException, InternalException {
    // the valid name should be not be null
    new CheckValidity<String>(name).valid(x -> x != null,
        new InteractionException("Invalid input : null"));
    // the valid age should not be less than 0
    new CheckValidity<Integer>(age).valid(x -> x >= 0,
        new InteractionException("Invalid input : null"));
    // the valid address should be a string that is consist of less than 100 in length
    new CheckValidity<String>(address).valid(x -> x != null,
        new InteractionException("Invalid input : null")).valid(x -> x.length() <= 100,
            new InteractionException("Address length over 100."));
    // the password should not be null
    new CheckValidity<String>(password).valid(x -> x != null,
        new InteractionException("Invalid input : null"));
    // then try to insert the information of the user into the database
    this.userId =
        DatabaseInsertHelper.insertNewUser(name, age, address, RolesMap.MAP.getMapping(roleType),
            password);
    this.password = password;

  }

  /**
   * This method is used to create a new user.
   * 
   * @param id the id of the given user
   * @param password the password of the user of given id
   * @throws InteractionException if the user input the invalid input
   * @throws InternalException there is something wrong in dealing with the database
   */
  protected User(int id, String password, Roles roleType)
      throws InteractionException, InternalException {
    // the id should be non-negative
    // password should not be null
    // the user must exist in the database
    CheckValidity.assert_prop(id >= 0, new InteractionException("Id can't be negative"));
    CheckValidity.assert_prop(password != null, new InteractionException("Invalid input : null"));
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getUserRole(id) == RolesMap.MAP.getMapping(roleType),
        new InternalException("Unexpected Exception: Role type inconsistency."));
    CheckValidity.assert_prop(DatabaseSelectHelper.getUserDetails(id) != null,
        new InteractionException("Inexistent User."));
    CheckValidity.assert_prop(
        PasswordHelpers.comparePassword(DatabaseSelectHelper.getPassword(id), password),
        new InteractionException("Wrong User name or password."));
    this.userId = id;
    this.password = password;


  }

  protected User(int id, Admin admin, Roles roleType)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getUserRole(id) == RolesMap.MAP.getMapping(roleType),
        new InternalException("Unexpected Exception: Role type inconsistency."));
    this.userId = id;
    this.password = null;
  }

  /**
   * This method is used for creating the new user.
   * 
   * @param name the name of the user
   * @param age the age of the user
   * @param address the address of the user
   * @param password the password set
   * @param type the type of the user
   * @return a new user of type store in type that contains all the input information
   * @throws UnexposedException if there is no constructor method in such series of class
   * @throws InteractionException the input value is invalid
   */
  public static User createUser(String name, int age, String address, String password, Roles type)
      throws InternalException, InteractionException {
    // get the type and map from the Roles enum

    return type.createUser().fx(name).fx(age).fx(address).fx(password);

  }

  /**
   * This method is used to get user from the database.
   * 
   * @param userId the user id that we need to get
   * @param admin who will get the user from the database
   * @return the user if the function is correct
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there are something wrong dealing with the database
   */
  public static User getUser(int userId, Admin admin)
      throws InteractionException, InternalException {
    // the input id should be non-negative
    CheckValidity.assert_prop(userId >= 0, new InteractionException("Id can't be negative"));
    // the admin should not be empty
    CheckValidity.assert_prop(admin != null, new InteractionException("Admin Unauthorized."));
    // call the map function to get the admin from the database
    int userRoleId = DatabaseSelectHelper.getUserRole(userId);
    Roles roletype = Roles.mapRole(userRoleId);

    return roletype.getUserByAdmin().fx(userId).fx(admin);
  }

  /**
   * This method is used to get user from the database.
   * 
   * @param userId the user id that we need to get
   * @param password the password of the id that need to be authenticated
   * @return the user if the function is correct
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there are something wrong dealing with the database
   */
  public static User getUser(int userId, String password)
      throws InteractionException, InternalException {
    if (!PasswordHelpers.comparePassword(DatabaseSelectHelper.getPassword(userId), password)) {
      throw new InteractionException(
          "Authenticated Failure : please check your password and user id again");
    }
    // Get the user role Id by databasehelper
    int userRoleId = DatabaseSelectHelper.getUserRole(userId);
    // Get the user role(type : Roles) of the User, into userRole
    Roles userRole = Roles.mapRole(userRoleId);


    return userRole.getUser().fx(userId).fx(password);
  }

  /**
   * This method is also used for get the user from the database.
   * 
   * @param userId the id of the user than we want to get from the database
   * @param password the password that need the authenticated
   * @param userRole the role type of the user
   * @return the corresponding user that have the corresponding id
   * @throws InteractionException the user input the invalid input
   * @throws InternalException there are something wrong dealing with the database
   */
  public static User getUser(int userId, String password, Roles userRole)
      throws InteractionException, InternalException {
    // get the user
    // map the type of the user
    CheckValidity.assert_prop(
        DatabaseSelectHelper.getUserRole(userId) == RolesMap.MAP.getMapping(userRole),
        new InteractionException("User.getUser : Inconsistent User Role."));

    return User.getUser(userId, password);
  }


  public static Roles getUserRole(int userId)
      throws UnexposedException, InternalException {
    return Roles.mapRole(DatabaseSelectHelper.getUserRole(userId));
  }

  public abstract Roles getUserRole();

  /**
   * This method will return the id of the user.
   * 
   * @return int that represents the id of the given user
   */
  public int getUserId() {
    return this.userId;
  }

  /**
   * This method will return the name of the user.
   * 
   * @return the name that extracted from the database
   * @throws InternalException if there is error in extracting the data
   */
  public String getName() throws InternalException {
    return DatabaseSelectHelper.getUserDetails(userId).left();
  }

  /**
   * This method is used to reset the name of the user.
   * 
   * @param name the new name that the user will have
   * @return true if the rename process is succeeded
   * @throws InternalException there is some thing wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public boolean setName(String name) throws InternalException, InteractionException {
    if (name == null) {
      throw new InteractionException("Name cannot be null");
    }
    return DatabaseUpdateHelper.updateUserName(name, this.userId);
  }

  /**
   * This method is used to get the age of the user from the database.
   * 
   * @return the age of the user
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is incorrect
   */
  public int getAge() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getUserDetails(userId).right().left();
  }

  /**
   * This method is used to set the age of the user and update the data in the database.
   * 
   * @param age the age of the given user
   * @return true if the rename process is correct
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public boolean setAge(int age) throws InternalException, InteractionException {
    if (age < 0) {
      throw new InteractionException("Unreasonable age value.");
    }
    return DatabaseUpdateHelper.updateUserAge(age, this.userId);

  }


  /**
   * This method will update the role id for the user.
   * 
   * @param roleType the new type of the user
   * @return the user with the new role
   * @throws InternalException if there is something wrong dealing with the database
   * @throws InteractionException the roleType does not exist in the database
   */
  public User returnUserWithNewRole(Roles roleType) throws InternalException, InteractionException {
    // Using the databaseUpdateHelper to update the corrseponding roleId of that user
    CheckValidity.assert_prop(this.password != null,
        new InteractionException("Admin Please Use Admin-type update-user-role."));

    DatabaseUpdateHelper.updateUserRole(Roles.mapRoleId(roleType), this.getUserId());
    User changedUser = null;
    try {
      changedUser = User.getUser(this.userId, this.password);
    } catch (InteractionException | InternalException e) {
      throw new InternalException("Unexpected Exception");
    }
    this.userId = -1;
    this.password = "Make it Invalid" + this.password;
    return changedUser;
  }

  /**
   * This will return the User by calling the corresponding method in the admin.
   * 
   * @param roleType the roleTpye that will be inserted
   * @param admin the admin who want to perform this process
   * @return The new role
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the roleType does not exist in the database
   */
  public User returnUserWithNewRole(Roles roleType, Admin admin)
      throws InternalException, InteractionException {
    // Using the databaseUpdateHelper to update the corrseponding roleId of that user
    DatabaseUpdateHelper.updateUserRole(Roles.mapRoleId(roleType), this.getUserId());
    User changedUser = null;
    try {
      changedUser = User.getUser(this.userId, admin);
    } catch (InteractionException | InternalException e) {
      throw new InternalException("Unexpected Exception");
    }
    this.userId = -1;
    this.password = "Make it Invalid" + this.password;
    return changedUser;
  }

  /**
   * This method is used to get the address of the user from the database.
   * 
   * @return the string that extracted from the database that represent the address of the user
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public String getAddress() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getUserDetails(this.userId).right().right();
  }

  /**
   * This method is used to set the user address and then update the information to the database.
   * 
   * @param address the new address the user will live
   * @return true if the update process is succeeded
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input the invalid input
   */
  public boolean setAddress(String address) throws InternalException, InteractionException {
    // check the validity of the input
    if (address == null || address.length() > 100) {
      throw new InteractionException(
          "Invalid address. The length of address must less than 100 and it cannot be null.");
    }
    return DatabaseUpdateHelper.updateUserAddress(address, this.userId);
  }

  /**
   * Set the password of the user with new password literally.
   * 
   * @param newpassword The new unhashed password.
   * @return True if succeed.
   * @throws InternalException If Unexpected happen.
   * @throws InteractionException If input unexpected.
   */
  public boolean setPasswordPlainly(String newpassword)
      throws InternalException, InteractionException {
    CheckValidity.assert_prop(password != null,
        new InteractionException("Password cannot be null"));
    return DatabaseUpdateHelper.updateUserPassword(newpassword, this.getUserId());

  }

  /**
   * Return the hashed password of the current user.
   * 
   * @return Password
   * @throws InternalException If Unexpected happened
   * @throws InteractionException If something inputs
   */
  public String getPassword()
      throws InternalException, InteractionException {
    return DatabaseSelectHelper.getPassword(this.getUserId());
  }



  /**
   * This method will initialize the message by call the corresponding function. which will always
   * corresponding to the data stored in the database
   * 
   * @param msg the string that represents the content of the message
   * @return the message class that have the information corresponding to the database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the message is msg
   */
  public Message newMessage(String msg)
      throws InternalException, InteractionException {
    return new Message(this.getUserId(), msg);
  }

  /**
   * This method will return a list of integer that contain the id of each message. by calling the
   * corresponding method in the Message class
   * 
   * @return list of the integer that represents the the id of all the messages in database
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public List<Integer> getAllMessageIds()
      throws InternalException, InteractionException {
    return Message.getAllMessageId(this.getUserId());
  }

  /**
   * This method will read the corresponding message with the corresponding messageId. and mutate
   * the status of the message
   * 
   * @param messageId the id of the message that you try to read
   * @return the string that represent the content of the database
   * @throws InternalException there is something wrong dealing the database
   * @throws InteractionException the message with given messageId does not exist in the database
   */
  public String readMessage(int messageId)
      throws InternalException, InteractionException {
    Message msg = Message.getMessage(this.getUserId(), messageId);
    msg.viewed();
    return msg.getMessage();
  }

  /**
   * This method will return the corresponding message with the corresponding messageId. and do not
   * mutate the status of the message
   * 
   * @param messageId the id of the message that you try to read
   * @return the string that represents the content of the database
   * @throws InternalException there is something wrong dealing the database
   * @throws InteractionException the message with given messageId does not exist in the database
   */
  public String checkMessage(int messageId)
      throws InternalException, InteractionException {
    Message msg = Message.getMessage(this.getUserId(), messageId);
    return msg.getMessage();
  }

  /**
   * This method will hash the new password and then store the hash version of the code to the
   * database.
   * 
   * @param newPassword the new string that need to be hashed and put into the database
   * @return the boolean to represent whether the process has succeeded or not
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the new password is not valid
   */
  public boolean setPassword(String newPassword) throws InternalException, InteractionException {
    // hash the password that will be inserted and stored in the database
    String password = PasswordHelpers.passwordHash(newPassword);
    this.password = newPassword;
    return this.setPasswordPlainly(password);
  }


}

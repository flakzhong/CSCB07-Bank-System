package com.bank.datainterface;

import com.bank.account.Account;
import com.bank.bank.Message;
import com.bank.bankdata.AccountImage;
import com.bank.bankdata.AccountTypesMap;
import com.bank.bankdata.DatabaseImage;
import com.bank.bankdata.Image;
import com.bank.bankdata.MessageImage;
import com.bank.bankdata.Mirror;
import com.bank.bankdata.RolesMap;
import com.bank.bankdata.UserAccountImage;
import com.bank.bankdata.UserImage;
import com.bank.databasehelper.CheckValidity;
import com.bank.databasehelper.Database;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;
import com.bank.tools.Dynatuple;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.User;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/* The core of database Recover and backup */
public class DataMeta {


  private static final DataIniter DatabaseInitHelper = DataOperator.initer(Database.DATABASE);

  // The file name for serialization
  private static final String serializationBackUpPath = "database_copy.ser";


  // The ability to serialize the Account
  private static final Mirror<Account> accountMirror =
      new Mirror<Account>() {
        @Override
        public Image<Account> injective(Account t) throws InternalException, InteractionException {

          return new AccountImage(t);
        }
      };

  // The ability to serialize the User
  private static final Mirror<User> userMirror =
      new Mirror<User>() {
        @Override
        public Image<User> injective(User t) throws InternalException, InteractionException {
          return new UserImage(t);
        }
      };

  // The ability to serialize the message
  private static final Mirror<Message> msgMirror =
      new Mirror<Message>() {
        @Override
        public Image<Message> injective(Message t) throws InternalException, InteractionException {

          return new MessageImage(t);
        }
      };

  // The ability to serialize the Dynatuple<Integer, Integer> (UserAccount)
  private static final Mirror<Dynatuple<Integer, Integer>> userAccountMirror =
      new Mirror<Dynatuple<Integer, Integer>>() {

        @Override
        public Image<Dynatuple<Integer, Integer>> injective(Dynatuple<Integer, Integer> t)
            throws InternalException, InteractionException {
          return new UserAccountImage(t.left(), t.right());
        }


      };

  // The ability to serialize the List of accounts
  private static final Mirror<List<Account>> accountsMirror =
      Mirror.duplicateMirror(accountMirror);

  // The ability to serialize the List of User
  private static final Mirror<List<User>> usersMirror =
      Mirror.duplicateMirror(userMirror);

  // The ability to serialize the list of user account asscoiation
  private static final Mirror<List<Dynatuple<Integer, Integer>>> userAccountsMirror =
      Mirror.duplicateMirror(userAccountMirror);

  // The ability to serialize the List of Message
  private static final Mirror<List<Message>> msgsMirror =
      Mirror.duplicateMirror(msgMirror);

  // The ability to serialize all the field
  private static final Mirror<Dynatuple<List<Account>,
      Dynatuple<List<User>,
          Dynatuple<List<Dynatuple<Integer, Integer>>,
              List<Message>>>>> databaseMirror =
                  Mirror.combineMirror(accountsMirror,
                      Mirror.combineMirror(usersMirror,
                          Mirror.combineMirror(userAccountsMirror, msgsMirror)));



  /**
   * Given admin, Backup the database through serialization
   * 
   * @param admin The given admin
   * @return DatabaseImage representing all the data of the database
   * @throws InteractionException if unexpected input.
   * @throws InternalException If unexpected happened.
   */
  public static DatabaseImage databaseRecord(Admin admin)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(admin != null,
        new InteractionException("Admin not authenticated."));

    List<Account> accountData = admin.getAllAccounts();

    List<User> userData = admin.getAllUsers();

    List<Dynatuple<Integer, Integer>> userAccountAssoc = new ArrayList<>();

    // Get all the User Account Association
    for (User eachuser : userData) {
      if (eachuser.getUserRole() == Roles.CUSTOMER) {
        Customer customer = (Customer) eachuser;
        customer.getAccounts().forEach(
            eachAccount -> userAccountAssoc.add(
                new Dynatuple<>(customer.getUserId(),
                    eachAccount.getAccountId())));
      }
    }

    // Get all the message
    List<Message> msgData = new ArrayList<>();
    for (User eachUser : userData) {
      msgData.addAll(Message.getAllMessages(eachUser.getUserId()));
    }

    // return the database image
    return new DatabaseImage(
        databaseMirror.injective(
            new Dynatuple<>(accountData,
                new Dynatuple<>(userData,
                    new Dynatuple<>(userAccountAssoc, msgData)))));

  }



  /**
   * Recover the database by given serialized file
   * 
   * @param admin The admin
   * @param image The image of the database
   * @return true if success
   * @throws InternalException if unexpected happened.
   */
  public static boolean databaseRecover(Admin admin, DatabaseImage image) throws InternalException {
    // Copy current DatabaseFiles into Database.bak
    CheckValidity.assert_prop(DatabaseInitHelper.trivialBackup(),
        new InternalException("Something unexpected happen."));
    try {
      DatabaseInitHelper.reInitialize();
      AccountTypesMap.MAP.updateMapping();
      RolesMap.MAP.updateMapping();
      image.injective();
    } catch (InteractionException | InternalException e) {
      CheckValidity.assert_prop(DatabaseInitHelper.trivialRecover(),
          new InternalException("Something has happened. Database Cannot be recovered."));
      throw new InternalException("Unexpected happened. We manage to recover the database.");
    }
    return true;


  }

  /**
   * Output the serialized file.
   * 
   * @param img Database image.
   * @return true if success.
   * @throws InternalException if unexpected happened.
   */
  public static boolean dataImageSerialize(DatabaseImage img) throws InternalException {
    FileOutputStream fileHandler = null;
    ObjectOutputStream serializationHandler = null;
    try {
      fileHandler = new FileOutputStream(serializationBackUpPath);
      serializationHandler = new ObjectOutputStream(fileHandler);

      serializationHandler.writeObject(img);

      serializationHandler.close();
      fileHandler.close();
    } catch (IOException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        if (fileHandler != null) {
          fileHandler.close();
        }
        if (serializationHandler != null) {
          serializationHandler.close();
        }
      } catch (IOException e) {
        ;
      }
    }

    return true;

  }

  /**
   * Database deserialize from the external storage.
   * 
   * @return DatabaseImage representing the deserialized information from external storage.
   * @throws InternalException if unexpected happened.
   */
  public static DatabaseImage dataImageDeserialize() throws InternalException {
    FileInputStream fileHandler = null;
    ObjectInputStream serializationHandler = null;
    DatabaseImage img = null;
    try {
      fileHandler = new FileInputStream(serializationBackUpPath);
      serializationHandler = new ObjectInputStream(fileHandler);
      img = (DatabaseImage) serializationHandler.readObject();
      serializationHandler.close();
      fileHandler.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new InternalException(e.getMessage());
    } finally {
      try {
        if (fileHandler != null) {
          fileHandler.close();
        }
        if (serializationHandler != null) {
          serializationHandler.close();
        }
      } catch (IOException e) {
        ;
      }
    }
    return img;
  }
}

package com.bank.bank;

import com.bank.databasehelper.CheckValidity;
import com.bank.databasehelper.Database;
import com.bank.datainterface.DataInserter;
import com.bank.datainterface.DataOperator;
import com.bank.datainterface.DataSelector;
import com.bank.datainterface.DataUpdator;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Fx;
import com.bank.user.Admin;
import java.util.ArrayList;
import java.util.List;

public class Message {
  private int receiver;
  private int messageId;

  private static final int MSG_LENGTH = 512;

  private static final DataInserter DatabaseInsertHelper = DataOperator.insert(Database.DATABASE);

  private static final DataSelector DatabaseSelectHelper = DataOperator.select(Database.DATABASE);

  private static final DataUpdator DatabaseUpdateHelper = DataOperator.update(Database.DATABASE);

  /**
   * This method will create a new message with the receiver id and the content of the message.
   * 
   * @param userId the id of the receiver
   * @param msg the content of the message
   * @throws InternalException there is something dealing with the database
   * @throws InteractionException the userid or the msg is not valid
   */
  public Message(int userId, String msg) throws InternalException, InteractionException {
    // make sure that the msg is not empty
    CheckValidity.assert_prop(msg != null, new InteractionException("Invalid input: Null"));
    // make sure that the length of the message is in the appropriate range
    CheckValidity.assert_prop(msg.length() <= Message.MSG_LENGTH,
        new InteractionException("Invalid input: String out of space."));
    // check whether the user is in the database
    CheckValidity.assert_prop(DatabaseSelectHelper.getUserDetails(userId) != null,
        new InteractionException("Inexistent User"));
    // it pass all the test, just insert the message to the database
    this.messageId = DatabaseInsertHelper.insertMessage(userId, msg);
    this.receiver = userId;
  }

  /**
   * This method will set the attribute of the message to be the receiver and the messageId.
   * 
   * @param userId the id of the userId
   * @param messageId the id of the message id
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is invalid
   */
  private Message(int userId, int messageId) throws InternalException, InteractionException {
    this.receiver = userId;
    this.messageId = messageId;
  }

  /**
   * This method will try to get the message from the database.
   * 
   * @param userId the id of the user
   * @param messageId the message id that the message own
   * @return the message class that represent the message with given id owning by the user
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user input is invalid
   */
  public static Message getMessage(int userId, int messageId)
      throws InternalException, InteractionException {
    // try to get the message
    CheckValidity.assert_prop(DatabaseSelectHelper.getSpecficMessage(messageId) != null,
        new InteractionException("Inexistent Message."));
    // try to make sure that the user have the access to the messageId
    CheckValidity.assert_prop(DatabaseSelectHelper.getAllMessage(userId).contains(messageId),
        new InteractionException("You don't have access."));

    return new Message(userId, messageId);

  }

  /**
   * This method will try to get the message as an admin.
   * 
   * @param admin the authenticated admin who can view the message
   * @param messageId the id of the message that the admin want to check
   * @return the specific Message the admin want to get(With the given messageid)
   * @throws InteractionException the user input is invalid
   * @throws InternalException there is something wrong dealing with the database
   */
  public static Message getMessage(Admin admin, int messageId)
      throws InteractionException, InternalException {
    CheckValidity.assert_prop(DatabaseSelectHelper.getSpecficMessage(messageId) != null,
        new InteractionException("Inexistent Message."));
    CheckValidity.assert_prop(admin != null, new InteractionException("Admin unauthenticated."));
    return new Message(-1, messageId);
  }

  /**
   * This method will return the string of the message.
   * 
   * @return the string that represents the contents of the message
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException there is something wrong dealing with the database
   */
  public String getMessage() throws InternalException, InteractionException {
    return DatabaseSelectHelper.getSpecficMessage(this.getMessageId());
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

  /**
   * This method will get all the message for the user with the given userId.
   * 
   * @param userId the id of the user that want to get the message
   * @return list of Message that owed by the user with the given userid
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user does not exist
   */
  public static List<Message> getAllMessages(int userId)
      throws InternalException, InteractionException {
    List<Integer> messageIds = DatabaseSelectHelper.getAllMessage(userId);
    return mapWithException(messageIds, x -> new Message(userId, x));
  }

  /**
   * This method will try to get all the message id of message owing by the current customer with
   * the given userid.
   * 
   * @param userId the id of the current customer
   * @return the list of integer that contain all the id of the user
   * @throws InternalException there is something wrong dealing with the database
   * @throws InteractionException the user does not exist
   */
  public static List<Integer> getAllMessageId(int userId)
      throws InternalException, InteractionException {
    return DatabaseSelectHelper.getAllMessage(userId);
  }

  /**
   * This method will return the receiver.
   * 
   * @return the receiver of the message
   */
  public int getReceiver() {
    return this.receiver;
  }

  /**
   * This method will set the state of the current message from unviewed to viewed.
   * 
   * @return the boolean whether the process has succeeded or not
   * @throws InteractionException there is something wrong dealing with the database
   * @throws InternalException there is something wrong dealing with the database
   */
  public boolean viewed() throws InteractionException, InternalException {
    return DatabaseUpdateHelper.updateUserMessageState(this.messageId);
  }

  /**
   * This method will return the id of the message.
   * 
   * @return the int that represents the id of the message
   */
  public int getMessageId() {
    return this.messageId;
  }


}

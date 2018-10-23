package com.bank.bankdata;

import com.bank.bank.Message;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.tools.Dynatuple;

/* An instance of Image<Message> */
public class MessageImage implements Image<Message> {

  // The reason we can cross platform
  private static final long serialVersionUID = 5700129240158849629L;
  // The message Image fields
  private Dynatuple<Integer, String> msgImage;

  /**
   * Construct a Message Image by given message.
   * 
   * @param msg The message.
   * @throws InternalException when unexpected happened.
   * @throws InteractionException When unexpected input.
   */
  public MessageImage(Message msg) throws InternalException, InteractionException {
    this.msgImage =
        new Dynatuple<>(msg.getReceiver(), msg.getMessage());
  }

  @Override
  public Message injective() throws InternalException, InteractionException {
    return new Message(this.msgImage.left(), this.msgImage.right());
  }


}

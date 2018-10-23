package com.bank.bankdata;

import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.Roles;
import com.bank.tools.Dynatuple;
import com.bank.user.User;


// The instance of Image<User>
public class UserImage implements Image<User> {

  // the reason we can cross platform
  private static final long serialVersionUID = 2802939125995245201L;

  // a field a User has,
  private Dynatuple<String, Dynatuple<Integer, Dynatuple<String, Dynatuple<String, Roles>>>> fields;


  /**
   * Construct User Image by User.
   * 
   * @param user The user.
   * @throws InternalException When unexpected happened.
   * @throws InteractionException Whe unexpected input.
   */
  public UserImage(User user) throws InternalException, InteractionException {
    this.fields = new Dynatuple<>(user.getName(),
        new Dynatuple<>(user.getAge(),
            new Dynatuple<>(user.getAddress(),
                new Dynatuple<>(user.getPassword(), user.getUserRole()))));
  }

  @Override
  public User injective() throws InternalException, InteractionException {

    User user = User.createUser(
        this.fields.left(),
        this.fields.right().left(),
        this.fields.right().right().left(),
        this.fields.right().right().right().left(),
        this.fields.right().right().right().right());
    user.setPasswordPlainly(this.fields.right().right().right().left());
    return user;


  }


}

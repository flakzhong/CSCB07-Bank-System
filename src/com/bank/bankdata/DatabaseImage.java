package com.bank.bankdata;

import com.bank.account.Account;
import com.bank.bank.Message;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.tools.Dynatuple;
import com.bank.user.User;
import java.util.List;

/*
 * The instance of Image<DynatupleList<List<Account>, List<User>, List<UserAccount>,
 * List<Message>>>>> It's actually a type alias, so that I don't need to write a such long type all
 * the time
 */

public class DatabaseImage
    implements Image<Dynatuple<List<Account>,
        Dynatuple<List<User>,
            Dynatuple<List<Dynatuple<Integer, Integer>>,
                List<Message>>>>> {


  // the reason we can cross platform
  private static final long serialVersionUID = 7200030249540609131L;
  private Image<Dynatuple<List<Account>,
      Dynatuple<List<User>,
          Dynatuple<List<Dynatuple<Integer, Integer>>,
              List<Message>>>>> databaseImage;

  // The field of the instance
  public DatabaseImage(Image<Dynatuple<List<Account>,
      Dynatuple<List<User>,
          Dynatuple<List<Dynatuple<Integer, Integer>>,
              List<Message>>>>> databaseImage) {
    this.databaseImage = databaseImage;
  }

  // Actually a wrapper
  @Override
  public
      Dynatuple<List<Account>,
          Dynatuple<List<User>, Dynatuple<List<Dynatuple<Integer, Integer>>, List<Message>>>>
      injective() throws InternalException, InteractionException {
    // TODO Auto-generated method stub
    return this.databaseImage.injective();
  }


}

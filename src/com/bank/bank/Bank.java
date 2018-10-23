package com.bank.bank;

import com.bank.account.Account;
import com.bank.bankdata.AccountTypesMap;
import com.bank.databasehelper.DatabaseDriverHelper;
import com.bank.exceptions.InteractionException;
import com.bank.exceptions.InternalException;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import com.bank.user.Admin;
import com.bank.user.Customer;
import com.bank.user.Teller;
import com.bank.user.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bank {

  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  private static void atmInteract(Atm atm)
      throws InternalException, InteractionException, IOException {
    Customer cus = atm.getCurrentCustomer();
    System.out.println("Hello, " + cus.getName());
    System.out.println("You live at: " + cus.getAddress());
    List<Account> accounts = cus.getAccounts();
    // check the customer has account or not
    // if he has, we list all the accounts and their information
    if (accounts.size() != 0) {
      System.out.println("You have accounts: ");
      for (Account acc : accounts) {
        System.out.println("Type: " + acc.getAccountType().toString()
            + " Id: "
            + acc.getAccountId()
            + " Name: "
            + acc.getName()
            + " Balance: "
            + acc.getBalance());

      }
    } else {
      // if size == 0, then the customer has no account.
      System.out.println("You have no account.");
    }
    // context menu

    System.out.println("Select the operation you want to do.");
    System.out.println("1. Make Deposit");
    System.out.println("2. Make withdrawal");
    System.out.println("3. Check message");
    System.out.println("4. Exit");
    String userCommand = br.readLine();
    while (!userCommand.equals("4")) {
      // if the customer wants to deposit money
      if (userCommand.equals("1")) {
        try {
          System.out.print("Enter the id of the account that you want to deposit money in: ");
          int accountId = Integer.parseInt(br.readLine());
          System.out.print("Enter the amount of money you want to deposit: ");
          BigDecimal amount = new BigDecimal(br.readLine());
          atm.makeDeposit(amount, accountId);
          // this means user entered some invalid input
        } catch (InteractionException e) {
          System.out.println("Invalid Inputs.");
          // this means the account id input by the customer doesn't belong to him/her
        } catch (InternalException e) {
          System.out.println("You don't have access to this account.");
          // this means user entered non-numeric value.
        } catch (NumberFormatException e) {
          System.out.println("Invalid accountId.");
        }
      } else if (userCommand.equals("2")) {
        // if the customer wants to withdrawal
        try {
          System.out.print("Enter the id of the account that you want to make withdraw: ");
          int accountId = Integer.parseInt(br.readLine());
          System.out.print("Enter the amount of money: ");
          BigDecimal amount = new BigDecimal(br.readLine());
          atm.makeWithdrawl(amount, accountId);
          System.out.println("Successfully withdrawl " + String.valueOf(amount)
              + " from account "
              + String.valueOf(accountId)
              + ".");
        } catch (InteractionException e) {
          System.out.println(e.getMessage());
          // this means the account id input by the customer doesn't belong to him/her
        } catch (InternalException e) {
          System.out.println(e.getMessage());
          // this means user entered non-numeric value.
        } catch (NumberFormatException e) {
          System.out.println("Invalid accountId.");
        }
      } else if (userCommand.equals("3")) {
        List<Integer> messageIds = cus.getAllMessageIds();
        String allMsgInfo = "";
        for (Integer curr : messageIds) {
          allMsgInfo = curr + ": "
              + cus.checkMessage(curr)
              + "\n"
              + allMsgInfo;
        }
        if (allMsgInfo.length() > 0) {
          System.out.print(allMsgInfo);
          System.out.print("Press enter to return to the main menu.");
          br.readLine();
        } else {
          System.out.println("You don't have any message.");
        }

      } else {
        System.out.println("Invalid selection, select again");
      }


      // after the user finishes what they wanted to do, we keep loop the context menu until they
      // choose to exit
      for (Account acc : cus.getAccounts()) {
        System.out.println("Type: " + acc.getAccountType().toString()
            + " Id: "
            + acc.getAccountId()
            + " Name: "
            + acc.getName()
            + " Balance: "
            + acc.getBalance());
      }
      System.out.println("Select the operation you want to do.");
      System.out.println("1. Make Deposit");
      System.out.println("2. Make withdrawal");
      System.out.println("3. Check message");
      System.out.println("4. Exit");
      userCommand = br.readLine();
    }
    // if they choose 3 to exit, we exit.
    System.out.print("System closed.");
    System.exit(0);
  }

  private static int changeToAtm() throws IOException, InternalException, InteractionException {
    Atm atm = new AtmImpl();
    // ask user to authenticate the atm
    try {
      System.out.print("Please enter your user(customer) id: ");
      int userId = Integer.parseInt(br.readLine());
      System.out.print("Please enter your password: ");
      String userPassword = br.readLine();
      atm.authenticateCustomer(userId, userPassword);
      // non-numeric id, wrong password or non-exist user id all mean that the what the user entered
      // is invalid
    } catch (InternalException | InteractionException | NumberFormatException e) {
      System.out.println("Invalid Id or password, please check.");
      // we return 10 in order to go back to the context menu.
      return 10;
    }
    // if the customer successfully authenticate, we change to the atm interface.
    atmInteract(atm);
    return 0;
  }

  private static int askForContextInput()
      throws IOException, InternalException, InteractionException {
    // show the context menu to the user
    System.out.print("1 - TELLER Interface" + "\n"
        + "2 - ATM Interface"
        + "\n"
        + "0 - Exit"
        + "\n"
        + "Enter Selection:");
    String input = br.readLine();
    // read user's input
    // 1 means entering a teller interface.
    if (input.equals("1")) {
      tellerLogIn();
      return 1;
      // 2 means an ATM interface
    } else if (input.equals("2")) {
      int loop = 1;
      while (loop == 1) {
        // change to atm interface if the user choose to do so.
        loop = changeToAtm();
      }
      return 1;
      // 0 means exit
    } else if (input.equals("0")) {
      return 0;
    } else {
      // if user enter something besides 0 1 2, we return 1 to the main to keep asking user until
      // they choose 0 1 or 2
      return 1;
    }
  }

  private static void interactWithCus(TellerTerminal tt)
      throws IOException, InternalException, InteractionException {
    // get current customer's and his/her accounts' information
    System.out.println("Customer: " + tt.getCurrentCustomer().getName()
        + " Address: "
        + tt.getCurrentCustomer().getAddress());
    System.out.println("Current customer accounts list: ");
    for (Account acc : tt.getCurrentCustomer().getAccounts()) {
      System.out.println("Type: " + acc.getAccountType().toString()
          + " Id: "
          + acc.getAccountId()
          + " Name: "
          + acc.getName()
          + " Balance: "
          + acc.getBalance()
          + "$");
    }
    // show the options.
    System.out.println("Select the operation you want:\n" + "1. Make new account"
        + "\n"
        + "2. Give interest"
        + "\n"
        + "3. Make a deposit"
        + "\n"
        + "4. Make a withdrawal"
        + "\n"
        + "5. Read customer's message"
        + "\n"
        + "6. View the total balance of the current customer\n"
        +
        "7. Mutate the name of the customer\n"
        + "8. Mutate the address of the customer\n"
        + "9. Mutate the password of the customer\n"
        + "10. close customer session");
    String tellerCommand = br.readLine();
    while (!tellerCommand.equals("10")) {
      if (tellerCommand.equals("1")) {
        // if the customer wants to make a new account.
        try {
          System.out.println("Please enter the name of the new account: ");
          String name = br.readLine();
          System.out.println("Please set the balance: ");
          BigDecimal balance;
          balance = new BigDecimal(br.readLine());
          String typeOption = "";
          // get account type and corresponding type id
          Map<Integer, AccountTypes> typeIdMap = new HashMap<Integer, AccountTypes>();
          for (AccountTypes typeEnum : AccountTypes.values()) {
            int typeId = AccountTypesMap.MAP.getMapping(typeEnum);
            String currAccountType = typeEnum.toString();
            // we shouldn't show restricted type and delete account type to the customer.
            // only admin can know it.
            if (!currAccountType.equals("DELETEAT")) {
              typeOption += String.valueOf(typeId) + ": "
                  + typeEnum.toString()
                  + ", ";
              typeIdMap.put(typeId, typeEnum);
            }
          }
          System.out.println("You can create the following kinds of accounts: ");
          System.out.println(typeOption);
          System.out.print("Please enter the account type id: ");
          int typeId = Integer.parseInt(br.readLine());
          // after we gathering the information, we proceed to the teller terminal to make new
          // account.
          AccountTypes type = typeIdMap.get(typeId);
          // if user enter non-existing account type, we stop it and ask him/her to re-enter.
          while (type == null) {
            System.out.println("Do not have such kind of account type.");
            System.out.println("You can create the following kinds of accounts: ");
            System.out.println(typeOption);
            System.out.print("Please enter the account type id: ");
            typeId = Integer.parseInt(br.readLine());
            type = typeIdMap.get(typeId);
          }
          // account balance should be greater or equal to 0 when created
          if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            int accountId = tt.makeNewAccount(name, balance, type);
            System.out.println(
                "The account has been created with id: " + String.valueOf(accountId));
          } else {
            System.out.println("The balance for the new account cannot be less than 0.");
          }
          // any kind exception means the input is invalid.
        } catch (Exception e) {
          System.out.println("Invalid input.");
        }
        // if the teller wants to give interest to the account.
      } else if (tellerCommand.equals("2")) {
        try {
          System.out.print("Please enter the id of the account to be given interest: ");
          int accountId = Integer.parseInt(br.readLine());
          tt.giveInterest(accountId);
          System.out.println("Successfully gave interest to account " + String.valueOf(accountId)
              + ".");
        } catch (InternalException e) {
          System.out.println("You don't have access to this account.");
        } catch (NumberFormatException e) {
          System.out.println("Invalid accountId.");
        } catch (Exception e) {
          System.out.println("You don't have access to this account.");
        }
      } else if (tellerCommand.equals("3")) {
        // try to make deposit
        try {
          System.out.print("Please enter the id of the account to be deposited: ");
          int accountId = Integer.parseInt(br.readLine());
          System.out.print("Please enter the amount of money that the customer wants to deposit: ");
          BigDecimal amount = new BigDecimal(br.readLine());
          tt.makeDeposit(amount, accountId);
          System.out.println("Successfully deposit " + String.valueOf(amount)
              + " to account "
              + String.valueOf(accountId)
              + ".");
        } catch (InteractionException e) {
          System.out.println(e.getMessage());
        } catch (InternalException e) {
          System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
          System.out.println("Invalid accountId.");
        } catch (Exception e) {
          System.out.println("You don't have access to this account.");
        }
      } else if (tellerCommand.equals("4")) {
        // try to withdrawal
        try {
          System.out.print("Please enter the id of the account to be withdrawal. ");
          int accountId = Integer.parseInt(br.readLine());
          System.out.print(
              "Please enter the amount of money that the customer wants to withdrawal: ");
          BigDecimal amount = new BigDecimal(br.readLine());
          tt.makeWithdrawl(amount, accountId);
          System.out.println("Successfully withdrawl " + String.valueOf(amount)
              + " from account "
              + String.valueOf(accountId)
              + ".");
        } catch (InternalException e) {
          System.out.println("You don't have access to this account.");
        } catch (InteractionException e) {
          System.out.println(e.getMessage());
          // when the accountId is larger than the maximum integer value, it will throw this
          // exception
        } catch (NumberFormatException e) {
          System.out.println("Invalid accountId.");
        } catch (Exception e) {
          System.out.println("You don't have access to this account.");
        }
      } else if (tellerCommand.equals("5")) {
        List<Integer> messageIds = tt.getCurrentCustomer().getAllMessageIds();
        String allMsgInfo = "";
        for (Integer curr : messageIds) {
          allMsgInfo = curr + ": "
              + tt.getCurrentCustomer().checkMessage(curr)
              + "\n"
              + allMsgInfo;
        }
        if (allMsgInfo.length() > 0) {
          System.out.println("The customer has the following message: ");
          System.out.print(allMsgInfo);
          System.out.print("Press enter to return to the main menu.");
          br.readLine();
        } else {
          System.out.println("The customer doesn't have any message.");
        }
      } else if (tellerCommand.equals("6")) {
        BigDecimal tem = null;
        try {
          tem = tt.allBalanceOfCurrentCusomter();
        } catch (InteractionException | InternalException e) {
          System.out.println("There is error in checking the balance of the current customer");
        }
        if (tem != null) {
          System.out.println("The total balance of the customer I am serving is " + tem);
          System.out.println("Then move baack to the customer command menu");
        } else {
          System.out.println("Since you do not input the valid user id, return to the menu");
        }
      } else if (tellerCommand.equals("7")) {
        System.out.println("The new name of the customer");
        String name = br.readLine();
        try {
          tt.setCustomerName(name);
        } catch (InternalException e) {
          System.out.println("Failed to update the name due to the invalid name");
        } catch (InteractionException x) {
          System.out.println("Failed to update the name due to the crash of the database");
        }
      } else if (tellerCommand.equals("8")) {
        System.out.println("The new address of the customer");
        String address = br.readLine();
        try {
          tt.setCustomerAddress(address);
        } catch (InternalException e) {
          System.out.println("Failed to update the address due to the invalid input");
        } catch (InteractionException x) {
          System.out.println("Failed to update the address due to the crash of the database");
        }
      } else if (tellerCommand.equals("9")) {
        System.out.println("The new password of the customer");
        String newPassWord = br.readLine();
        try {
          tt.setCustomerPassword(newPassWord);
        } catch (InternalException e) {
          System.out.println("Failed to update the password due to the invalid name");
        } catch (InteractionException x) {
          System.out.println("Failed to update the password due to the crash of the database");
        }
      } else {
        // when the user doesn't type given option number.
        // we ask them to re-enter.
        System.out.print("Invalid Input, enter your choice again.");
      }
      // shoiw the menu again
      System.out.println("Customer: " + tt.getCurrentCustomer().getName()
          + " Address: "
          + tt.getCurrentCustomer().getAddress());
      System.out.println("Current customer accounts list: ");
      for (Account acc : tt.getCurrentCustomer().getAccounts()) {
        System.out.println("Type: " + acc.getAccountType().toString()
            + " Id: "
            + acc.getAccountId()
            + " Name: "
            + acc.getName()
            + " Balance: "
            + acc.getBalance());
      }
      System.out.println("Select the operation you want:\n" + "1. Make new account"
          + "\n"
          + "2. Give interest"
          + "\n"
          + "3. Make a deposit"
          + "\n"
          + "4. Make a withdrawal"
          + "\n"
          + "5. Read customer's message"
          + "\n"
          + "6. View the total balance of the current customer\n"
          + "7. Mutate the name of the customer\n"
          + "8. Mutate the address of the customer\n"
          + "9. Mutate the password of the customer\n"
          + "10. close customer session");
      tellerCommand = br.readLine();
    }
    // when the teller enters 5, we close current customer's session.
    // we deauthenticate current customer.
    tt.deauthenticateCustomer();
  }

  private static void tellerLogIn() throws IOException, InternalException {
    try {
      System.out.print("Please enter a teller id(Note: You have only one chance to log in.): ");
      int tellerId = Integer.parseInt(br.readLine());
      System.out.print("Please enter a Password: ");
      String password = br.readLine();
      TellerTerminal tt = new TellerTerminalImpl(tellerId, password);
      // if no exception is thrown, we change to teller interface.
      askForTellerInput(tt);
      // catch possible exceptions.
    } catch (InteractionException | NumberFormatException | InternalException e) {
      System.out.println("Failed to log in. Please check your id and password.");
      System.out.println("System closed successfully.");
      System.exit(1);
    }
  }


  private static void askForTellerInput(TellerTerminal tt) throws IOException, InternalException {
    System.out.println("Hello, you have successfully logged in.");
    // when the teller logs in the first time, there isn't an authenticated customer in the terminal
    boolean havecus = false;
    while (!havecus) {
      // therefore we ask the teller to authenticate a customer.
      System.out.println(
          "You have no customer right now, please select the operation you want to do.");
      System.out.print("1. authenticate new user" + "\n"
          + "2. Make new customer"
          + "\n"
          + "3. Read your messages"
          + "\n"
          + "4. Send a message to customer"
          + "\n"
          + "5. Exit"
          + "\n");
      String tellerCommand = br.readLine();
      // we keep looping until the teller chose to exit or an valid customer id and password is
      // entered.
      while (!tellerCommand.equals("5")) {
        if (tellerCommand.equals("1")) {
          try {
            // try to authenticate a customer
            System.out.print("Enter the id of your customer: ");
            int customerId = Integer.parseInt(br.readLine());
            System.out.print("Enter the password of your customer: ");
            String cusPassword = br.readLine();
            tt.authenticateCustomer(customerId, cusPassword);
            interactWithCus(tt);
            // catch possible exceptions.
          } catch (InternalException | InteractionException | NumberFormatException e) {
            System.out.println("Authentication failed.");
          }
        } else if (tellerCommand.equals("2")) {
          try {
            // try to create new customer
            System.out.print("Please enter the name of the new customer: ");
            String name = br.readLine();
            System.out.print("Please set the age of the customer: ");
            int age = Integer.parseInt(br.readLine());
            System.out.print("Please set the address of the customer: ");
            String address = br.readLine();
            System.out.print("Please set the password: ");
            String userPassword = br.readLine();
            int cusId = tt.makeNewCustomer(name, age, address, userPassword);
            // if no exception is thrown, that means the customer has authenticated successfully
            // we change to another interface.
            System.out.println("New customer has been created with id " + String.valueOf(cusId));
            interactWithCus(tt);
          } catch (InteractionException | NumberFormatException e) {
            System.out.println("Please check whether the input values are valid.");
          }
        } else if (tellerCommand.equals("3")) {
          try {
            List<Integer> messageIds = tt.listAllMessageOfCurrentTeller();
            String allMsgInfo = "";
            for (Integer curr : messageIds) {
              allMsgInfo = curr + ": "
                  + tt.checkMessage(curr)
                  + "\n"
                  + allMsgInfo;
            }
            if (allMsgInfo.length() > 0) {
              System.out.println("You have the following message: ");
              System.out.print(allMsgInfo);
              System.out.print("Press enter to return to the main menu.");
              br.readLine();
            } else {
              System.out.println("You don't have any message.");
            }
          } catch (Exception e) {
            ;
          }
        } else if (tellerCommand.equals("4")) {
          try {
            System.out.print(
                "Please enter the id of the receiver(must be a customer) of the message: ");
            int customerId = Integer.parseInt(br.readLine());
            System.out.print("Please enter the message: ");
            String message = br.readLine();
            tt.leaveMessage(customerId, message);
            System.out.println("Message sent successfully.");
          } catch (InteractionException | InternalException e) {
            System.out.println("You cannot send a message to current receiver.");
          } catch (NumberFormatException e) {
            System.out.println("Invalid id.");
          }
        } else {
          System.out.println("Invalid command.");
        }
        // if the teller fails to authenticate a customer, we keep asking him/her to authenticate or
        // create customer.
        System.out.println(
            "You have no customer right now, please select the operation you want to do.");
        System.out.print(
            "1. authenticate new user" + "\n"
                + "2. Make new User"
                + "\n"
                + "3. Read your messages"
                + "\n"
                + "4. Send a message to customer"
                + "\n"
                + "5. Exit"
                + "\n");
        tellerCommand = br.readLine();
      }
      // if teller enters 3, we exit.
      System.out.print("System closed.");
      System.exit(0);
    }
  }

  private static int createNewAdmin() throws IOException, InternalException, InteractionException {
    try {
      System.out.print("Enter admin name: ");
      String adminName = br.readLine();
      System.out.print("Please enter a non-negative integer to set the admin's age: ");
      int adminAge = Integer.parseInt(br.readLine());
      System.out.print("Please enter the address of the admin(1-100 characters): ");
      String adminAddress = br.readLine();
      System.out.print("Set the password(input password must be non-empty): ");
      String adminPassword = br.readLine();
      // after gathering the information, we create an admin and print corresponding id
      int adminUserId = User.createUser(adminName, adminAge, adminAddress, adminPassword,
          Roles.ADMIN).getUserId();
      System.out.println("Admin created with id " + String.valueOf(adminUserId));
      return 0;
      // if either of the following two exception is thrown, that means the user entered something
      // invalid
    } catch (InteractionException | NumberFormatException e) {
      System.out.println("Invalid input values.");
    }
    return 1;

  }

  private static int createNewTeller() throws IOException, InternalException {
    try {
      System.out.print("Enter teller name: ");
      String name = br.readLine();
      System.out.print("Set teller age: ");
      int age = Integer.parseInt(br.readLine());
      System.out.print("Enter the address of the teller: ");
      String address = br.readLine();
      System.out.print("Set the password: ");
      String password = br.readLine();
      // after gathering the information, we create an teller and print corresponding id
      int userId = User.createUser(name, age, address, password, Roles.TELLER).getUserId();
      System.out.println("Teller created with id " + String.valueOf(userId));
      return 0;
      // if either of the following two exception is thrown, that means the user entered something
      // invalid
    } catch (InteractionException | NumberFormatException e) {
      System.out.println("Invalid input values.");
    }
    return 1;
  }

  private static void seeCus(AdminTerminal at)
      throws InternalException, InteractionException, IOException {
    List<Customer> listCus = at.allCustomers();
    for (Customer i : listCus) {
      System.out.println("ID: " + String.valueOf(i.getUserId())
          + " Name: "
          + i.getName()
          + " Address: "
          + i.getAddress());
    }
    // if this bank has customer
    if (listCus.size() != 0) {
      // we should've print all customer's id and their basic information
      // now the admin can choose to check accounts' information of a specific customer by entering
      // their customer id.
      System.out.println("Input the customer's Id to see the account "
          + "details of the customer or input 0 to go back.");
      String choice = br.readLine();
      // 0 means exit.
      while (!choice.equals("0")) {
        Customer chosenCustomer = null;
        for (Customer i : listCus) {
          if (i.getUserId() == Integer.parseInt(choice)) {
            // if the chosen customer exists, we assign current customer to it.
            chosenCustomer = i;
            List<Account> accounts = i.getAccounts();
            // print all the accounts of the customer if exist.
            if (accounts.size() > 0) {
              for (Account acc : accounts) {
                System.out.println(
                    "Type: " + acc.getAccountType().toString()
                        + " Id: "
                        + acc.getAccountId()
                        + " Name: "
                        + acc.getName()
                        + " Balance: "
                        + acc.getBalance());
              }
            } else {
              // it is possible for a customer to have no account.
              System.out.println("This customer doesn't have any account.");
            }
          }
        }
        if (chosenCustomer == null) {
          System.out.println("This customer doesn't exist.");
        }
        System.out.println("Input the customer's Id to see the account "
            + "details of the customer or input 0 to go back.");
        choice = br.readLine();
      }
    } else {
      // it is possible for a bank to have no customer.
      System.out.println("There is no customer in the database.");
    }
  }

  private static int changeToAdmin() throws IOException, InternalException {
    try {
      System.out.print("Please enter an admin id(Note: You have only one chance to log in.): ");
      int id = Integer.parseInt(br.readLine());
      System.out.print("Please enter a Password: ");
      String password = br.readLine();
      AdminTerminal at = new AdminTerminalImpl(id, password);
      // if no exception is thrown when logging in the admin terminal
      // then we change to admin interface.
      interactWithAdmin(at);
      return 0;
      // if there is some exception is thrown, that means invalid information is entered.
      // we exit directly
    } catch (InteractionException | NumberFormatException | InternalException e) {
      System.out.println("Incorrect Id or password.");
      System.out.println("System closed successfully");
      System.exit(1);
      return 1;
    }
  }

  private static void interactWithAdmin(AdminTerminal at)
      throws IOException, InternalException, InteractionException {
    // show the menu
    System.out.println("Select the operation you want to do:\n" + "1 - create a new admin\n"
        + "2 - create a new teller\n"
        + "3 - view all admins\n"
        + "4 - view all tellers\n"
        + "5 - view all customers\n"
        + "6 - check your own message\n"
        + "7 - check specific message\n"
        + "8 - send message\n"
        + "9 - databaseDaemon\n"
        + "10 - databaseRecover\n"
        + "11 - promote teller to admin\n"
        + "12 - view all balance of a customer\n"
        + "13 - View all the money in the bank\n"
        +
        "14 - exit");
    String selection = br.readLine();
    // keep looping until user choose to exit
    while (!selection.equals("14")) {
      if (selection.equals("1")) {
        int loop = 1;
        while (loop == 1) {
          loop = createNewAdmin();
        }
      } else if (selection.equals("2")) {
        int loop = 1;
        while (loop == 1) {
          loop = createNewTeller();
        }
      } else if (selection.equals("3")) {
        List<Admin> listAdmin = at.allAdmins();
        // list all admin and corresponding information
        for (Admin i : listAdmin) {
          System.out.println("ID: " + String.valueOf(i.getUserId())
              + " Name: "
              + i.getName()
              + " Address: "
              + i.getAddress());
        }

      } else if (selection.equals("4")) {
        List<Teller> listTeller = at.allTellers();
        // list all teller and corresponding information
        for (Teller i : listTeller) {
          System.out.println("ID: " + String.valueOf(i.getUserId())
              + " Name: "
              + i.getName()
              + " Address: "
              + i.getAddress());
        }
        if (listTeller.size() == 0) {
          System.out.println("There is no teller in the system.");
        }
      } else if (selection.equals("5")) {
        seeCus(at);
      } else if (selection.equals("6")) {
        try {
          List<Integer> messageIds = at.listAllMessages();
          String allMsgInfo = "";
          for (Integer curr : messageIds) {
            allMsgInfo = curr + ": "
                + at.viewMessage(curr)
                + "\n"
                + allMsgInfo;
          }
          if (allMsgInfo.length() > 0) {
            System.out.println("You have the following message: ");
            System.out.print(allMsgInfo);
            System.out.print(
                "Enter 0 to mark all message as viewed.\n"
                    + "Enter message Id to mark specific message as viewed.\n"
                    + "Enter anyting else will return to the main menu"
                    + " and all messages' status will not change.\n");
            String entered = br.readLine();
            int userChoice = Integer.parseInt(entered);
            if (userChoice == 0) {
              for (Integer curr : messageIds) {
                at.updateMessagestatus(curr);
              }
              System.out.println("All messages' status are viewed.");
            } else {
              if (messageIds.contains(userChoice)) {
                at.updateMessagestatus(userChoice);
                System.out.println("The message's status is viewed.");
              } else {
                System.out.println(
                    "Selected message is not in your message list."
                        + " All messages' status remain the same.");
              }
            }
          } else {
            System.out.println("You don't have any message.");
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid message id.");
        }
      } else if (selection.equals("7")) {
        System.out.print("Please enter the id of the message that you want to view: ");
        String entered = br.readLine();
        try {
          int messageId = Integer.parseInt(entered);
          String message = at.viewMessage(messageId);
          System.out.println("This is the message: " + message);
          System.out.println("Enter 0 as viewed, anything else as untouched.");
          try {
            if (Integer.parseInt(br.readLine()) == 0) {
              at.updateMessagestatus(messageId);
              System.out.println("Message viewed");
            } else {
              System.out.println("Message status remain the same.");
            }
          } catch (NumberFormatException e) {
            System.out.println("Message status remain the same.");
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid message id.");
        } catch (InternalException | InteractionException e) {
          System.out.println("No such a message");
        }
      } else if (selection.equals("8")) {
        try {
          System.out.print("Please enter the id of the receiver of the message: ");
          int userId = Integer.parseInt(br.readLine());
          System.out.print("Please enter the message: ");
          String message = br.readLine();
          at.leaveMessage(userId, message);
          System.out.println("Message sent successfully.");
        } catch (InteractionException | InternalException e) {
          System.out.println("Input user id doesn't exist.");
        } catch (NumberFormatException e) {
          System.out.println("Invalid id.");
        }
      } else if (selection.equals("9")) {
        at.databaseDaemon();
      } else if (selection.equals("10")) {
        System.out.println("Please Restart the system.");
        at.databaseRecover();
      } else if (selection.equals("11")) {
        System.out.print("Please enter the teller's id:");
        try {
          int userId = Integer.parseInt(br.readLine());
          at.promoteTeller(userId);
          System.out.println("promote successfully.");
        } catch (NumberFormatException e) {
          System.out.println("Invalid id.");
        } catch (InternalException | InteractionException e) {
          System.out.println("No such a teller");
        }
      } else if (selection.equals("12")) {
        System.out.println("Please enter the user id");
        BigDecimal tem = null;
        try {
          int userId = Integer.parseInt(br.readLine());
          tem = at.balanceOfaCustomer(userId);
        } catch (InteractionException | InternalException e) {
          System.out.println("Input user id doesn't exist");
        } catch(NumberFormatException e) {
          System.out.println("Invalid ID.");
        }
        if (tem != null) {
          System.out.println("The balance of the customer with the given id is " + tem);
        } else {
          System.out.println("Since you do not input the valid user id, return to the menu");
        }
      } else if (selection.equals("13")) {
        BigDecimal tem = null;
        try {
          tem = at.balanceOfAllAcount();
        } catch (InteractionException | InternalException e) {
          System.out.println("Can not read all the balance from the bank");
        }
        if (tem != null) {
          System.out.println("The total amount of money stored in the bank is" + tem);
        } else {
          System.out.println("Can not read this information from the database");
        }
      } else {
        System.out.println("Invalid choice, choose again");
      }
      // show the menu again.
      System.out.println("Select the operation you want to do:\n" + "1 - create a new admin\n"
          + "2 - create a new teller\n"
          + "3 - view all admins\n"
          + "4 - view all tellers\n"
          + "5 - view all customers\n"
          + "6 - check your own message\n"
          + "7 - check specific message\n"
          + "8 - send message\n"
          + "9 - databaseDaemon\n"
          + "10 - databaseRecover\n"
          + "11 - promote teller to admin\n"
          + "12 - view all balance of a customer\n"
          + "13 - View all the money in the bank\n"
          + "14 - exit");
      selection = br.readLine();
    }
    System.out.print("System closed.");
    System.exit(1);
  }

  /**
   * This is the main method to run your entire program! Follow the Candy Cane instructions to
   * finish this off.
   * 
   * @param argv unused.
   */
  public static void main(String[] argv) {
    try {
      if (argv.length == 1 && argv[0].equals("-1")) {
        System.out.println("You are initializing the database and creating an admin now.");
        DatabaseDriverHelper.initializeDatabase();
        int loop = 1;
        while (loop == 1) {
          loop = createNewAdmin();
        }
      } else if (argv.length == 1 && argv[0].equals("1")) {
        int loop = 1;
        while (loop == 1) {
          loop = changeToAdmin();
        }
      } else {
        System.out.println("Select the interface you want to use.");
        int loop = 1;
        while (loop == 1) {
          loop = askForContextInput();
        }
      }
    } catch (Exception e) {
      System.out.println("Oops, system crashed.");
      System.out.println(e.getClass() + e.getMessage());
    } finally {
      System.out.println("System exited.");
    }
  }
}

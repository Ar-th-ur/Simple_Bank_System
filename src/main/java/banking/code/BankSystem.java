package banking.code;

import org.sqlite.SQLiteDataSource;

import java.util.Scanner;

public class BankSystem {
	private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
	private final BankDataBase dataBase;
	private boolean isLogged = false;

	public BankSystem(String url) {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:" + url);
		this.dataBase = BankDataBase.getInstance(dataSource);
		dataBase.init();
	}

	/**
	 * Starts the program
	 */
	public void launch() {
		Card currentCard = null;
		while (true) {
			if (!isLogged) {
				printStartingMenu();
				switch (scanner.nextInt()) {
					// create account
					case 1:
						createAccount();
						break;
					// log in
					case 2:
						System.out.println("Enter your card number:");
						String number = scanner.next();
						System.out.println("Enter your PIN:");
						String pin = scanner.next();

						currentCard = dataBase.getCard(number, pin);
						if (currentCard != null) {
							System.out.println("You have successfully logged in!");
							isLogged = true;
						} else {
							System.out.println("Wrong card number or PIN!");
						}
						break;
					// exit
					default:
						System.out.println("\nBye!");
						return;
				}
			} else {
				printCardMenu();
				switch (scanner.nextInt()) {
					// balance
					case 1:
						System.out.println("Balance: " + currentCard.getBalance());
						break;
					// add income
					case 2:
						System.out.println("Enter income:");
						currentCard.addIncome(scanner.nextInt());
						break;
					// do transfer
					case 3:
						currentCard.transfer(scanner);
						break;
					// close account
					case 4:
						currentCard.closeAcc();
						break;
					// log out
					case 5:
						System.out.println("You have successfully logged out!");
						isLogged = false;
						break;
					// exit
					default:
						System.out.println("\nBye!");
						return;
				}
			}
		}
	}

	private void printCardMenu() {
		System.out.println("1. Balance\n" +
		                   "2. Add income\n" +
		                   "3. Do transfer\n" +
		                   "4. Close account\n" +
		                   "5. Log out\n" +
		                   "0. Exit");
	}

	/**
	 * Creates card
	 */
	private void createAccount() {
		Card card = new Card(dataBase);
		card.addToDataBase(dataBase);
		System.out.printf("Your card has been created\n" +
		                  "Your card number:\n" +
		                  "%s\n" +
		                  "Your card PIN:\n" +
		                  "%s\n",
		                  card.getNumber(), card.getPin());
	}

	private void printStartingMenu() {
		System.out.println("1. Create an account\n" +
		                   "2. Log into account\n" +
		                   "0. Exit");
	}
}

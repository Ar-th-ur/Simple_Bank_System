package banking.code;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Card {
	private final Random       random = new Random();
	private final BankDataBase dataBase;
	private       String       number;
	private       String       pin;
	private       int         balance;

	/**
	 * Creates a card and then adds it to the database
	 * @param dataBase the database to which the card will be added
	 */
	public Card(BankDataBase dataBase) {
		this.balance = 0;
		this.dataBase = dataBase;

		do {
			number = createCardNumber("400000");
			pin = createPIN();
		} while (dataBase.contains(number));
		addToDataBase(dataBase);
	}

	/**
	 * Creates a card with the specified number and pin
	 * @param number card number
	 * @param pin    card pin
	 */
	public Card(String number, String pin, int balance, BankDataBase dataBase) {
		if (isLuhn(number)) {
			this.number = number;
			this.pin = pin;
			this.balance = balance;
			this.dataBase = dataBase;
		} else {
			throw new RuntimeException("This number does`nt passes Luhn algorithm");
		}
	}

	/**
	 * Transfers income to the another person
	 */
	public void transfer(Scanner scanner) {
		System.out.println("Transfer");
		System.out.println("Enter card number:");
		String anotherCardNumber = scanner.next();

		if (anotherCardNumber.equals(number)) {
			System.out.println("You can't transfer money to the same account!");
			return;
		}
		if (!Card.isLuhn(anotherCardNumber)) {
			System.out.println("Probably you made a mistake in the card number.\n Please try again!");
			return;
		}
		if (!dataBase.contains(anotherCardNumber)) {
			System.out.println("Such a card does not exist.");
			return;
		}

		System.out.println("Enter how much money you want to transfer:");
		int toTransfer = scanner.nextInt();

		if (toTransfer > balance) {
			System.out.println("Not enough money!");
			return;
		}
		balance -= toTransfer;

		dataBase.updateCardBalance(anotherCardNumber, toTransfer);
		dataBase.updateCardBalance(number, -toTransfer);
	}

	/**
	 * Increases money
	 */
	public void addIncome(int value) {
		balance += value;
		dataBase.updateCardBalance(this.number, value);
		System.out.println("Income was added!");
	}

	/**
	 * Checks whether the number passes the luhn algorithm
	 * @param number number to check
	 * @return true if passes the luhn algorithm, false otherwise
	 */
	public static boolean isLuhn(String number) {
		int sum = 0;
		for (int i = 0; i < number.length() - 1; i++) {
			int num = Character.getNumericValue(number.charAt(i));
			if (i % 2 == 0) {
				num *= 2;
				if (num > 9) {
					num -= 9;
				}
			}
			sum += num;
		}

		return (sum + Character.getNumericValue(number.charAt(number.length() - 1))) % 10 == 0;
	}

	/**
	 * Adds card to the specified database
	 * @param dataBase database to which the card will be added
	 */
	public void addToDataBase(BankDataBase dataBase) {
		dataBase.add(this);
	}

	/**
	 * Removes card from the database
	 */
	public void closeAcc() {
		dataBase.remove(this.number);
	}

	public String getNumber() {
		return number;
	}

	public String getPin() {
		return pin;
	}

	public int getBalance() {
		return balance;
	}

	/**
	 * Randomly creates a card number with bin
	 * @param bin Bank Identification Number
	 * @return randomly created card number
	 */
	private String createCardNumber(String bin) {
		StringBuilder number = new StringBuilder(bin);

		int sum = Arrays.stream(bin.split("")).mapToInt(i -> Integer.parseInt(i) * 2).sum();
		for (int i = 0; i < 9; i++) {
			int num = random.nextInt(10);
			number.append(num);
			if (i % 2 == 0) {
				num *= 2;
				if (num > 9) {
					num -= 9;
				}
			}
			sum += num;
		}

		number.append((sum * 10 - sum % 10) % 10);
		return number.toString();
	}

	/**
	 * Randomly creates the PIN code
	 * @return PIN code
	 */
	private String createPIN() {
		return IntStream.range(0, 4)
			.mapToObj(i -> random.nextInt(10))
			.map(String::valueOf)
			.collect(Collectors.joining());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Card card = (Card) o;
		return number.equals(card.number) && pin.equals(card.pin);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number, pin);
	}
}

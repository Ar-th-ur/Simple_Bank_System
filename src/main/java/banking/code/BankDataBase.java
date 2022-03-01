package banking.code;

import javax.sql.DataSource;
import java.sql.*;

public class BankDataBase implements QueriesSource {
	private static BankDataBase Instance;
	private final  DataSource   dataSource;

	private BankDataBase(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static BankDataBase getInstance(DataSource dataSource) {
		if (Instance == null) {
			Instance = new BankDataBase(dataSource);
		}
		return Instance;
	}

	/**
	 * Deletes and then creates table
	 */
	public void init() {
		try (final Connection conn = dataSource.getConnection();
		     final Statement statement = conn.createStatement()) {
			statement.executeUpdate(CREATE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the card to the database
	 * @param card card that need to add to the database
	 */
	public void add(Card card) {
		try (final Connection conn = dataSource.getConnection();
		     final PreparedStatement statement = conn.prepareStatement(INSERT)) {
			statement.setString(1, card.getNumber());
			statement.setString(2, card.getPin());
			statement.setInt(3, card.getBalance());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks does the card exists
	 * @return true if card is exists in the database
	 */
	public boolean contains(String number) {
		try (final Connection conn = dataSource.getConnection();
		     final PreparedStatement statement = conn.prepareStatement(GET_BY_NUMBER)) {
			statement.setString(1, number);

			try (ResultSet set = statement.executeQuery()) {
				return set.next();
			}
		} catch (SQLException ignored) {}
		return false;
	}

	/**
	 * Removes specified card from database
	 * @param number card number that needs to remove from database
	 */
	public void remove(String number) {
		try (final Connection conn = dataSource.getConnection();
		     final PreparedStatement statement = conn.prepareStatement(DELETE_BY_NUMBER)) {
			statement.setString(1, number);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates card pin of specified card number
	 * @param number card number
	 * @param balance card balance which needs to be updated
	 */
	public void updateCardBalance(String number, int balance) {
		try (final Connection conn = dataSource.getConnection();
			 final PreparedStatement statement = conn.prepareStatement(ADD_TO_BALANCE_BY_NUMBER)) {
			statement.setInt(1, balance);
			statement.setString(2, number);

			System.out.println(statement.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param pin card pin
	 * @param number card number
	 * @return a card from the database, if it exists with the specified number and PIN, otherwise null
	 */
	public Card getCard(String number, String pin) {
		try (final Connection conn = dataSource.getConnection();
		     final PreparedStatement statement = conn.prepareStatement(GET_BY_NUMBER_AND_PIN)) {
			statement.setString(1, number);
			statement.setString(2, pin);

			try (final ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					int balance = resultSet.getInt("balance");
					return new Card(number, pin, balance, this);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

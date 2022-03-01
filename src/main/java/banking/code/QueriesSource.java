package banking.code;

public interface QueriesSource {
	String TABLE_NAME   = "card";
	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
	                      + "id INTEGER, "
	                      + "number TEXT, "
	                      + "pin TEXT, "
	                      + "balance INTEGER DEFAULT 0 "
	                      + ")";
	String INSERT                   = "INSERT INTO " + TABLE_NAME + " (number, pin, balance) VALUES (?, ?, ?)";
	String DELETE_BY_NUMBER         = "DELETE FROM " + TABLE_NAME + " WHERE number = ?";
	String GET_BY_NUMBER_AND_PIN    = "SELECT * FROM " + TABLE_NAME + " WHERE number = ? AND pin = ?";
	String GET_BY_NUMBER            = "SELECT * FROM " + TABLE_NAME + " WHERE number = ?";
	String ADD_TO_BALANCE_BY_NUMBER = "UPDATE " + TABLE_NAME + " SET balance = balance + ?" + " WHERE number = ?";
}

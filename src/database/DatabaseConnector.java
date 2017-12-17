package database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DatabaseConnector {
//	private static Connection connection = null;
	private Connection connection = null;
	
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		if(connection == null || connection.isClosed())
			connection = getDatabseConnection();

		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	public Connection getDatabseConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		// old way to connect databse
		/*System.out.println("DatabaseConnector");
//		String connectionURL = "jdbc:mysql://localhost/shopebazar?user=root&password=''";
		String connectionURL = "jdbc:mysql://192.168.0.209/shopebazar";
		System.out.println("DatabaseConnector");
//		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("DatabaseConnector");
//		connection = DriverManager.getConnection(connectionURL);
		connection = DriverManager.getConnection(connectionURL, "msp", "msp");
		System.out.println("DatabaseConnector");*/
		
		// new way to connect database port is optional if you run MySQL on 3306 or set port value
		MysqlDataSource dataSource = new MysqlDataSource();
		
		// common port number
//		dataSource.setPort(3306);
		
		// msp local
		/*dataSource.setServerName("192.168.0.209");
		dataSource.setDatabaseName("shopebazar");
		dataSource.setUser("msp");
		dataSource.setPassword("msp");*/
		
		// mohit home local
		/*dataSource.setServerName("localhost");
		dataSource.setDatabaseName("shopebazar");
		dataSource.setUser("msp");
		dataSource.setPassword("msp");*/
		
		// live server
		dataSource.setServerName("shopebazar.cgmic1fxtf78.us-east-1.rds.amazonaws.com");
		dataSource.setDatabaseName("shopenazar");
		dataSource.setUser("shOpEbazar_2017");
		dataSource.setPassword("shOpEbezar2017");
		
		// get connection
		connection = dataSource.getConnection();
//		System.out.println("DatabaseConnector");
		
		return connection;
	}
}

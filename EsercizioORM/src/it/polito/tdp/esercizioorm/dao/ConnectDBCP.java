package it.polito.tdp.esercizioorm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.DataSources;

public class ConnectDBCP {

	private static final String jdbcURL = "jdbc:mysql://localhost/iscritticorsi?user=root";
	private static DataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			// Crea il DataSource
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1); //chiudo il programma perchè sarebbe errore irrecuperabile
			}
		}
		// Non parliamo più con il DriverJDBC, è la nostra libreria che ci parla
		try {
			Connection c = ds.getConnection();
			return c;
			
			
		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}
	}

}

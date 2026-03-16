package com.apps.quantitymeasurementapp.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.apps.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurementapp.util.ConnectionPool;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository{
	// Logger for logging database operations and errors
	private static final Logger logger = Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());

	// Singleton instance of the repository
	private static QuantityMeasurementDatabaseRepository instance;

	private static final String INSERT_QUERY = "INSERT INTO quantity_measurement_entity "
			+ "(this_value, this_unit, this_measurement_type, that_value, that_unit, "
			+ "that_measurement_type, operation, result_value, result_unit, "
			+ "result_measurement_type, result_string, is_error, error_message, " + "created_at, updated_at) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW( ), NOW( ) )";

	private static final String SELECT_ALL_QUERY = "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

	private static final String SELECT_BY_OPERATION = "SELECT * FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC";

	private static final String SELECT_BY_MEASUREMENT_TYPE = "SELECT * FROM quantity_measurement_entity "
			+ "WHERE this_measurement_type = ? ORDER BY created_at DESC";

	private static final String DELETE_ALL_QUERY = "DELETE FROM quantity_measurement_entity";

	private static final String COUNT_QUERY = "SELECT COUNT(*) FROM quantity_measurement_entity";

	private ConnectionPool connectionPool;
	
	private QuantityMeasurementDatabaseRepository() {
		
	}
	
	private void initializeDatabase() {

	    String createTableQuery =
	            "CREATE TABLE IF NOT EXISTS quantity_measurement_entity ("
	                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
	                    + "this_value DOUBLE,"
	                    + "this_unit VARCHAR(50),"
	                    + "this_measurement_type VARCHAR(50),"
	                    + "that_value DOUBLE,"
	                    + "that_unit VARCHAR(50),"
	                    + "that_measurement_type VARCHAR(50),"
	                    + "operation VARCHAR(50),"
	                    + "result_value DOUBLE,"
	                    + "result_unit VARCHAR(50),"
	                    + "result_measurement_type VARCHAR(50),"
	                    + "result_string VARCHAR(255),"
	                    + "is_error BOOLEAN,"
	                    + "error_message VARCHAR(255),"
	                    + "created_at TIMESTAMP,"
	                    + "updated_at TIMESTAMP"
	                    + ")";

	    Connection conn = null;
	    Statement stmt = null;

	    try {

	        conn = connectionPool.getConnection();
	        stmt = conn.createStatement();
	        stmt.execute(createTableQuery);

	        logger.info("Database table initialized successfully");

	    } catch (Exception e) {

	        logger.severe("Error initializing database: " + e.getMessage());

	    } finally {

	        closeResources(stmt, conn);
	    }
	}

	public static synchronized QuantityMeasurementDatabaseRepository getInstance() {

	    if (instance == null) {
	        instance = new QuantityMeasurementDatabaseRepository();
	        try {
				instance.connectionPool = ConnectionPool.getInstance();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        instance.initializeDatabase();
	    }

	    return instance;
	}
	
	@Override
	public void save(QuantityMeasurementEntity entity) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;

	    try {

	        conn = connectionPool.getConnection();
	        pstmt = conn.prepareStatement(INSERT_QUERY);

	        pstmt.setDouble(1, entity.thisValue);
	        pstmt.setString(2, entity.thisUnit);
	        pstmt.setString(3, entity.thisMeasurementType);

	        pstmt.setDouble(4, entity.thatValue);
	        pstmt.setString(5, entity.thatUnit);
	        pstmt.setString(6, entity.thatMeasurementType);

	        pstmt.setString(7, entity.operation);

	        pstmt.setDouble(8, entity.resultValue);
	        pstmt.setString(9, entity.resultUnit);
	        pstmt.setString(10, entity.resultMeasurementType);

	        pstmt.setString(11, entity.resultString);
	        pstmt.setBoolean(12, entity.isError);
	        pstmt.setString(13, entity.errorMessage);

	        pstmt.executeUpdate();

	        logger.info("Measurement saved successfully");

	    } catch (SQLException e) {

	        logger.severe("Error saving measurement: " + e.getMessage());

	    } finally {

	        closeResources(pstmt, conn);
	    }
	}
	
	/*
	* Retrieves all QuantityMeasurementEntity instances from the database. This method executes a
	*/
	
	@Override
	public List<QuantityMeasurementEntity> getAllMeasurements(){
		Connection con = null;
		PreparedStatement statement = null;
		
		List<QuantityMeasurementEntity> result = new ArrayList<>();
		
		try {
			con = connectionPool.getConnection();
			statement = con.prepareStatement(SELECT_ALL_QUERY);
			
			ResultSet res = statement.executeQuery();
			
			while(res.next()) {
				result.add(mapResultSetToEntity(res));
			}
		}
		catch(SQLException e) {
			logger.severe("Error saving measurement: " + e.getMessage());
		}
		finally {
	        closeResources(statement, con);
	    }
		return result;
	}
	
	/**
	* Get measurements by operation type. This method retrieves all quantity measurement
	*/
	public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation){
		return null;
	}
	
	/**
	* Get measurements by measurement type (Length, Weight, Volume, Temperature).
	*/
	
	public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType){
		return null;
	}

	/**
	* Get count of all measurements. This method executes a SQL query to count the
	*/
	
	public int getTotalCount() {

	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {

	        conn = connectionPool.getConnection();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(COUNT_QUERY);

	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (SQLException e) {

	        logger.severe("Error counting measurements: " + e.getMessage());

	    } finally {

	        closeResources(rs, stmt, conn);
	    }

	    return 0;
	}
	
	/**
	* Delete all measurements (useful for testing). This method executes a SQL query
	* 
	*/
	public void deleteAll() {

	    Connection conn = null;
	    Statement stmt = null;

	    try {

	        conn = connectionPool.getConnection();
	        stmt = conn.createStatement();
	        stmt.executeUpdate(DELETE_ALL_QUERY);

	        logger.info("All measurements deleted");

	    } catch (SQLException e) {

	        logger.severe("Error deleting measurements: " + e.getMessage());

	    } finally {

	        closeResources(stmt, conn);
	    }
	}
	
	public String getPoolStatistics() {

	    return "Available Connections: "
	            + connectionPool.getAvailableConnectionCount()
	            + ", Used Connections: "
	            + connectionPool.getUsedConnectionCount()
	            + ", Total: "
	            + connectionPool.getTotalConnectionCount();
	}

	/**
	* Map ResultSet row to QuantityMeasurementEntity
	*/
	private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) {

	    try {
	        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(rs.getDouble("this_value"), rs.getString("this_unit"), rs.getString("this_measurement_type"), rs.getDouble("that_value"), rs.getString("that_unit"), rs.getString("that_measurement_type"), rs.getString("operation"), rs.getDouble("result_value"), rs.getString("result_unit"), rs.getString("result_measurement_type"), rs.getString("result_string"), rs.getBoolean("is_error"), rs.getString("error_message"));
	        return entity;

	    } catch (SQLException e) {
	        logger.severe("Error mapping result set: " + e.getMessage());
	        return null;
	    }
	}

	/**
	* Release connection back to pool
	*/
	private void closeResources(ResultSet rs, Statement stmt, Connection conn) {

	    try {

	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();

	        if (conn != null) connectionPool.releaseConnection(conn);

	    } catch (Exception e) {

	        logger.warning("Error closing resources: " + e.getMessage());
	    }
	}

	private void closeResources(Statement stmt, Connection conn) {

	    try {

	        if (stmt != null) stmt.close();

	        if (conn != null) connectionPool.releaseConnection(conn);

	    } catch (Exception e) {

	        logger.warning("Error closing resources: " + e.getMessage());
	    }
	}
}
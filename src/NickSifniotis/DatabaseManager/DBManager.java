package NickSifniotis.DatabaseManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;


/**
 * <p>
 *     A simple class that provides a clean, easy interface to the SQLite library.
 * </p>
 *
 * <p>
 *     Instances of this class are associated with a physical database file. Applications that use
 *     multiple databases will need to maintain multiple instances of DBManager.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @since 31/08/2015
 * @version 3.0.0
 */
public class DBManager
{
    /** The path to the database file. **/
    private String database_path;

    /** The database filename. **/
    private String database_name;

    /** The defalt database filename. **/
    private final String default_name = "database.db";

    /** The default path to the database. **/
    private final String default_path = "database/";


    /**
     * <p>
     *     Constructor that connects to the default database location.
     * </p>
     */
    public DBManager()
    {
        __set_db_location(default_path, default_name);
    }


    /**
     * <p>
     *     Constructor that creates an instance connecting to a user-specified database.
     * </p>
     *
     * <p>
     *     If the path to the database does not exist, the constructor will attempt to create it.
     * </p>
     *
     * @param database_path The path where the database file is found.
     * @param database_name The name of the database file.
     */
    public DBManager(String database_path, String database_name)
    {
        if (database_name.equals(""))
            database_name = default_name;

        if (database_path.equals(""))
            database_path = default_path;

        if (database_path.charAt(database_path.length() - 1) != '/')
            database_path += "/";

        __set_db_location(database_path, database_name);
    }


    /**
     * <p>
     *     Sets the location and name of the database file.
     * </p>
     *
     * <p>
     *     If the path does not exist, this method will attempt to create it.
     * </p>
     *
     * @param new_path The path where the database file will be located, without the trailing '/'.
     * @param new_name The name of the database file.
     */
    private void __set_db_location (String new_path, String new_name)
    {
        Path path = Paths.get(new_path);
        if (!Files.isDirectory(path))
        {
            try
            {
                Files.createDirectory(path);
            }
            catch (Exception e)
            {
                System.out.println("Error creating path for database file: " + new_path);
                System.out.println("Fatal error: Terminating.");
                System.exit(1);
            }
        }

        database_name = new_name;
        database_path = new_path;
    }


    /**
     * <p>
     *     Executes an SQL query on the database.
     * </p>
     *
     * <p>
     *     The SQL query is passed to this method through the parameter <i>query</i>.
     *     This method executes the query blindly; no safety or validity checks are performed at all.
     *     It would be a good idea to write external code to verify the correctness of the query before
     *     invoking this method.
     * </p>
     *
     * @param query The SQL query to execute
     * @throws SQLException This class handles no exceptions. If the query you pass to this method is dodgy,
     * you wear the consequences.
     */
    public void Execute (String query) throws SQLException
    {
        Connection connection;
        Statement statement = null;

        try
        {
            connection = Connect();
            statement = connection.createStatement();
            statement.execute(query);
        }
        finally
        {
            closeQuietly(statement);
        }
    }


    /**
     * <p>
     *     Inserts a row into the database and returns the primary key of the newly created row.
     * </p>
     *
     * <p>
     *     No checks are performed on the SQL query that is passed into this method. You could conceivably
     *     throw in a SELECT or a DELETE instead. This method is not guaranteed to behave correctly if any
     *     query other than an INSERT is executed.
     * </p>
     *
     * @param query The SQL [INSERT] query to execute.
     * @return The primary key of the newly created row.
     * @throws SQLException This class does not handle faulty queries.
     */
    public int InsertAndReturnKey(String query) throws SQLException
    {
        int res = -1;
        Connection connection;
        PreparedStatement statement;
        ResultSet generated_keys = null;

        try
        {
            connection = Connect();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int affected_rows = statement.executeUpdate();

            if (affected_rows != 1)
                throw new SQLException("DBManager.InsertAndReturnKey - Insert into database failed. Affected rows: " + affected_rows + ": " + query);

            generated_keys = statement.getGeneratedKeys();

            if (generated_keys.next())
                res = generated_keys.getInt(1);
            else
                throw new SQLException("DBManager.InsertAndReturnKey - Creating row failed, no ID obtained.");
        }
        finally
        {
            closeQuietly(generated_keys);
        }

        return res;
    }


    /**
     * <p>
     *     Executes an SQL SELECT query and returns the results in a ResultSet.
     * </p>
     *
     * @param query The SELECT query to execute.
     * @param connection The object that represents a connection to the database. It is returned by DBManager.Connect()
     * @return The results of the query, stored in a java.sql.ResultSet object.
     * @throws SQLException This class does not handle exceptions at all. Don't feed this method INSERTs or DELETEs please.
     */
    public ResultSet ExecuteQuery (String query, Connection connection) throws SQLException
    {
        ResultSet results = null;

        if (connection != null)
            results = connection.createStatement().executeQuery(query);

        return results;
    }


    /**
     * <p>
     *      Get a connection object from the database that can be used to process SQL and so forth.
     * </p>
     *
     * @throws SQLException Exceptions aren't handled by this class.
     * @return A connection object that is connected to the database. Remember to close when finished!
     */
    public Connection Connect() throws SQLException
    {
        Connection connection = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + database_path + database_name);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * <p>
     *     Disconnects <i>connection</i> from the database.
     * </p>
     *
     * @param connection The connection to disconnect.
     */
    public void Disconnect (Connection connection)
    {
        closeQuietly(connection);
    }


    /**
     * <p>
     *     Disconnects <i>results</i> from the database.
     * </p>
     *
     * @param results The ResultSet to disconnect.
     */
    public void Disconnect (ResultSet results)
    {
        closeQuietly(results);
    }


    /**
     * <p>
     *     Quietly closes an instance of a ResultSet. The Statement and Connection that are
     *     associated with the ResultSet are also closed.
     * </p>
     *
     * @param result The instance to close.
     */
    private void closeQuietly (ResultSet result)
    {
        if (result == null)
            return;

        try
        {
            Statement statement = result.getStatement();
            result.close();
            closeQuietly(statement);
        }
        catch (Exception e)
        {
            // ignore it
        }
    }


    /**
     * <p>
     *     Quietly closes a Connection instance.
     * </p>
     *
     * @param connection The instance to close.
     */
    private void closeQuietly (Connection connection)
    {
        if (connection == null)
            return;

        try
        {
            connection.close();
        }
        catch (Exception e)
        {
            // ignore it
        }
    }


    /**
     * <p>
     *     Quietly closes a Statement instance, including its associated Connection.
     * </p>
     *
     * @param statement The instance to close.
     */
    private void closeQuietly (Statement statement)
    {
        if (statement == null)
            return;

        try
        {
            Connection connection = statement.getConnection();
            statement.close();
            closeQuietly(connection);
        }
        catch (Exception e)
        {
            // ignore it
        }
    }
}

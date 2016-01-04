package NickSifniotis.DatabaseManager;

import java.sql.*;


/**
 * <p>
 *     Static class that provides a clean, easy interface to the SQLite library.
 * </p>
 *
 * <p>
 *     @TODO it might be elite to change this from a static class to the ordinary sort,
 *     so as to allow the use of multiple databases in the same application.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @since 31/08/2015
 * @version 2.0.0
 */
class DBManager
{
    /** The path to the database file. **/
    private static String database_path = "database";

    /** The database filename. **/
    private static String database_name = "database.db";


    /**
     * <p>
     *     Sets the location and name of the database file.
     * </p>
     *
     * <p>
     *     A call to this method is not <i>required</i>; DBManager is happy to use the default values
     *     "database/database.db".
     * </p>
     *
     * <p>
     *     However, if you do intend to use a database in another location, be sure to invoke this method
     *     <i>before</i> attempting to perform any operations on the database.
     * </p>
     *
     * @param new_path The path where the database file will be located, without the trailing '/'.
     * @param new_name The name of the database file.
     */
    public static void SetDBLocation (String new_path, String new_name)
    {
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
     *     It would be a good idea to write external code to verify the correctness of the query.
     * </p>
     *
     * @param query The SQL query to execute
     * @throws SQLException This static class handles no exceptions. If the query you pass to this method is dodgy,
     * you wear the consequences.
     */
    public static void Execute (String query) throws SQLException
    {
        Connection connection = null;
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
            closeQuietly(connection);
        }
    }


    /**
     * Executes the SQL and return the key of the affected row
     * Obviously .. the query can only be one INSERT only.
     *
     * @param query the query to execute
     * @return the pri key of the newly created row
     * @throws SQLException - this class does not handle faulty queries
     */
    public static int ExecuteReturnKey (String query) throws SQLException
    {
        int res = -1;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generated_keys = null;

        try
        {
            connection = Connect();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int affected_rows = statement.executeUpdate();

            if (affected_rows != 1)
                throw new SQLException("DBManager.ExecuteReturnKey - Insert into database failed. Affected rows: " + affected_rows + ": " + query);

            generated_keys = statement.getGeneratedKeys();

            if (generated_keys.next())
                res = generated_keys.getInt(1);
            else
                throw new SQLException("DBManager.ExecuteReturnKey - Creating user failed, no ID obtained.");
        }
        finally
        {
            closeQuietly(generated_keys);
            closeQuietly(statement);
            closeQuietly(connection);
        }

        return res;
    }


    /**
     * <p>
     *     Executes an SQL query and returns the results in a resultSet.
     * </p>
     *
     * @param query The SELECT query to execute
     * @param connection The object that represents a connection to the database. It is returned by DBManager.Connect()
     * @return The results of the query, stored in a java.sql.ResultSet object.
     * @throws SQLException This class does not handle exceptions at all.
     */
    public static ResultSet ExecuteQuery (String query, Connection connection) throws SQLException
    {
        ResultSet results = null;

        if (connection != null)
            results = connection.createStatement().executeQuery(query);

        return results;
    }


    /**
     * <p>
     *      Connects to the database, and
     *      returns a connection object that can be used to process SQL and so forth.
     * </p>
     *
     * @throws SQLException - SQL Exceptions aren't handled by this class.
     * @return A connection object that is connected to the database. Remember to close when finished!
     */
    public static Connection Connect() throws SQLException
    {
        Connection connection = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + database_path + "/" + database_name);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * Disconnects the given connection from the database.
     *
     * @param connection The connection to disconnect
     */
    public static void Disconnect (Connection connection)
    {
        closeQuietly(connection);
    }


    /**
     * Disconnect the ResultSet from the database.
     *
     * @param results The ResultSet to disconnect.
     */
    public static void Disconnect (ResultSet results)
    {
        closeQuietly(results);
    }


    /**
     * Sssh!
     *
     * Utility functions to close database connections without a fuss.
     *
     * @param res - the database entity to close
     */
    private static void closeQuietly (ResultSet res)
    {
        if (res == null)
            return;

        try
        {
            res.close();
        }
        catch (Exception e)
        {
            // ignore it
        }
    }

    private static void closeQuietly (Connection res)
    {
        if (res == null)
            return;

        try
        {
            res.close();
        }
        catch (Exception e)
        {
            // ignore it
        }
    }

    private static void closeQuietly (Statement res)
    {
        if (res == null)
            return;

        try
        {
            res.close();
        }
        catch (Exception e)
        {
            // ignore it
        }
    }
}

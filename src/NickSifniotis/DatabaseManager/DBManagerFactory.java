package NickSifniotis.DatabaseManager;

/**
 * <p>
 *     Class that manufactures DBManager instances.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @version 1.1.0
 * @since 04/01/2016
 */
public class DBManagerFactory
{
    /** The database filename. **/
    public String database_name;

    /** The path to the database file. **/
    public String database_path;


    public DBManagerFactory()
    {
        database_name = "";
        database_path = "";
    }


    /**
     * <p>
     *     Sets the database file that this DBManager connects to.
     * </p>
     *
     * @param name The database filename.
     * @return This DBManagerFactory instance, for further construction.
     */
    public DBManagerFactory Name(String name)
    {
        this.database_name = name;
        return this;
    }


    /**
     * <p>
     *     Sets the path to the database file that this DBManager connects to.
     * </p>
     *
     * @param path The path to the database.
     * @return This DBManagerFactory instance, for further construction.
     */
    public DBManagerFactory Path(String path)
    {
        this.database_path = path;
        return this;
    }


    /**
     * <p>
     *     Constructs and returns the DBManager instance.
     * </p>
     * @return A DBManager instance.
     */
    public DBManager build()
    {
        return new DBManager(database_path, database_name);
    }


    /**
     * <p>
     *     Create a new instance of a DBManagerFactory object.
     * </p>
     *
     *
     * @return An instance of a DBManagerFactory that will be used to construct DBManager class instances.
     */
    public static DBManagerFactory NewDBManager()
    {
        return new DBManagerFactory();
    }
}

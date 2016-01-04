package NickSifniotis.DatabaseManager;

/**
 * <p>
 *     Static class that manufactures DBManager instances.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @version 1.0.0
 * @since 04/01/2016
 */
public class DBManagerFactory
{
    private static String database_name;
    private static String database_path;

    public void NewDBManager()
    {
        database_name = "";
        database_path = "";
    }


    public void Name(String name)
    {
        database_name = name;
    }


    public void Path(String path)
    {
        database_path = path;
    }


    public static DBManager build()
    {
        return new DBManager(database_path, database_name);
    }
}

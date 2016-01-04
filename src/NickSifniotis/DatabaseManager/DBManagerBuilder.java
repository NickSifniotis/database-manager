package NickSifniotis.DatabaseManager;

/**
 * <p>
 *     Builder class for creating DBManager instances.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @version 1.0.0
 * @since 04/01/2016
 */
public class DBManagerBuilder
{
    public String database_name;
    public String database_path;

    public DBManagerBuilder()
    {
        database_name = "";
        database_path = "";
    }

    public DBManagerBuilder Name(String name)
    {
        this.database_name = name;
        return this;
    }

    public DBManagerBuilder Path(String path)
    {
        this.database_path = path;
        return this;
    }

    public DBManager build()
    {
        return new DBManager(database_path, database_name);
    }
}

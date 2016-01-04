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
    /**
     * <p>
     *     Create a new instance of a DBManager object.
     * </p>
     * 
     * <p>
     *     This factory method uses an intermediate data structure, <to be finished>
     * </p>
     * @TODO: DBManagerBuilder and DBMAnagerFactory can be merged into the same class, because one class
     * can have both static and nonstatic methods.
     * @return An instance of something (TBA) that will be used to construct DBManager class instances.
     */
    public static DBManagerBuilder NewDBManager()
    {
        return new DBManagerBuilder();
    }
}

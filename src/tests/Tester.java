package tests;

import NickSifniotis.DatabaseManager.DBManager;
import NickSifniotis.DatabaseManager.DBManagerFactory;

/**
 * <p>
 *     Testing module for the DatabaseManager package.
 * </p>
 *
 * @author Nick Sifniotis u5809912
 * @since 04/01.2016
 * @version 1.0.0
 */
public class Tester
{
    public static void main(String[] args)
    {
        DBManager default_database = DBManagerFactory.NewDBManager()
                                            .build();

        try
        {
            default_database.Execute("DROP TABLE IF EXISTS t");
            default_database.Execute("CREATE TABLE t(x INTEGER PRIMARY KEY ASC, y, z); ");
            default_database.Execute("INSERT INTO t (y, x) VALUES ('hello', 2)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

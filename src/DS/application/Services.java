package DS.application;

import DS.persistence.DataAccess;
import DS.persistence.DataAccessObject;


public class Services {
	private static DataAccess localDataAccessService = null;
	
	public static DataAccess createLocalDataAccess(String dbName){
		if(localDataAccessService == null){
			localDataAccessService = new DataAccessObject(dbName);
			localDataAccessService.openLocal(dbName);
		}
		return localDataAccessService;
	}
	
    public static DataAccess getLocalDataAccess()
    {
        if (localDataAccessService == null)
        {
            System.out.println("Connection to data access has not been established");
            System.exit(1);
        }
        return localDataAccessService;
    }

    public static void closeDataAccess()
    {
        if (localDataAccessService != null)
        {
            localDataAccessService.close();
        }
        localDataAccessService = null;
    }
}

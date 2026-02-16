package Service;

import Model.Request;
import Model.Response;
import Model.UserData;
import dataaccess.*;

import static server.Handler.*;

public class ClearService {



    public Response.ClearResponse Clear(Request.ClearRequest request) throws DataAccessException {


        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        return new Response.ClearResponse();

    }
}

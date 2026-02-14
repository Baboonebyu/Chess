package Service;

import Model.Request;
import Model.Response;
import Model.UserData;
import dataaccess.*;

import static server.Handler.authDAO;
import static server.Handler.userDAO;

public class ClearService {



    public Response.ClearResponse Clear(Request.ClearRequest request) throws DataAccessException {


        userDAO.clear();
        authDAO.clear();

        return new Response.ClearResponse();

    }
}

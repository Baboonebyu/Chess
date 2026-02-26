package service;

import model.Request;
import model.Response;
import dataaccess.*;

import static server.Handler.*;

public class ClearService {


    public Response.ClearResponse clear(Request.ClearRequest request) throws DataAccessException {


        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

        return new Response.ClearResponse();

    }
}

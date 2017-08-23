package com.oscar.olimpia.Utils;

/**
 * Created by Usuario on 22/08/2017.
 */

public class Constants {
    public interface codePhoto{
        int CAMERA_REQUEST = 1;
        int GALLERY_REQUEST = 2;
    }

    public interface extraActivity{
        String pojoUsuario = "pojoUsuario";

    }

    public interface successType{
        int DATA_LOAD = 101;
    }

    public interface errorType{
        int ERROR_DATA_LOAD = 201;
    }

    public interface succesMessage{
        String successLoadData = "Se cargo al información";
    }

    public interface errorMessage{
        String errorLoginCreateUser = "No se pudo cargar al infromación";
    }

    public interface typeResponseWebService{
        int TYPE_JS0N = 401;
        int TYPE_XML = 402;
        int RESPONSE_OK = 200;
        int RESPONSE_NOT_OK = 201;

    }
}

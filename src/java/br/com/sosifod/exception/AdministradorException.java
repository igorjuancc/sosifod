package br.com.sosifod.exception;

public class AdministradorException extends Exception {

    public AdministradorException(String message) {
        super(message);
    }
    
    public AdministradorException(String message, Throwable cause){
        super(message, cause);
    }
            
}

package br.com.sosifod.exception;

public class OficialException extends Exception {

    public OficialException(String message) {
        super(message);
    }
    
    public OficialException(String message, Throwable cause){
        super(message, cause);
    }
            
}

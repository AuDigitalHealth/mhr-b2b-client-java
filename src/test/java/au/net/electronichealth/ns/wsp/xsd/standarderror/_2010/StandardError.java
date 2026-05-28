package au.net.electronichealth.ns.wsp.xsd.standarderror._2010;

public class StandardError {
    private StandardErrorCodeType errorCode;
    private String message;

    public void setErrorCode(StandardErrorCodeType code) { this.errorCode = code; }
    public StandardErrorCodeType getErrorCode() { return errorCode; }

    public void setMessage(String msg) { this.message = msg; }
    public String getMessage() { return message; }
}


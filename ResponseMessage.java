package student.utils;

/**
 * Created by cxr1205628673 on 2019/11/11.
 */
public class ResponseMessage <T>{
    private String message;
    private boolean success;
    private T body;

    public ResponseMessage(boolean success, T body, String message) {
        this.success = success;
        this.body = body;
        this.message = message;
    }
    public ResponseMessage(boolean success, T body) {
        this.success = success;
        this.body = body;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

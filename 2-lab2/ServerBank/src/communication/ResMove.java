//created by duknust
//find in https://github.com/Duknust
package communication;

public class ResMove extends Response {

    boolean status = false;

    public ResMove(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}

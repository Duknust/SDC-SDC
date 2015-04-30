//created by duknust
//find in https://github.com/Duknust
package bank;

import java.io.Serializable;

public class Operation implements Serializable {

    Type op;
    int amount;
    boolean status;

    public enum Type {

        MOVE, BALANCE, LEAVE
    }

    public Operation(boolean status) {
        this.op = null;
        this.amount = -1;
        this.status = status;
    }

    public Operation(Type op) {
        this.op = op;
        this.amount = -1;
    }

    public Operation(Type op, int amount) {
        this.op = op;
        this.amount = amount;
    }

    public Type getOperation() {
        return this.op;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setOperation(Type op) {
        this.op = op;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

package bank;

import java.io.Serializable;

public class Operation implements Serializable {

    int op;
    int amount;
    boolean status;

    public Operation(boolean status) {
        this.op = -1;
        this.amount = -1;
        this.status = status;
    }

    public Operation(int op) {
        this.op = op;
        this.amount = -1;
    }

    public Operation(int op, int amount) {
        this.op = op;
        this.amount = amount;
    }

    public int getOperation() {
        return this.op;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setOperation(int op) {
        this.op = op;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

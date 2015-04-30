//created by duknust
//find in https://github.com/Duknust
package communication;

public class OpMove extends Operation {

    int amount;

    public OpMove(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}

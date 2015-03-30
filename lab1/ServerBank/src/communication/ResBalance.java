//created by duknust
//find in https://github.com/Duknust
package communication;

public class ResBalance extends Response {

    int amount = -1;

    public ResBalance(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}

package conceptdrift;


import java.util.Objects;

public final class Transaction {
    private long accountId;
    private long eventTimestamp;
    private float amount;

    public Transaction() {
    }

    public Transaction(long accountId, long eventTimestamp, float amount) {
        this.accountId = accountId;
        this.eventTimestamp = eventTimestamp;
        this.amount = amount;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getEventTimestamp() {
        return this.eventTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.eventTimestamp = timestamp;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.accountId, this.eventTimestamp, this.amount});
    }

    public String toString() {
        return "Event{accountId=" + this.accountId + ", timestamp=" + this.eventTimestamp + ", amount=" + this.amount + '}';
    }



    public Transaction add(Transaction b) {
        System.out.println("Transaction amount" +b.getAmount());
        return b;
    }
}


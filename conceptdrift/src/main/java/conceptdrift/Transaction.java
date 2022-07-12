package conceptdrift;


import conceptdrift.utils.SimpleDateFormatter;

import java.util.Objects;

public final class Transaction {
    private long accountId;
    private long eventTimestamp;
    private long ingestionTimestamp;
    private double amount;

    public Transaction() {
    }

    public Transaction(long accountId, long ingestionTimestamp, long eventTimestamp, double amount) {
        this.accountId = accountId;
        this.eventTimestamp = eventTimestamp;
        this.ingestionTimestamp = ingestionTimestamp;
        this.amount = amount;
    }

    public double getLatency(){
        return (ingestionTimestamp - eventTimestamp)/1000.0;
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
    public long getIngestionTimestamp() {
        return this.ingestionTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.eventTimestamp = timestamp;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    public int hashCode() {
        return Objects.hash(new Object[]{this.accountId, this.eventTimestamp, this.amount});
    }

    public String toString() {
        return "Event{ingestionTime=" + SimpleDateFormatter.toDate(this.ingestionTimestamp) + ", eventTime=" + SimpleDateFormatter.toDate(this.eventTimestamp) + ", latency=" + this.getLatency() + '}';
    }



    public Transaction add(Transaction b) {
        System.out.println("Transaction amount" +b.getAmount());
        return b;
    }
}


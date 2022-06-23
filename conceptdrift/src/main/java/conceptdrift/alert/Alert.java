package conceptdrift.alert;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Objects;
import java.util.Date;

public final class Alert {
    private long id;
    private long dateTime;

    public Alert() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setDateTime(long dateTime){ this.dateTime = dateTime;}


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }

    public String toString() {
        return "Alert{id=" + this.id + ", date=" + this.dateTime + "}";
    }
}

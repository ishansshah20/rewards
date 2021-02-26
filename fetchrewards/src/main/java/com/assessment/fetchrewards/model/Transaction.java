package com.assessment.fetchrewards.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    private UUID tid;
    public UUID getTid() {
        return tid;
    }

    public void setTid(UUID tid) {
        this.tid = tid;
    }

    private String payer;

    private long points;

    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean isProcessed;

    private Date timestamp;

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}

/*@Entity
public class Transaction {

}*/

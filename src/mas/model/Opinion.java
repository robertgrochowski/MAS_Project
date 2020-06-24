package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class Opinion
{
    private long id;

    private LocalDateTime timeCreated;
    private int mark; //todo constraints
    private String comment;
    private Ticket ticket;

    public Opinion() {
    }

    private Opinion(Ticket ticket, LocalDateTime timeCreated, int mark, String comment) throws Exception {
        if(ticket == null) {
            throw new Exception("The whole (ticket) does not exists!");
        }
        this.timeCreated = timeCreated;
        this.mark = mark;
        this.comment = comment;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public int getMark() {
        return mark;
    }

    public String getComment() {
        return comment;
    }

    @OneToOne
    public Ticket getTicket() {
        return ticket;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

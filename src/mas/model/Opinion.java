package mas.model;

import mas.model.enums.TicketStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Opinion
{
    private long id;
    private static final int MAX_DAYS_TO_ADD_OPINION = 14;
    private static final Set<Integer> validMarks = new HashSet<>(Arrays.asList(1,2,3,4,5));

    private LocalDateTime timeCreated;
    private int mark;
    private String comment;
    private Ticket ticket;

    public Opinion() {
    }

    public Opinion(Ticket ticket, LocalDateTime timeCreated, int mark, String comment) throws Exception {
        if(ticket == null) {
            throw new Exception("The whole (ticket) does not exists!");
        }
        if(Duration.between(ticket.getDateCreated(), timeCreated).toDays() > MAX_DAYS_TO_ADD_OPINION) {
            throw new Exception("Opinion can not be added after "+MAX_DAYS_TO_ADD_OPINION+" days of creating the application!");
        }
        if(ticket.getStatus() != TicketStatus.FINISHED) {
            throw new Exception("Opinion can be added only in ticket's " + TicketStatus.FINISHED.toString() + " status");
        }

        setTimeCreated(timeCreated);
        setMark(mark);
        setComment(comment);
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

    @OneToOne(cascade = CascadeType.ALL)
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

    public void setMark(int mark) throws Exception {
        if(!validMarks.contains(mark)) {
            throw new Exception("Invalid mark: " + mark);
        }
        this.mark = mark;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

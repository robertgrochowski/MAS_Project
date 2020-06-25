package mas.model.enums;

/**
 * Implemented "Stan zgloszenia" enum from UML diagram.
 * Represents available statuses for Ticket class
 * @see mas.model.Ticket
 *
 * @since 1.0
 * @author Robert Grochowski
 */
public enum TicketStatus {
    SUBMITTED, ACCEPTED, CANCELED, IN_PROGRESS, WAITING_FOR_DELIVERER,
    IN_TRANSIT, COMPLETED, READY_TO_PICKUP, FINISHED
}

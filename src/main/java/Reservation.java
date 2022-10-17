import java.time.LocalDate;
import java.util.UUID;

public class Reservation {
    private UUID reservation_id;
    private UUID room_id;
    private LocalDate start_date;
    private LocalDate end_date;

    public Reservation(UUID reservation_id, UUID room_id, LocalDate start_date, LocalDate end_date) {
        this.reservation_id = reservation_id;
        this.room_id = room_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservation_id=" + reservation_id +
                ", room_id=" + room_id +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }

    public UUID getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(UUID reservation_id) {
        this.reservation_id = reservation_id;
    }

    public UUID getRoom_id() {
        return room_id;
    }

    public void setRoom_id(UUID room_id) {
        this.room_id = room_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
}

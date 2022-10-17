import java.math.BigDecimal;
import java.util.UUID;

public class Room {
    private UUID room_id;
    private UUID hotel_id;
    private Integer room_nb;
    private Integer capacity;
    private BigDecimal price;

    public Room(UUID room_id, UUID hotel_id, Integer room_nb, Integer capacity, BigDecimal price) {
        this.room_id = room_id;
        this.hotel_id = hotel_id;
        this.room_nb = room_nb;
        this.capacity = capacity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room_id=" + room_id +
                ", hotel_id=" + hotel_id +
                ", room_nb=" + room_nb +
                ", capacity=" + capacity +
                ", price=" + price +
                '}';
    }

    public UUID getRoom_id() {
        return room_id;
    }

    public void setRoom_id(UUID room_id) {
        this.room_id = room_id;
    }

    public UUID getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(UUID hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Integer getRoom_nb() {
        return room_nb;
    }

    public void setRoom_nb(Integer room_nb) {
        this.room_nb = room_nb;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

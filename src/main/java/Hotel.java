import java.util.UUID;

public class Hotel {
    private UUID hotel_id;
    private String name;
    private String phone;
    private String address;

    @Override
    public String toString() {
        return "Hotel{" +
                "hotel_id=" + hotel_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Hotel(UUID hotel_id, String name, String phone, String address) {
        this.hotel_id = hotel_id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(UUID hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.utils.UUIDs.random;

public class CassandraConnector {

    public static Cluster cluster;
    public static Session session;


    public static void main(String[] args) {
        Cluster.Builder b = Cluster.builder().addContactPoint("127.0.0.1");
        b.withPort(9042);
        cluster = b.build();
        session = cluster.connect();

        CassandraConnector connector = new CassandraConnector();
        session.execute("DROP KEYSPACE IF EXISTS hotel ");
        connector.createKeyspace("hotel ");
        Session hotelSession = cluster.connect("hotel ");
        connector.createTableHotelTable(hotelSession);
        connector.createTableRoomTable(hotelSession);
        connector.createTableReservationTable(hotelSession);
        UUID hotel1Id = random();
        connector.insertIntoHotel(new Hotel(hotel1Id, "'Pas-Mineda'", "'+37066635666'", "'Kaimo g, 24'"), hotelSession);
        UUID room1Id = random();
        UUID room2Id = random();
        UUID room3Id = random();
        connector.insertIntoRoom(new Room(room1Id, hotel1Id, 101, 4, BigDecimal.valueOf(120d)), hotelSession);
        connector.insertIntoRoom(new Room(room2Id, hotel1Id, 102, 4, BigDecimal.valueOf(120d)), hotelSession);
        connector.insertIntoRoom(new Room(room3Id, hotel1Id, 103, 2, BigDecimal.valueOf(90d)), hotelSession);
        connector.insertIntoReservation(new Reservation(random(), room1Id, LocalDate.now(), LocalDate.now()), hotelSession);

//        connector.printHotels(connector.selectAllHotels(hotelSession));
//        connector.printRooms(connector.selectAllRooms(hotelSession));
//        connector.printReservations(connector.selectAllReservations(hotelSession));
        connector.printRooms(connector.getRoomsByHotelIdAndRoomNb(hotelSession, hotel1Id, 4, BigDecimal.valueOf(150d)));

    }

    public void createKeyspace(String name) {
        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(name).append("WITH replication = {")
                        .append("'class':'").append("SimpleStrategy")
                        .append("','replication_factor':").append(2)
                        .append("};");
        String query = sb.toString();
        session.execute(query);
    }

    public void printHotels(List<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            System.out.println(hotel.toString());
        }
    }

    public void printReservations(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }

    public void printRooms(List<Room> rooms) {
        for (Room room : rooms) {
            System.out.println(room.toString());
        }
    }

    public List<Hotel> selectAllHotels(Session session) {
        StringBuilder sb =
                new StringBuilder("SELECT * FROM ").append("hotel");
        String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<Hotel> hotels = new ArrayList<Hotel>();
        rs.forEach(r -> {
            hotels.add(new Hotel(
                    r.getUUID("hotel_id"),
                    r.getString("name"),
                    r.getString("phone"),
                    r.getString("address")));
        });
        return hotels;
    }

    public List<Room> selectAllRooms(Session session) {
        StringBuilder sb =
                new StringBuilder("SELECT * FROM ").append("room");
        String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<Room> hotels = new ArrayList<Room>();
        rs.forEach(r -> {
            hotels.add(new Room(
                    r.getUUID("room_id"),
                    r.getUUID("hotel_id"),
                    r.getInt("room_nb"),
                    r.getInt("capacity"),
                    r.getDecimal("price")));
        });
        return hotels;
    }

    public List<Reservation> selectAllReservations(Session session) {
        StringBuilder sb =
                new StringBuilder("SELECT * FROM ").append("reservations");
        String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<Reservation> hotels = new ArrayList<Reservation>();
        rs.forEach(r -> {
            hotels.add(new Reservation(
                    r.getUUID("reservation_id"),
                    r.getUUID("room_id"),
                    LocalDate.ofEpochDay(r.getDate("start_date").getDaysSinceEpoch()),
                    LocalDate.of(r.getDate("end_date").getYear(), r.getDate("end_date").getMonth(), r.getDate("end_date").getDay())));
        });
        return hotels;
    }

    public void insertIntoHotel(Hotel hotel, Session session) {
        session.execute("INSERT INTO hotel (hotel_id, name, phone, address) " +
                "VALUES (" + hotel.getHotel_id() + ", " + hotel.getName() + ", " + hotel.getPhone() + ", " + hotel.getAddress() + ")");
    }

    public void insertIntoRoom(Room room, Session session) {
        session.execute("INSERT INTO room (room_id, hotel_id, room_nb, capacity, price) " +
                "VALUES (" + room.getRoom_id() + ", " + room.getHotel_id() + ", " + room.getRoom_nb() + ", " + room.getCapacity() + ", " + room.getPrice() + ")");
    }

    public void insertIntoReservation(Reservation reservation, Session session) {
        session.execute("INSERT INTO reservations (reservation_id, room_id, start_date, end_date) " +
                "VALUES (" + reservation.getReservation_id() + ", " + reservation.getRoom_id() + ", " + reservation.getStart_date().toEpochDay() + ", " + reservation.getEnd_date().toEpochDay() + ")");
    }


    public void createTableHotelTable(Session session) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("hotel").append("(")
                .append("hotel_id uuid PRIMARY KEY, ")
                .append("name text,")
                .append("phone text,")
                .append("address text);");
        String query = sb.toString();
        session.execute(query);
    }

    public void createTableRoomTable(Session session) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("room").append("(")
                .append("room_id uuid, ")
                .append("hotel_id uuid, ")
                .append("room_nb int,")
                .append("capacity int,")
                .append("price decimal,")
                .append("PRIMARY KEY(hotel_id, capacity, price));");
        String query = sb.toString();
        session.execute(query);
    }

    public void createTableReservationTable(Session session) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("reservations").append("(")
                .append("reservation_id uuid PRIMARY KEY, ")
                .append("room_id uuid, ")
                .append("start_date date,")
                .append("end_date date);");
        String query = sb.toString();
        session.execute(query);
    }

    public List<Room> getRoomsByCapacity(Session session, Integer capacity) {
        String query = "SELECT * FROM room WHERE capacity >= " + capacity;
        ResultSet rs = session.execute(query);

        List<Room> rooms = new ArrayList<Room>();

        rs.forEach(r -> {
            rooms.add(new Room(
                    r.getUUID("room_id"),
                    r.getUUID("hotel_id"),
                    r.getInt("room_nb"),
                    r.getInt("capacity"),
                    r.getDecimal("price")));
        });

        return rooms;
    }

    public List<Room> getRoomsByPrice(Session session, Integer price) {
        String query = "SELECT * FROM room WHERE price <= " + price;
        ResultSet rs = session.execute(query);

        List<Room> rooms = new ArrayList<Room>();

        rs.forEach(r -> {
            rooms.add(new Room(
                    r.getUUID("room_id"),
                    r.getUUID("hotel_id"),
                    r.getInt("room_nb"),
                    r.getInt("capacity"),
                    r.getDecimal("price")));
        });

        return rooms;
    }

    public List<Room> getRoomsByRoomId(Session session, UUID room_id) {
        String query = "SELECT * FROM room WHERE room_id = " + room_id;
        ResultSet rs = session.execute(query);
        List<Room> rooms = new ArrayList<Room>();

        rs.forEach(r -> {
            rooms.add(new Room(
                    r.getUUID("room_id"),
                    r.getUUID("hotel_id"),
                    r.getInt("room_nb"),
                    r.getInt("capacity"),
                    r.getDecimal("price")));
        });

        return rooms;
    }

    public List<Room> getRoomsByHotelIdAndRoomNb(Session session, UUID hotel_id, Integer capacity, BigDecimal price) {
        String query = "SELECT * FROM room WHERE hotel_id = " + hotel_id + " AND capacity = " + capacity + " AND price <= " + price;
        ResultSet rs = session.execute(query);
        List<Room> rooms = new ArrayList<Room>();

        rs.forEach(r -> {
            rooms.add(new Room(
                    r.getUUID("room_id"),
                    r.getUUID("hotel_id"),
                    r.getInt("room_nb"),
                    r.getInt("capacity"),
                    r.getDecimal("price")));
        });

        return rooms;
    }
}
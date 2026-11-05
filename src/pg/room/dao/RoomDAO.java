package pg.room.dao;

import java.util.ArrayList;
import java.util.List;
import pg.room.model.Room;

public class RoomDAO {

    private static ArrayList<Room> rooms = new ArrayList<>();
    private static int nextId = 1;

    public void addRoom(Room room) {
        room.setRoomId(nextId++);
        rooms.add(room);
    }

    public ArrayList<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(int id) {
        for (Room room : rooms) {
            if (room.getRoomId() == id) {
                return room;
            }
        }
        return null;
    }

    public boolean updateRoom(int id, Room updatedRoom) {
        Room room = getRoomById(id);

        if (room == null) {
            return false;
        }

        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setRoomType(updatedRoom.getRoomType());
        room.setRent(updatedRoom.getRent());
        room.setCapacity(updatedRoom.getCapacity());
        room.setStatus(updatedRoom.getStatus());

        return true;
    }

    public boolean deleteRoom(int id) {
        Room room = getRoomById(id);

        if (room == null) {
            return false;
        }

        rooms.remove(room);
        return true;
    }
}

package pg.room.service;

import java.util.ArrayList;

import pg.room.dao.RoomDAO;
import pg.room.dao.RoomAllocationDAO;
import pg.room.model.Room;
import pg.room.model.RoomAllocation;

public class RoomService {

    private RoomDAO dao = new RoomDAO();
    private RoomAllocationDAO allocationDAO = new RoomAllocationDAO();

    public boolean addRoom(Room room) {

        if (!validateRoom(room)) {
            return false;
        }

        for (Room r : dao.getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(room.getRoomNumber())) {
                return false;
            }
        }

        room.setStatus("Available");
        dao.addRoom(room);
        return true;
    }

    public ArrayList<Room> getAllRooms() {
        updateAllStatuses();
        return dao.getAllRooms();
    }

    public boolean updateRoom(int id, Room updatedRoom) {

        if (!validateRoom(updatedRoom)) {
            return false;
        }

        Room oldRoom = dao.getRoomById(id);

        if (oldRoom == null) {
            return false;
        }

        for (Room r : dao.getAllRooms()) {
            if (r.getRoomNumber().equalsIgnoreCase(updatedRoom.getRoomNumber())
                    && r.getRoomId() != id) {
                return false;
            }
        }

        if (updatedRoom.getCapacity() <
                allocationDAO.getOccupiedCount(id)) {
            return false;
        }

        updatedRoom.setStatus(oldRoom.getStatus());

        boolean updated = dao.updateRoom(id, updatedRoom);

        updateStatus(id);

        return updated;
    }

    public boolean deleteRoom(int id) {

        if (allocationDAO.getOccupiedCount(id) > 0) {
            return false;
        }

        return dao.deleteRoom(id);
    }

    public boolean allocateRoom(int studentId, int roomId) {

        Room room = dao.getRoomById(roomId);

        if (room == null || studentId <= 0) {
            return false;
        }

        if (allocationDAO.isStudentAllocated(studentId)) {
            return false;
        }

        int occupied = allocationDAO.getOccupiedCount(roomId);

        if (occupied >= room.getCapacity()) {
            return false;
        }

        allocationDAO.addAllocation(studentId, roomId);

        updateStatus(roomId);

        return true;
    }

    public boolean vacateRoom(int studentId) {

        for (RoomAllocation a : allocationDAO.getAllAllocations()) {

            if (a.getStudentId() == studentId) {

                int roomId = a.getRoomId();

                boolean removed = allocationDAO.removeAllocation(studentId);

                updateStatus(roomId);

                return removed;
            }
        }

        return false;
    }

    public ArrayList<RoomAllocation> getAllAllocations() {
        return allocationDAO.getAllAllocations();
    }

    public Room searchRoom(String search) {

        for (Room room : dao.getAllRooms()) {

            if (String.valueOf(room.getRoomId()).equals(search)
                    || room.getRoomNumber().equalsIgnoreCase(search)) {
                return room;
            }
        }

        return null;
    }

    public int getOccupiedCount(int roomId) {
        return allocationDAO.getOccupiedCount(roomId);
    }

    public int getAvailableRooms() {

        int count = 0;

        for (Room room : dao.getAllRooms()) {
            if (allocationDAO.getOccupiedCount(room.getRoomId())
                    < room.getCapacity()) {
                count++;
            }
        }

        return count;
    }

    public int getOccupiedRooms() {

        int count = 0;

        for (Room room : dao.getAllRooms()) {
            if (allocationDAO.getOccupiedCount(room.getRoomId())
                    == room.getCapacity()) {
                count++;
            }
        }

        return count;
    }

    private void updateStatus(int roomId) {

        Room room = dao.getRoomById(roomId);

        if (room != null) {

            int occupied = allocationDAO.getOccupiedCount(roomId);

            if (occupied >= room.getCapacity()) {
                room.setStatus("Occupied");
            } else {
                room.setStatus("Available");
            }
        }
    }

    private void updateAllStatuses() {

        for (Room room : dao.getAllRooms()) {
            updateStatus(room.getRoomId());
        }
    }

    public boolean validateRoom(Room room) {

        if (room.getRoomNumber() == null ||
                room.getRoomNumber().trim().isEmpty()) {
            return false;
        }

        if (room.getRoomType() == null ||
                room.getRoomType().isEmpty()) {
            return false;
        }

        if (room.getRent() <= 0 || room.getCapacity() <= 0) {
            return false;
        }

        return true;
    }
}

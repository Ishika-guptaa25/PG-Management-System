
package pg.room.dao;

import java.util.ArrayList;
import pg.room.model.RoomAllocation;

public class RoomAllocationDAO {

    private static ArrayList<RoomAllocation> allocations =
            new ArrayList<>();

    private static int nextId = 1;

    public void addAllocation(int studentId, int roomId) {

        RoomAllocation allocation =
                new RoomAllocation(nextId++, studentId, roomId);

        allocations.add(allocation);
    }

    public ArrayList<RoomAllocation> getAllAllocations() {
        return new ArrayList<>(allocations);
    }

    public boolean removeAllocation(int studentId) {

        for (RoomAllocation a : allocations) {

            if (a.getStudentId() == studentId) {
                allocations.remove(a);
                return true;
            }
        }

        return false;
    }

    public int getOccupiedCount(int roomId) {

        int count = 0;

        for (RoomAllocation a : allocations) {

            if (a.getRoomId() == roomId) {
                count++;
            }
        }

        return count;
    }

    public boolean isStudentAllocated(int studentId) {

        for (RoomAllocation a : allocations) {

            if (a.getStudentId() == studentId) {
                return true;
            }
        }

        return false;
    }
}


package pg.room.model;

public class RoomAllocation {

    private int allocationId;
    private int studentId;
    private int roomId;

    public RoomAllocation(int allocationId, int studentId, int roomId) {
        this.allocationId = allocationId;
        this.studentId = studentId;
        this.roomId = roomId;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getRoomId() {
        return roomId;
    }
}

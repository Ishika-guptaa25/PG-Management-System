package pg.complaint.dao;

import java.util.ArrayList;
import pg.complaint.model.Complaint;

public class ComplaintDAO {

    private static ArrayList<Complaint> complaints = new ArrayList<>();

    private static int nextId = 1;

    public void addComplaint(Complaint complaint) {

        complaint.setComplaintId(nextId++);

        complaints.add(complaint);
    }

    public ArrayList<Complaint> getAllComplaints() {

        return new ArrayList<>(complaints);
    }

    public Complaint getComplaintById(int complaintId) {

        for (Complaint c : complaints) {

            if (c.getComplaintId() == complaintId) {
                return c;
            }
        }

        return null;
    }

    public boolean updateComplaint(int complaintId, Complaint updatedComplaint) {

        for (Complaint c : complaints) {

            if (c.getComplaintId() == complaintId) {

                c.setStudentId(updatedComplaint.getStudentId());
                c.setCategory(updatedComplaint.getCategory());
                c.setDescription(updatedComplaint.getDescription());
                c.setComplaintDate(updatedComplaint.getComplaintDate());
                c.setStatus(updatedComplaint.getStatus());

                return true;
            }
        }

        return false;
    }

    public boolean deleteComplaint(int complaintId) {

        for (Complaint c : complaints) {

            if (c.getComplaintId() == complaintId) {

                complaints.remove(c);
                return true;
            }
        }

        return false;
    }

    public ArrayList<Complaint> getComplaintsByStudentId(int studentId) {

        ArrayList<Complaint> result = new ArrayList<>();

        for (Complaint c : complaints) {

            if (c.getStudentId() == studentId) {
                result.add(c);
            }
        }

        return result;
    }

}
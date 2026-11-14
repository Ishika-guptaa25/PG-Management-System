package pg.complaint.service;

import pg.complaint.dao.ComplaintDAO;
import pg.complaint.model.Complaint;
import java.util.ArrayList;

public class ComplaintService {

    private ComplaintDAO complaintDAO = new ComplaintDAO();

    public void addComplaint(Complaint complaint) {

        if (complaint.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid Student ID");
        }

        if (complaint.getCategory() == null || complaint.getCategory().isBlank()) {
            throw new IllegalArgumentException("Complaint category cannot be empty");
        }

        if (complaint.getDescription() == null || complaint.getDescription().isBlank()) {
            throw new IllegalArgumentException("Complaint description cannot be empty");
        }

        complaint.setStatus("Pending");

        complaintDAO.addComplaint(complaint);
    }

    public ArrayList<Complaint> getAllComplaints() {

        return complaintDAO.getAllComplaints();

    }

    public Complaint getComplaintById(int complaintId) {

        if (complaintId <= 0) {
            throw new IllegalArgumentException("Invalid Complaint ID");
        }

        return complaintDAO.getComplaintById(complaintId);
    }

    public ArrayList<Complaint> getComplaintsByStudentId(int studentId) {

        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid Student ID");
        }

        return complaintDAO.getComplaintsByStudentId(studentId);
    }

    public void updateComplaintStatus(int complaintId, String status) {

        if (complaintId <= 0) {
            throw new IllegalArgumentException("Invalid Complaint ID");
        }

        if (status == null ||
                !(status.equals("Pending") ||
                        status.equals("In Progress") ||
                        status.equals("Resolved"))) {

            throw new IllegalArgumentException("Invalid Complaint Status");
        }

        Complaint complaint = complaintDAO.getComplaintById(complaintId);

        if (complaint == null) {
            throw new IllegalArgumentException("Complaint not found");
        }

        complaint.setStatus(status);
    }
    public void deleteComplaint(int complaintId) {

        if (complaintId <= 0) {
            throw new IllegalArgumentException("Invalid Complaint ID");
        }

        Complaint complaint = complaintDAO.getComplaintById(complaintId);

        if (complaint == null) {
            throw new IllegalArgumentException("Complaint not found");
        }

        complaintDAO.deleteComplaint(complaintId);
    }


}
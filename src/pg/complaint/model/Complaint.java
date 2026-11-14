package pg.complaint.model;

public class Complaint {

    private int complaintId;
    private int studentId;
    private String category;
    private String description;
    private String complaintDate;
    private String status;
    public Complaint(int complaintId, int studentId,
                     String category, String description,
                     String complaintDate, String status) {

        this.complaintId = complaintId;
        this.studentId = studentId;
        this.category = category;
        this.description = description;
        this.complaintDate = complaintDate;
        this.status = status;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public String getCategory() {
        return category;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public String getDescription() {
        return description;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
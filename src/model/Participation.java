package model;

public class Participation {
    private int participationID;
    private Project project;
    private String username;
    private String participationDate;
    private int numSlots;
    private int totalHours;
    private double totalContribution;

    public Participation(int participationID, Project project, String username, String participationDate, int numSlots, int totalHours, double totalContribution) {
        this.participationID = participationID;
        this.project = project;
        this.username = username;
        this.participationDate = participationDate;
        this.numSlots = numSlots;
        this.totalHours = totalHours;
        this.totalContribution = totalContribution;
    }

    public int getParticipationID() {
        return participationID;
    }
    public void setParticipationID(int participationID) {
        this.participationID = participationID;
    }
    
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getParticipationDate() {
        return participationDate;
    }
    public void setParticipationDate(String participationDate) {
        this.participationDate = participationDate;
    }

    public int getNumSlots() {
        return numSlots;
    }
    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }

    public int getTotalHours() {
        return totalHours;
    }
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public double getTotalContribution() {
        return totalContribution;
    }
    public void setTotalContribution(double totalContribution) {
        this.totalContribution = totalContribution;
    }
}

package model;

public class Project {
    private int projectID;
    private String projectTitle;
    private String projectLocation;
    private String projectDay;
    private double hourlyValue;
    private int registeredSlots;
    private int totalSlots;
    private int availableSlots;
    private boolean enabled;

    public Project(int projectID, String projectTitle, String projectLocation, String projectDay, double hourlyValue, int registeredSlots, int totalSlots, boolean enabled) {
        this.projectID = projectID;
        this.projectTitle = projectTitle;
        this.projectLocation = projectLocation;
        this.projectDay = projectDay;
        this.hourlyValue = hourlyValue;
        this.registeredSlots = registeredSlots;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots - registeredSlots < 0 ? 0 : totalSlots - registeredSlots;
        this.enabled = enabled;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getProjectTitle() {
        return projectTitle;
    }
    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }
    
    public String getProjectLocation() {
        return projectLocation;
    }
    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectDay() {
        return projectDay;
    }
    public void setProjectDay(String projectDay) {
        this.projectDay = projectDay;
    }

    public double getHourlyValue() {
        return hourlyValue;
    }
    public void setHourlyValue(double hourlyValue) {
        this.hourlyValue = hourlyValue;
    }

    public int getRegisteredSlots() {
        return registeredSlots;
    }
    // Update available slots when updating registered slots
    public void setRegisteredSlots(int registeredSlots) {
        this.registeredSlots = registeredSlots;
        setAvailableSlots();
    }

    public int getTotalSlots() {
        return totalSlots;
    }
    // Update available slots when updating total slots
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
        setAvailableSlots();
    }

    public int getAvailableSlots() {
        return availableSlots;
    }
    private void setAvailableSlots() {
        this.availableSlots = this.totalSlots - this.registeredSlots < 0 ? 0 : this.totalSlots - this.registeredSlots;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

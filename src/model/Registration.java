package model;

public class Registration {
    private int registrationID;
    private Project project;
    private String username;
    private int slotOneHours;
    private int slotTwoHours;
    private int slotThreeHours;
    
    public Registration(Project project, String username, int slotOneHours, int slotTwoHours, int slotThreeHours) {
        this.project = project;
        this.username = username;
        this.slotOneHours = slotOneHours;
        this.slotTwoHours = slotTwoHours;
        this.slotThreeHours = slotThreeHours;
    }

    public Registration(int registrationID, Project project, String username, int slotOneHours, int slotTwoHours, int slotThreeHours) {
        this.registrationID = registrationID;
        this.project = project;
        this.username = username;
        this.slotOneHours = slotOneHours;
        this.slotTwoHours = slotTwoHours;
        this.slotThreeHours = slotThreeHours;
    }
    
    public int getRegistrationID() {
        return registrationID;
    }
    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
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
    
    public int getSlotOneHours() {
        return slotOneHours;
    }
    public void setSlotOneHours(int slotOneHours) {
        this.slotOneHours = slotOneHours;
    }

    public int getSlotTwoHours() {
        return slotTwoHours;
    }
    public void setSlotTwoHours(int slotTwoHours) {
        this.slotTwoHours = slotTwoHours;
    }

    public int getSlotThreeHours() {
        return slotThreeHours;
    }
    public void setSlotThreeHours(int slotThreeHours) {
        this.slotThreeHours = slotThreeHours;
    }
}

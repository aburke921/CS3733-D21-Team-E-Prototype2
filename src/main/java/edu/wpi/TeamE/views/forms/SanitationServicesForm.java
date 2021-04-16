package edu.wpi.TeamE.views.forms;

public class SanitationServicesForm {

  private String departmentField;
  private String roomField;
  private String numberField;
  private String serviceTypeField;
  private String assignmentField;
  //private String instructions;
  private boolean status;

  public SanitationServicesForm(String departmentField, String roomField, String numberField, String serviceTypeField, String assignmentField) {

    this.departmentField = departmentField;
    this.roomField = roomField;
    this.numberField = numberField;
    this.serviceTypeField = serviceTypeField;
    this.assignmentField = assignmentField;
    //this.instructions = instructions;
    this.status = false;

  }

  public String getDepartmentField() {
      return this.departmentField;
  }

  public String getRoomField() {
      return this.roomField;
  }

  public String getNumberField() {
      return this.numberField;
  }

  public String getServiceTypeField() {
      return this.serviceTypeField;
  }

  public String getAssignmentField() {
      return this.assignmentField;
  }

//  public String getInstructions() {
//      return this.instructions;
//  }
}

package com.example.cyber_incident_management_system;

public class Incident {

    // Incident attributes
    int id;
    String title;
    String description;
    String incidentType;
    String severity;
    String status;
    String reportedDate;
    String affectedAsset;

    // Constructor
    public Incident(int id,
                    String title,
                    String description,
                    String incidentType,
                    String severity,
                    String status,
                    String reportedDate,
                    String affectedAsset) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.incidentType = incidentType;
        this.severity = severity;
        this.status = status;
        this.reportedDate = reportedDate;
        this.affectedAsset = affectedAsset;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public String getAffectedAsset() {
        return affectedAsset;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public void setAffectedAsset(String affectedAsset) {
        this.affectedAsset = affectedAsset;
    }
}

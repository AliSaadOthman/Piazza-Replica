package org.piazza.model.wrappers;

import org.piazza.model.document.Report;

public class ReportWrapper {
    private String id;
    private String userCreatedReport;
    private String userReported;
    private String reason;
    public ReportWrapper(Report report){
        this.id=report.getArangoId();
        if(report.getUserCreatedReport()!=null){
            this.userCreatedReport=report.getUserCreatedReport().getEmail();
        }else{
            this.userCreatedReport="Deleted User";
        }
        if(report.getUserReported()!=null){
            this.userReported=report.getUserReported().getEmail();
        }else{
            this.userReported="Deleted User";
        }
        this.reason=report.getReason();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCreatedReport() {
        return userCreatedReport;
    }

    public void setUserCreatedReport(String userCreatedReport) {
        this.userCreatedReport = userCreatedReport;
    }

    public String getUserReported() {
        return userReported;
    }

    public void setUserReported(String userReported) {
        this.userReported = userReported;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

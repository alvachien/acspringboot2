package com.alvachien.learning.java.acspringboot2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="t_fin_account_ext_dp")
public class FinAccountExtDP implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="ACCOUNTID")
    private int accountid;
    @Column(name="DIRECT")
    private boolean direct;
    @Column(name="STARTDATE")
    private Date startdate;
    @Column(name="ENDDATE")
    private Date enddate;
    @Column(name="RPTTYPE")
    private short rpttype;
    @Column(name="REFDOCID")
    private int refdocid;
    @Column(name="DEFRRDAYS")
    private String defrrdays;
    @Column(name="COMMENT")
    private String comment;

    public int getAccountID() {
        return this.accountid;
    }
    public void setAccountID(int id) {
        this.accountid = id;
    }

    public boolean getDirect() {
        return this.direct;
    }

    public void setDirect(boolean dir) {
        this.direct = dir;
    }

    public Date getStartDate() {
        return this.startdate;
    }

    public void setStartDate(Date st) {
        this.startdate = st;
    }

    public Date getEndDate() {
        return this.enddate;
    }

    public void setEndDate(Date st) {
        this.enddate = st;
    }

    public short getRptType() {
        return this.rpttype;
    }

    public void setRptType(short rt) {
        this.rpttype = rt;
    }

    public int getRefDocID() {
        return this.refdocid;
    }

    public void setRefDocID(int rfid) {
        this.refdocid = rfid;
    }

    public String getDefrrDays() {
        return this.defrrdays;
    }

    public void setDefrrDays(String dfdays) {
        this.defrrdays = dfdays;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // @Override
    // public String toString() {
    //     return "Product [summary=" + summary + ", description=" + description + "]";
    // }
}

package com.alvachien.learning.java.acspringboot2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="t_fin_account_ctgy")
public class FinAccountCategory implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    @Column(name="NAME")
    private String name;
    @Column(name="ASSETFLAG")
    private boolean assetflag;
    @Column(name="COMMENT")
    private String comment;
    @Column(name="SYSFLAG")
    private boolean sysflag;
    @Column(name="CREATEDBY")
    private String createdby;
    @Column(name="CREATEDAT")
    private Date createdat;
    @Column(name="UPDATEDBY")
    private String updatedby;
    @Column(name="UPDATEDAT")
    private Date updatedat;

    public int getID() {
        return this.id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;;
    }

    public boolean getAssetFlag() {
        return this.assetflag;
    }

    public void setAssetFlag(boolean assetflag) {
        this.assetflag = assetflag;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public boolean getSysFlag() {
        return this.sysflag;
    }

    public void setSysFlag(boolean sysflag) {
        this.sysflag = sysflag;
    }
    public String getCreatedBy() {
        return this.createdby;
    }

    public void setCreatedBy(String createdby) {
        this.createdby = createdby;
    }

    public Date getCreatedAt() {
        return this.createdat;
    }

    public void setCreatedAt(Date createdat) {
        this.createdat = createdat;
    }

    public String getUpdatedBy() {
        return this.updatedby;
    }

    public void setUpdatedBy(String updatedby) {
        this.updatedby = updatedby;
    }

    public Date getUpdatedAt() {
        return this.updatedat;
    }

    public void setUpdatedAt(Date udpatedat) {
        this.updatedat = udpatedat;
    }

    // @Override
    // public String toString() {
    //     return "Product [summary=" + summary + ", description=" + description + "]";
    // }

}

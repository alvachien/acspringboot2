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
@Table(name="t_fin_account")
public class FinAccount implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    @Column(name="CTGYID")
    private int ctgyid;
    @Column(name="NAME")
    private String name;
    @Column(name="COMMENT")
    private String comment;
    @Column(name="OWNER")
    private String owner;
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

    public int getCtgyID() {
        return this.ctgyid;
    }

    public void setCtgyID(int ctgyid) {
        this.ctgyid = ctgyid;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

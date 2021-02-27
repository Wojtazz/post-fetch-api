package com.wwesolowski.postfetchapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table

public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer postId;

    @NotNull
    private ModifyType modifyType;

    @NotNull
    private Date modifyDate;

    public Activity() {

    }
    public Activity(Integer postId, ModifyType modifyType, Date modifyDate) {
        this.postId = postId;
        this.modifyType = modifyType;
        this.modifyDate = modifyDate;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public ModifyType getModifyType() {
        return modifyType;
    }

    public void setModifyType(ModifyType modifyType) {
        this.modifyType = modifyType;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}

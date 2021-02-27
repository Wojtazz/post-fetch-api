package com.wwesolowski.postfetchapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private Integer postId;
    
    @NotNull
    private ModifyType modifyType;

    @NotNull
    private Date modifyDate;

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

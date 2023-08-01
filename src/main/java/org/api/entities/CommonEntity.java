package org.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public abstract class CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "del_flg")
    @JsonProperty("delFlg")
    @JsonIgnore
    private Integer delFlg = 0;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    @JsonProperty("createDate")
    @JsonIgnore
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    @JsonProperty("updateDate")
    @JsonIgnore
    private Date updateDate;
}

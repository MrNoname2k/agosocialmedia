package org.api.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class CommonResponse {
    @JsonProperty("delFlg")
    private Integer delFlg = 0;

    @JsonProperty("createDate")
    private String createDate;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("id")
    private String id;
}

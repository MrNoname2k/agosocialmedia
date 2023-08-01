package org.api.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.entities.UserEntity;
import org.checkerframework.checker.units.qual.N;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse extends CommonResponse{

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("idUserFrom")
    private UserEntity userEntityFrom;

    @JsonProperty("idUserTo")
    private UserEntity userEntityTo;
}

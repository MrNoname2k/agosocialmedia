package org.api.payload.response.messageResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.entities.RelationshipEntity;
import org.api.entities.UserEntity;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.RelationshipResponse;
import org.api.payload.response.homePageResponses.UserHomeRespon;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse extends CommonResponse {

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("userFrom")
    private UserEntity userEntityFrom;

    @JsonProperty("userTo")
    private UserEntity userEntityTo;

    @JsonProperty("relationship")
    private RelationshipEntity relationship;
}

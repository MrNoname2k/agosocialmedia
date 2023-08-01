package org.api.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.UserResponse.UserResponse;
import org.api.payload.response.homePageResponses.UserHomeRespon;
import org.api.payload.response.messageResponse.MessageResponseForHeaderList;
import org.api.payload.response.messageResponse.MessageViewAllResponse;
import org.api.payload.response.messageResponse.MessagesBetweenTwoUserResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipResponse extends CommonResponse{

    @JsonProperty("status")
    private String status;

    @JsonProperty("messages")
    private List<MessageResponseForHeaderList> messages;

    @JsonProperty("idUserOne")
    private UserHomeRespon userEntityOne;

    @JsonProperty("idUserTow")
    private UserHomeRespon userEntityTow;
}

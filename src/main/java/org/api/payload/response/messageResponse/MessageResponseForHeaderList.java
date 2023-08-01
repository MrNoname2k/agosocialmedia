package org.api.payload.response.messageResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.homePageResponses.UserHomeRespon;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseForHeaderList extends CommonResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("userFrom")
    private UserHomeRespon userEntityFrom;
}

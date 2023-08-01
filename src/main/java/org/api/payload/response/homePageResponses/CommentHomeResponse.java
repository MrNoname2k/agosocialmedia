package org.api.payload.response.homePageResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.UserResponse.UserResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentHomeResponse extends CommonResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("idComment")
    private String idComment;

    @JsonProperty("userEntityComment")
    private UserHomeRespon userEntityComment;
}

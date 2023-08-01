package org.api.payload.response.homePageResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.UserResponse.PostResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHomeResponse extends CommonResponse {

    @JsonProperty("type")
    private String type;

    @JsonProperty("content")
    private String content;

    @JsonProperty("idUserCreate")
    private UserHomeRespon userEntity;

    @JsonProperty("idPost")
    private PostResponse postEntity;
}

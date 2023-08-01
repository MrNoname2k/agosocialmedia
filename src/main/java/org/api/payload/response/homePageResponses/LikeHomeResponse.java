package org.api.payload.response.homePageResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeHomeResponse extends CommonResponse {
    @JsonProperty("userEntityLike")
    private UserHomeRespon userEntityLike;
}

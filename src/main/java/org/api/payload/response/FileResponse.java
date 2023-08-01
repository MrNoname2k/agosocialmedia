package org.api.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse extends CommonResponse {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("isCurrenAvatar")
    private Integer isCurrenAvatar = 0;

    @JsonProperty("isCurrenBanner")
    private Integer isCurrenBanner = 0;
}

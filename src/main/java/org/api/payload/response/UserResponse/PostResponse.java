package org.api.payload.response.UserResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.FileResponse;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse extends CommonResponse {

    @JsonProperty("content")
    private String content;

    @JsonProperty("accessModifierLevel")
    private String accessModifierLevel;

    @JsonProperty("typePost")
    private String typePost;

    @JsonProperty("idAlbum")
    private AlbumResponse albumEntityPost;

    @JsonProperty("fileEntities")
    private List<FileResponse> fileEntities;

}

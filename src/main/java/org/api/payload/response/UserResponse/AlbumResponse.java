package org.api.payload.response.UserResponse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse extends CommonResponse {
    @JsonProperty("name")
    private String name;

    @JsonProperty("typeAlbum")
    private String typeAlbum;
}

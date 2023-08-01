package org.api.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.entities.UserEntity;
import org.api.payload.response.CommonResponse;
import org.api.payload.response.homePageResponses.PostHomeRespon;
import org.api.payload.response.homePageResponses.UserHomeRespon;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebNotification extends CommonResponse implements Serializable{

    private static final long serialVersionUID = 1L;

    private UserHomeRespon userEntity;
    private String type;
    private String content;
    private PostHomeRespon postEntity;
    private String status;
}

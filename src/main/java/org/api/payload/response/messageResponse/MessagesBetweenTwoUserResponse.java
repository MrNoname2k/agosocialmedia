package org.api.payload.response.messageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.homePageResponses.PostHomeRespon;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagesBetweenTwoUserResponse {
    private String toUserId;
    private String toUserFirstName;
    private String toUserLastName;
    private List<PostHomeRespon> avatar;
    private boolean login;
    private List<MessageViewAllResponse> messages;
}

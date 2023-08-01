package org.api.payload.response.messageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageViewAllResponse extends CommonResponse {
    private String content;
    private String status;
    private String fromUserId;
}

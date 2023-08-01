package org.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMessagesResponse extends CommonResponse {
    private String fromUserId;
    private String fromUserFirstName;
    private String fromUserLastName;
    private String content;
}

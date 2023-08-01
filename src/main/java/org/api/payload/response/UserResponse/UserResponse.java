package org.api.payload.response.UserResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.CommonResponse;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends CommonResponse {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("birthDay")
    private String birthDay;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("linkIg")
    private String linkIg;

    @JsonProperty("linkFacebook")
    private String linkFacebook;

    @JsonProperty("lastLoginDate")
    private Date lastLoginDate;

    @JsonProperty("online")
    private Boolean online;

    @JsonProperty("posts")
    private List<PostResponse> posts;

    @JsonProperty("status")
    private String status;

    @JsonProperty("description")
    private String description;

}

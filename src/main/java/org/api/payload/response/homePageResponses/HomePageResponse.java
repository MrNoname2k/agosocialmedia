package org.api.payload.response.homePageResponses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.payload.response.RelationshipResponse;
import org.api.payload.response.pageResponse.MessagePageResponse;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class HomePageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserHomeRespon userEntity;
    private List<RelationshipResponse> relationshipEntities;
    private PostHomePageResponse postEntityPage;
    private NotifiHomePageResponse notificationEntityPage;
    private MessagePageResponse messagePageResponse;
    private List<RecommendFriendsHomeResponse> recommendFriends;
}

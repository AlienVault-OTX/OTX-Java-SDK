package com.alienvault.otx.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for User JSON
 * {
 * "username": "root",
 * "pulse_count": 0,
 * "subscriber_count": 1,
 * "follower_count": 3,
 * "award_count": 0,
 * "member_since": "453 days ago ",
 * "accepted_edits_count": 0,
 * "avatar_url": "https://otx20-dev-web-media.s3.amazonaws.com/media/avatars/root/resized/80/otx-learning-center_d7TpH1y.png"
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String username;
    private int pulse_count;
    private int subscriber_count;
    private int follower_count;
    private int award_count;
    private int accepted_edits_count;
    private String member_since;
    private String avatar_url;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPulse_count() {
        return pulse_count;
    }

    public void setPulse_count(int pulse_count) {
        this.pulse_count = pulse_count;
    }

    public int getSubscriber_count() {
        return subscriber_count;
    }

    public void setSubscriber_count(int subscriber_count) {
        this.subscriber_count = subscriber_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getAward_count() {
        return award_count;
    }

    public void setAward_count(int award_count) {
        this.award_count = award_count;
    }

    public int getAccepted_edits_count() {
        return accepted_edits_count;
    }

    public void setAccepted_edits_count(int accepted_edits_count) {
        this.accepted_edits_count = accepted_edits_count;
    }

    public String getMember_since() {
        return member_since;
    }

    public void setMember_since(String member_since) {
        this.member_since = member_since;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}

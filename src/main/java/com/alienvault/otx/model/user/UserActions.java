package com.alienvault.otx.model.user;

/**
 * Created by rspitler on 5/10/16.
 */
public enum UserActions {
    /**
     * subscribe: Include all of this users' pulses in my threat intelligence feed.
     */
    SUBSCRIBE("subscribe"),
    /**
     * unsubscribe: Remove all of this users' pulses from my threat intelligence feed.
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * follow: Include all of this users' pulses in my activity feed, (Individual pulses must be subscribed to directly for inclusion in threat intelligence feed).
     */
    FOLLOW("follow"),
    /**
     * unfollow: Remove all of this users' pulses from my activity feed.
     */
    UNFOLLOW("unfollow");

    private String action;

    UserActions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}

package org.mazhuang.guanggoo.data.entity;

/**
 * Created by mazhuang on 2017/9/16.
 */

public class Meta {
    private Node node;
    private User user;
    private String lastTouched;
    private User lastReplyUser;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastTouched() {
        return lastTouched;
    }

    public void setLastTouched(String lastTouched) {
        this.lastTouched = lastTouched;
    }

    public User getLastReplyUser() {
        return lastReplyUser;
    }

    public void setLastReplyUser(User lastReplyUser) {
        this.lastReplyUser = lastReplyUser;
    }
}
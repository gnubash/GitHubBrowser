package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

/**
 * Created by Anton.
 */
@Entity(tableName = "users", indices = {@Index(value = {"id"}, unique = true)})
public class UserEntry {

    @PrimaryKey(autoGenerate = true)
    private int users_id;

    @ColumnInfo(name = "id")
    private int id;

    private String login;

    private String followers_url;
    private int followers;
    private String following_url;
    private int following;
    private String avatar_url;

    private byte [] userImage;

    @TypeConverters(ReposListConverter.class)
    private List<String> ownedRepos;
    private String repos_url;
    @TypeConverters(ReposListConverter.class)
    private List<String> starredRepos;
    private String starred_url;

    public UserEntry() {

    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getFollowing_url() {
        return following_url;
    }

    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public byte [] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte [] userImage) {
        this.userImage = userImage;
    }

    public List<String> getOwnedRepos() {
        return ownedRepos;
    }

    public void setOwnedRepos(List<String> ownedRepos) {
        this.ownedRepos = ownedRepos;
    }

    public String getRepos_url() {
        return repos_url;
    }

    public void setRepos_url(String repos_url) {
        this.repos_url = repos_url;
    }

    public List<String> getStarredRepos() {
        return starredRepos;
    }

    public void setStarredRepos(List<String> starredRepos) {
        this.starredRepos = starredRepos;
    }

    public String getStarred_url() {
        return starred_url;
    }

    public void setStarred_url(String starred_url) {
        this.starred_url = starred_url;
    }

    @Override
    public String toString() {

        StringBuilder asString = new StringBuilder();
        String separator = " ";
        asString.append(id).append(separator)
                .append(login).append(separator)
                .append(followers_url).append(separator)
                .append(followers).append(separator)
                .append(following_url).append(separator)
                .append(following);

        if (ownedRepos != null) {
            asString.append(separator).append(ownedRepos.toString());
        }
        if (starredRepos != null) {
            asString.append(separator).append(starredRepos.toString());
        }

        return asString.toString();
    }
}

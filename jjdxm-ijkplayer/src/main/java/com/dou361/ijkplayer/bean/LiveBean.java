package com.dou361.ijkplayer.bean;



public class LiveBean {
    private String nickname;
    private long livestarttime;
    private String liveStream;
    private String portrait;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getLivestarttime() {
        return livestarttime;
    }

    public void setLivestarttime(long livestarttime) {
        this.livestarttime = livestarttime;
    }

    public String getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(String liveStream) {
        this.liveStream = liveStream;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}

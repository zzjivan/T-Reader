package com.snick.zzj.t_reader.beans;

/**
 * Created by zzj on 17-2-6.
 * "status": 1,
    "msg": "【更新内容】（后略）",
    "latest": "2.2.0"
 */

public class Version {
    private int status;
    private String msg;
    private String lastest;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLastest() {
        return lastest;
    }

    public void setLastest(String lastest) {
        this.lastest = lastest;
    }
}

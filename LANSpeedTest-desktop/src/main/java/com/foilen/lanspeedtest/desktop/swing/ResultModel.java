/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.desktop.swing;

import com.foilen.lanspeedtest.core.events.ServerFoundEvent;

public class ResultModel {

    private String name;
    private String host;
    private int port;
    private Double downloadSpeedMbps;
    private Double uploadSpeedMbps;
    private String comment;

    private boolean active;
    private boolean pendingMesure;

    public ResultModel() {
    }

    public ResultModel(ServerFoundEvent event) {
        name = event.getName();
        host = event.getHost();
        port = event.getPort();
        active = true;
    }

    public String getComment() {
        return comment;
    }

    public Double getDownloadSpeedMbps() {
        return downloadSpeedMbps;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public Double getUploadSpeedMbps() {
        return uploadSpeedMbps;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPendingMesure() {
        return pendingMesure;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDownloadSpeedMbps(Double downloadSpeedMbps) {
        this.downloadSpeedMbps = downloadSpeedMbps;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPendingMesure(boolean pendingMesure) {
        this.pendingMesure = pendingMesure;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUploadSpeedMbps(Double uploadSpeedMbps) {
        this.uploadSpeedMbps = uploadSpeedMbps;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResultModel [name=");
        builder.append(name);
        builder.append(", host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);
        builder.append(", downloadSpeedMbps=");
        builder.append(downloadSpeedMbps);
        builder.append(", uploadSpeedMbps=");
        builder.append(uploadSpeedMbps);
        builder.append(", active=");
        builder.append(active);
        builder.append("]");
        return builder.toString();
    }

}

/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core.events;

public class TestCompleteEvent {

    private String name;
    private String host;
    private int port;
    private Double downloadSpeedMbps;
    private Double uploadSpeedMbps;
    private String comment;

    public TestCompleteEvent(String name, String host, int port, Double downloadSpeedMbps, Double uploadSpeedMbps, String comment) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.downloadSpeedMbps = downloadSpeedMbps;
        this.uploadSpeedMbps = uploadSpeedMbps;
        this.comment = comment;
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

    public void setPort(int port) {
        this.port = port;
    }

    public void setUploadSpeedMbps(Double uploadSpeedMbps) {
        this.uploadSpeedMbps = uploadSpeedMbps;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TestCompleteEvent [name=");
        builder.append(name);
        builder.append(", host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);
        builder.append(", downloadSpeedMbps=");
        builder.append(downloadSpeedMbps);
        builder.append(", uploadSpeedMbps=");
        builder.append(uploadSpeedMbps);
        builder.append(", comment=");
        builder.append(comment);
        builder.append("]");
        return builder.toString();
    }

}

/*
    LANSpeedTest https://github.com/foilen/LANSpeedTest
    Copyright (C) 2016-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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

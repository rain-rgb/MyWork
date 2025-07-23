package com.trtm.iot.soslist.common;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {

    private String loginMessage = null;  // 用于存储登录消息
    private String deviceMessage = null;  // 用于存储设备消息

    // 构造函数：传入 WebSocket 服务器的 URI
    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    // 连接成功时的回调
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("连接成功！状态码：" + handshakedata.getHttpStatus());
    }

    // 收到消息时的回调
    @Override
    public void onMessage(String message) {
        // 假设根据不同的消息格式来区分
        if (message.contains("ma_login")) {
            this.loginMessage = message;  // 存储登录相关的消息
        } else if (message.contains("ma_get_active_devices")) {
            this.deviceMessage = message;  // 存储设备相关的消息
        }
        System.out.println("收到服务器消息：" + message);
    }

    // 连接关闭时的回调
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("连接关闭，原因：" + reason);
    }

    // 出现错误时的回调
    @Override
    public void onError(Exception ex) {
        System.err.println("发生错误：" + ex.getMessage());
    }

    // 发送消息
    public void sendMessage(String message) {
        if (this.isOpen()) {
            this.send(message);
            System.out.println("已发送消息：" + message);
        } else {
            System.out.println("连接未打开，无法发送消息");
        }
    }

    // 获取登录消息
    public String getLoginMessage() {
        return loginMessage;
    }

    // 获取设备消息
    public String getDeviceMessage() {
        return deviceMessage;
    }

}

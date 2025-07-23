package org.jeecg.modules.tcp;

import com.trtm.api.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {

    private final SocketPool socketPool;

    public TcpClient(SocketPool socketPool) {
        this.socketPool = socketPool;
    }

    public String sendCommand(String command) throws Exception {
        Socket socket = null;
        String msg ="";
        try {
            // 从连接池中获取 Socket
            socket = socketPool.borrowSocket();
            socket.setSoTimeout(5000); // 设置超时时间为 5 秒
            OutputStream outputStream = socket.getOutputStream();
            byte[] data = command.getBytes(StandardCharsets.UTF_8);
            outputStream.write(data);
            outputStream.flush();
            System.out.println("命令已发送到 TCP 服务器: " + command);

            // 4. 接收响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            msg = reader.readLine();
//            if(StringUtils.isBlank(msg)){
//                msg = "服务端未返回数据";
//            }
//            reader.close();
//            socket.getInputStream().close();

            // 读取服务端响应
            byte[] buffer = new byte[1024];
            int bytesRead = socket.getInputStream().read(buffer);
            if (bytesRead == -1) {
                msg = "服务端未返回数据";
               //  throw new IOException("服务端未返回数据");
            }
            msg = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("收到服务端响应: " + msg);

            return msg;
        } finally {
            // 将 Socket 返回连接池
            if (socket != null) {
                socketPool.returnSocket(socket);
            }
        }

    }
}

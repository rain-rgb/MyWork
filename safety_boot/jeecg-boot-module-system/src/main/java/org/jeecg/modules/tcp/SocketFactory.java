package org.jeecg.modules.tcp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;

public class SocketFactory extends BasePooledObjectFactory<Socket> {

    private final String serverAddress;
    private final int serverPort;

    public SocketFactory(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public Socket create() throws Exception {
        return new Socket(serverAddress, serverPort);
    }

    @Override
    public PooledObject<Socket> wrap(Socket socket) {
        return new DefaultPooledObject<>(socket);
    }

    @Override
    public void destroyObject(PooledObject<Socket> p) throws Exception {
        p.getObject().close(); // 关闭 Socket
    }

    @Override
    public boolean validateObject(PooledObject<Socket> p) {
        return p.getObject().isConnected() && !p.getObject().isClosed();
    }
}

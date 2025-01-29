package com.mine.myrpc.server.tcp;

import com.mine.myrpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {


    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx=Vertx.vertx();
        //创建TCP服务器
        NetServer server=vertx.createNetServer();

        //处理请求
        server.connectHandler(new TcpServerHandler());

        //启动TCP服务器并监听指定端口
        server.listen(port,netServerAsyncResult -> {
            if (netServerAsyncResult.succeeded()){
                System.out.println("TCP server start on port "+port);
            }else {
                System.err.println("Failed to start TCP server: "+netServerAsyncResult.cause());
            }
        });
    }

    private byte[] handleRequest(byte[] requestData) {
        return "Hello, TCP Client!".getBytes();
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}

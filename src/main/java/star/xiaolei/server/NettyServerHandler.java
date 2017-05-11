package star.xiaolei.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Created by 周高磊
 * Date: 2017/5/11.
 * Email: gaoleizhou@gmail.com
 * Desc:
 */
public class NettyServerHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 从Channel读取buffer
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        // 获得缓冲区可读的字节数
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("netty server receive request : " + body);
        String currentTime = "query time".equalsIgnoreCase(body) ? new Date(
                System.currentTimeMillis()).toString() : "bad request";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        // 性能考虑，仅将待发送的消息发送到缓冲数组中，再通过调用flush方法，写入channel中
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}

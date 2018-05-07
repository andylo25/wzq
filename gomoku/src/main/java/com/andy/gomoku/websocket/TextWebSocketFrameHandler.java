package com.andy.gomoku.websocket;



import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.action.IWebAction;
import com.andy.gomoku.game.Global;
import com.andy.gomoku.utils.CommonUtils;
import com.andy.gomoku.utils.GmAction;
import com.andy.gomoku.utils.JsonUtils;
import com.andy.gomoku.utils.SpringContextHolder;
import com.google.common.collect.Maps;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AttributeKey<NettySocketSession> nssKey = AttributeKey.valueOf("netty.channel.session.key");
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		// 初始化session
		NettySocketSession session = new NettySocketSession(ctx);
		ctx.channel().attr(nssKey).getAndSet(session);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Global.addSession(ctx.channel().attr(nssKey).get());
		super.channelActive(ctx);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		NettySocketSession session = ctx.channel().attr(nssKey).get();
		String message = msg.text();
		if(logger.isInfoEnabled()){
			logger.info("来自客户端消息:{}", message);
		}
		if(session.getUser() == null){
			logger.error("用户没有登录，请确认");
		}
		Map<String, Object> reqData = JsonUtils.json2Map(message);
		Integer action = MapUtils.getInteger(reqData, "action");
		if(action == null)return;
		IWebAction webAction = SpringContextHolder.getBean(GmAction.ACTION_PREFIX+action, IWebAction.class);
		Map<String, Object> data = (Map<String, Object>) reqData.get("data");
		if(data == null){
			data = Maps.newHashMap();
		}
		try {
			webAction.doAction(session,data);
		} catch (Exception e) {
			logger.error("执行逻辑异常", e);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 先退出房间
		NettySocketSession session = ctx.channel().attr(nssKey).get();
		CommonUtils.outRoom(session.getUser());
		Global.removeSession(session);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("Current channel handlerRemoved");
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("连接异常", cause);
	}
}
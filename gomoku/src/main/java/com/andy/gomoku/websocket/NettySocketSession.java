package com.andy.gomoku.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.gomoku.game.GameUser;
import com.andy.gomoku.utils.GoConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class NettySocketSession implements MySocketSession{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ChannelHandlerContext channelHandlerContext;
	
	public NettySocketSession() {
	}
	
	public NettySocketSession(ChannelHandlerContext ctx) {
		channelHandlerContext = ctx;
	}

	@Override
	public void setAttr(String name, Object value) {
		if(channelHandlerContext != null){
			channelHandlerContext.channel().attr(AttributeKey.valueOf(name)).getAndSet(value);
		}
	}

	@Override
	public Object getAttr(String name) {
		if(channelHandlerContext != null){
			return channelHandlerContext.channel().attr(AttributeKey.valueOf(name)).get();
		}
		return null;
	}

	@Override
	public Object removeAttr(String name) {
		return channelHandlerContext.channel().attr(AttributeKey.valueOf(name)).getAndRemove();
	}

	@Override
	public GameUser getUser() {
		return (GameUser) getAttr(GoConstant.USER_SESSION_KEY);
	}

	@Override
	public void sendMessage(String message) {
		if(channelHandlerContext == null || channelHandlerContext.isRemoved() || !channelHandlerContext.channel().isOpen())return;
		if(logger.isInfoEnabled()){
			logger.info("发送消息:{}", message);
		}
		channelHandlerContext.writeAndFlush(new TextWebSocketFrame(message));
	}

	@Override
	public void invalidate() {
		channelHandlerContext = null;
	}

}

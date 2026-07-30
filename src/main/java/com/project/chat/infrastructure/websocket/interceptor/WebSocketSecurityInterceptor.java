package com.project.chat.infrastructure.websocket.interceptor;

import com.project.chat.infrastructure.security.CustomUserDetailsService;
import com.project.chat.infrastructure.security.TokenService;
import com.project.chat.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private static final String ROOM_TOPIC_PREFIX = "/topic/rooms/";

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final RoomRepository roomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticateConnection(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        return message;
    }

    private void authenticateConnection(StompHeaderAccessor accessor) {
        List<String> authorization = accessor.getNativeHeader("Authorization");

        if (authorization == null || authorization.isEmpty()) {
            throw new IllegalArgumentException("JWT Token is required for WebSocket connection");
        }

        String header = authorization.get(0);
        if (!header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid WebSocket authorization header");
        }

        String email = tokenService.validateToken(header.substring(7));
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Invalid JWT Token in WebSocket connection");
        }

        UserDetails user = userDetailsService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        accessor.setUser(authentication);
    }

    private void authorizeSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null || !destination.startsWith(ROOM_TOPIC_PREFIX)) {
            return;
        }

        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new IllegalArgumentException("Unauthenticated WebSocket subscription");
        }

        UUID roomId = extractRoomId(destination);
        if (!roomRepository.existsByIdAndMembers_Email(roomId, principal.getName())) {
            throw new IllegalArgumentException("User is not authorized for this room");
        }
    }

    private UUID extractRoomId(String destination) {
        String roomPath = destination.substring(ROOM_TOPIC_PREFIX.length());
        int nextSeparator = roomPath.indexOf('/');
        String roomId = nextSeparator >= 0
                ? roomPath.substring(0, nextSeparator)
                : roomPath;

        try {
            return UUID.fromString(roomId);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid room destination", exception);
        }
    }
}
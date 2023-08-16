package com.tora.websocket;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebListener
public class WebSocketListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();
        session.setAttribute("remote_ip", sre.getServletRequest().getRemoteAddr());
        session.setAttribute("remote_port", sre.getServletRequest().getRemotePort());
    }
}


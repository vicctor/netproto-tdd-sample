/*
 * Copyright 2017 Artur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agendomat.net.tdd.sample;

import com.agendomat.net.tdd.sample.net.NetworkDataHandler;
import com.agendomat.net.tdd.sample.net.TimeoutProvider;
import com.agendomat.net.tdd.sample.protocol.MyProtocolNotificationsHandler;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * This test validates the simple message based protocol with tiny initial
 * negotiation. Let's assume that the protocol is symetric, so the client and
 * server are identical. Protocol is text-based, so all frames are sent as ASCII
 * encoded text messages.
 *
 * Protocol starts with version exchange message. Client and server sends the
 * version notification package having syntax: MYPROTO:VER:<VERSIONNUMBER>
 *
 * where VERSIONNUMBER is as string of 5 digits.
 *
 * After the application received version number, it must accept or reject peer
 * by responding with one of ACCEPT or REJECT message.
 *
 * After successful negotiation, the messages exchange starts.
 *
 * Each site can send at any time the the message frame of following syntax:
 * <FRAMTYPE>:<DATALENGTH>:<DATABODY>
 *
 * Where: FRAMETYPE is one char describing the message content type: S for
 * String, I for Image DATALENGTH is a length of DATABODY part of the message.
 * This field is formated as 5 digits string. DATABODY is a data, the size of
 * this field must match the DATALENGTH field
 *
 *
 * @author Artur
 */
public class MyProtocolEngineTest {

    private final static Charset CHARSET = Charset.forName("ASCII");
    private MyProtocolEngine engine;
    private NetworkDataHandler networkDataHandler;
    private TimeoutProvider timeoutProvider;
    private MyProtocolNotificationsHandler messagehandler;

    public MyProtocolEngineTest() {
    }

    @Before
    public void beforeTest() {
        engine = new MyProtocolEngine();

        networkDataHandler = mock(NetworkDataHandler.class);
        timeoutProvider = mock(TimeoutProvider.class);
        messagehandler = mock(MyProtocolNotificationsHandler.class);

        engine.setTimeoutProvider(timeoutProvider);
        engine.setNetworkDataHandler(networkDataHandler);
        engine.setMessageHandler(messagehandler);
    }

    @Test
    public void shall_successfulyNegotiateVersion() {
        // given
        byte[] versionFrame = "MYPROTO:VER:000001".getBytes(CHARSET);
        byte[] acceptFrame = "ACCEPT".getBytes(CHARSET);

        // when
        engine.onNetworkData(versionFrame);
        engine.onNetworkData(acceptFrame);

        // then
        byte[] expectedProtocolVersion = "MYPROTO:VER:000001".getBytes(CHARSET);
        byte[] expectedConfirmationMessage = "ACCEPT".getBytes(CHARSET);
        verify(networkDataHandler).onNetworkData(expectedProtocolVersion);
        verify(networkDataHandler).onNetworkData(expectedConfirmationMessage);
    }

    @Test
    public void shall_successfulyNegotiateVersionWithFramgentedInput() {
        // given
        String versionFrame = "MYPROTO:VER:000001";
        String acceptFrame = "ACCEPT";
        byte[] input = (versionFrame + acceptFrame).getBytes(CHARSET);

        // when
        for (byte b : input) {
            engine.onNetworkData(new byte[]{b});
        }

        // then
        byte[] expectedProtocolVersion = "MYPROTO:VER:000001".getBytes(CHARSET);
        byte[] expectedConfirmationMessage = "ACCEPT".getBytes(CHARSET);
        verify(networkDataHandler).onNetworkData(expectedProtocolVersion);
        verify(networkDataHandler).onNetworkData(expectedConfirmationMessage);
    }

    @Test
    public void shall_requestTimeput() {
        // given
        String versionFrameFragment = "M";
        byte[] input = (versionFrameFragment).getBytes(CHARSET);

        // when
        engine.onNetworkData(input);

        // then
        int expectedTimeout = 1000;
        verify(timeoutProvider).onTimeoutRequested(expectedTimeout);
    }
    
    @Test
    public void shall_notifyErrorOnTimeout() {
        // given
        String versionFrameFragment = "M";
        byte[] input = (versionFrameFragment).getBytes(CHARSET);

        // when
        engine.onNetworkData(input);
        engine.onNetworkTimeout();

        // then
        int expectedTimeout = 1000;
        verify(timeoutProvider).onTimeoutRequested(expectedTimeout);
        verify(messagehandler).onError();
    }
    
    @Test
    public void shall_notifyErrorOnInvalidHeadr() {
        // given
        String versionFrameFragment = "MYPROTO:VER:00000X";
        byte[] input = (versionFrameFragment).getBytes(CHARSET);

        // when
        engine.onNetworkData(input);
        
        // then
        verify(messagehandler).onError();
    }
    
    @Test
    public void shall_closeConnectionOnPeerStoppedListening() {
        // given

        // when
        engine.onPeerClosedConnection();
        
        // then
        verify(messagehandler).onEnd();
        verify(networkDataHandler).closeConnection();
    }
}

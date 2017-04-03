package com.agendomat.net.tdd.sample;

import com.agendomat.net.tdd.sample.net.NetworkAdapter;
import com.agendomat.net.tdd.sample.net.NetworkDataHandler;
import com.agendomat.net.tdd.sample.net.TimeoutProvider;
import com.agendomat.net.tdd.sample.protocol.ProtocolAdapter;
import com.agendomat.net.tdd.sample.protocol.MyProtocolNotificationsHandler;

/**
 *
 * @author Artur
 */
public class MyProtocolEngine implements NetworkAdapter, ProtocolAdapter {

    @Override
    public void setNetworkDataHandler(NetworkDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTimeoutProvider(TimeoutProvider provider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNetworkData(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNetworkTimeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNetworkError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onPeerClosedConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNetworkClosed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMessageHandler(MyProtocolNotificationsHandler handler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

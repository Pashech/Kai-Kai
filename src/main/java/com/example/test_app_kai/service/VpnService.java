package com.example.test_app_kai.service;

import java.io.IOException;
import java.util.List;

public interface VpnService {

    List<String> getList() throws IOException;

    void vpnStart(String location) throws IOException;

    void vpnStartRandom() throws IOException;

    String getVpnStatus() throws IOException;

    void vpnStop() throws IOException;

    void vpnClientRestart() throws IOException;
}

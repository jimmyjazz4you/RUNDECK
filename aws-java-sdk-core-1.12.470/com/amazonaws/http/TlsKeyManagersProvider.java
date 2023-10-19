package com.amazonaws.http;

import javax.net.ssl.KeyManager;

public interface TlsKeyManagersProvider {
   KeyManager[] getKeyManagers();
}

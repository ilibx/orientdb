package com.orientechnologies.orient.client.remote.message;

import java.io.IOException;

import com.orientechnologies.orient.client.binary.OChannelBinaryAsynchClient;
import com.orientechnologies.orient.client.remote.OBinaryRequest;
import com.orientechnologies.orient.client.remote.OStorageRemoteSession;
import com.orientechnologies.orient.enterprise.channel.binary.OChannelBinaryProtocol;

public class OCountRecordsRequest implements OBinaryRequest {
  @Override
  public void write(OChannelBinaryAsynchClient network, OStorageRemoteSession session, int mode) throws IOException {
  }

  @Override
  public byte getCommand() {
    return OChannelBinaryProtocol.REQUEST_DB_COUNTRECORDS;
  }
}
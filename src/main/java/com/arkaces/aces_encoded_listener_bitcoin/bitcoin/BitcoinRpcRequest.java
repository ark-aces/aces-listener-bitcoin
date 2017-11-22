package com.arkaces.aces_encoded_listener_bitcoin.bitcoin;

import lombok.Data;

import java.util.List;

@Data
public class BitcoinRpcRequest {
    private String jsonrpc = "1.0";
    private String id = "curltext";
    private String method;
    private List<Object> params;
}

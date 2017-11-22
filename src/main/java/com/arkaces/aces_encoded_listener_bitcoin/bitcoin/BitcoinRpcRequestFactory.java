package com.arkaces.aces_encoded_listener_bitcoin.bitcoin;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BitcoinRpcRequestFactory {

    public BitcoinRpcRequest create(String method) {
        return create(method, new ArrayList<>());
    }

    public BitcoinRpcRequest create(String method, List<Object> params) {
        BitcoinRpcRequest request = new BitcoinRpcRequest();
        request.setMethod(method);
        request.setParams(params);
        return request;
    }
}

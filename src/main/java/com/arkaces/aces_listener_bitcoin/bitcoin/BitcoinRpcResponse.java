package com.arkaces.aces_listener_bitcoin.bitcoin;

import lombok.Data;

@Data
public class BitcoinRpcResponse<T> {
    private T result;
    private Object error;
    private String id;
}

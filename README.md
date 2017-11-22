# ACES Bitcoin Encoded Listener

This service runs along side a bitcoin node and scans the blockchain
for new transactions to send to subscribers using the ACES 
Encoded Listener API.


## How it Works

This listener implementation connects to a locally running bitcoind instance
using the JSON-RPC client. 

Subscriptions specify a `minConfirmations` required for new transaction
notifications. Because bitcoind doesn't have a method for querying 
transactions across the blockchain using confirmations, this application
needs scan blocks recursively until the `maxScanBlockDepth` is reached.
Any new transactions that are found confirmed in each block will be 
posted to the corresponding listener subscriber.

1) get best block (longest chain)
2) iterate over transactions in best block (confirmations = 1)
3) if block depth < `maxScanBlockDepth`
   1) repeat from step 1 with previous block in chain


## Setup

Use `vagrant up` to create a VM for running bitcoin testnet locally.
To setup and start the bitcoin test node, run the commands in
`setup.sh`.



## Bitcoin JSON-RPC Requests

If `bitcoind` is running, you can make JSON-RPC requests to the 
backend using http requests. The configuration information is in
`~/.bitcoin/bitcoin.conf`. See [setup.sh](setup.sh) for more
information.

Example JSON-RPC call to bitcoind backend:

```shell
curl --data-binary '{
  "jsonrpc": "1.0",
  "id": "curltext",
  "method": "getinfo",
  "params":[]
}' \
-H 'content-type:text/plain;' \
-u 'bitcoinrpc:change_this_to_a_long_random_password' \
http://localhost:18332/
```

This application uses JSON-RPC calls to query bitcoin 
transaction information.


## Example Usage

Consumers register their Http callback endpoint by posting to the `subscriptions`
endpoint. Immediately following successful a subscription the encoded
listener will send all new Bitcoin transactions to the registered callback
URL.

```bash
curl -X POST 'localhost:8080/subscriptions' \
-H 'Content-type: application/json' \
-d '{
  "callbackUrl": "http://localhost:8080/event-logger",
  "minConfirmations": 5
}'
```

```json
{
  "identifier" : "TwpEVgS64WKG4WalMgBk",
  "callbackUrl" : "http://localhost:8080/event-logger",
  "createdAt" : "2017-10-24T04:15:17.091Z"
}
```

```
2017-11-22 00:25:27.197  INFO 10796 --- [nio-8080-exec-1] c.a.a.event.EventLogController           : {
  "data" : "{\"txid\":\"d89fd69d29212acd072336540b9d2f14eedd26ba7f17cdca89492ce17533dccd\",\"hash\":\"d89fd69d29212acd072336540b9d2f14eedd26ba7f17cdca89492ce17533dccd\",\"version\":1,\"size\":480,\"vsize\":480,\"locktime\":0,\"vin\":[{\"txid\":\"079aaf4864d1b0e16ac7c42bdbccccff91971b49090fa85ee2c3cd5d42eca6ae\",\"vout\":0,\"scriptSig\":{\"asm\":\"0 3045022100815637f921226887d5613a06e0b6f46857ac28e92e396464a127659638a6a9d50220564aed0aea388873ae5c0c63c8192720f1270c4049efb7b80ac901a289d2b984[ALL] 304402201745389eed9c25392eec2e75a39e25a5df056d6ac687e2088c82260a63f0a4d6022026fae35b27acaf0db43b3dc86b3c93947da0d389d754fd36f6d86e9f5e84653d[ALL] 1 63522102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249b2102bbfc41899e132965f3a7a4a7424def7e8f62d992ff108fd231131b772890da9f52ae6763a9148fa0b349e0d62f0bb442f9c4ddf0d6c7c8297b13882102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac67020004b2752102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac6868\",\"hex\":\"00483045022100815637f921226887d5613a06e0b6f46857ac28e92e396464a127659638a6a9d50220564aed0aea388873ae5c0c63c8192720f1270c4049efb7b80ac901a289d2b9840147304402201745389eed9c25392eec2e75a39e25a5df056d6ac687e2088c82260a63f0a4d6022026fae35b27acaf0db43b3dc86b3c93947da0d389d754fd36f6d86e9f5e84653d01514caf63522102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249b2102bbfc41899e132965f3a7a4a7424def7e8f62d992ff108fd231131b772890da9f52ae6763a9148fa0b349e0d62f0bb442f9c4ddf0d6c7c8297b13882102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac67020004b2752102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac6868\"},\"sequence\":4294967295}],\"vout\":[{\"value\":0.00230988,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_HASH160 4d611eb8d63a3fb003ded25297f348270b46e56b OP_EQUAL\",\"hex\":\"a9144d611eb8d63a3fb003ded25297f348270b46e56b87\",\"reqSigs\":1,\"type\":\"scripthash\",\"addresses\":[\"2MzJNN56FaWmqqpYAx61gmtLJqyqzcePPvh\"]}},{\"value\":0.0,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_RETURN cab67fe800055781131ce3a7a70a5f191ccb3a59e3d3206722256459\",\"hex\":\"6a1ccab67fe800055781131ce3a7a70a5f191ccb3a59e3d3206722256459\",\"type\":\"nulldata\"}},{\"value\":0.00230988,\"n\":2,\"scriptPubKey\":{\"asm\":\"OP_HASH160 e83c151d36ed30c56564c0e7d367558f28206e50 OP_EQUAL\",\"hex\":\"a914e83c151d36ed30c56564c0e7d367558f28206e5087\",\"reqSigs\":1,\"type\":\"scripthash\",\"addresses\":[\"2NERAjPhwy7txawMEbgzBxKL5KFpj977Afg\"]}}],\"hex\":\"0100000001aea6ec425dcdc3e25ea80f09491b9791ffccccdb2bc4c76ae1b0d16448af9a0700000000fd440100483045022100815637f921226887d5613a06e0b6f46857ac28e92e396464a127659638a6a9d50220564aed0aea388873ae5c0c63c8192720f1270c4049efb7b80ac901a289d2b9840147304402201745389eed9c25392eec2e75a39e25a5df056d6ac687e2088c82260a63f0a4d6022026fae35b27acaf0db43b3dc86b3c93947da0d389d754fd36f6d86e9f5e84653d01514caf63522102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249b2102bbfc41899e132965f3a7a4a7424def7e8f62d992ff108fd231131b772890da9f52ae6763a9148fa0b349e0d62f0bb442f9c4ddf0d6c7c8297b13882102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac67020004b2752102e4e9fac7e9641c481276e350f88252ead47c2f937e2d060cf7c1559d731c249bac6868ffffffff034c8603000000000017a9144d611eb8d63a3fb003ded25297f348270b46e56b8700000000000000001e6a1ccab67fe800055781131ce3a7a70a5f191ccb3a59e3d32067222564594c8603000000000017a914e83c151d36ed30c56564c0e7d367558f28206e508700000000\",\"blockhash\":\"00000000000031e374763b6855cdde678d5c8cf7ba1817f680c0afe28c23f2a0\",\"confirmations\":1,\"time\":1511338725,\"blocktime\":1511338725}",
  "createdAt" : "2017-11-22T08:25:27.194Z"
}
2017-11-22 00:25:27.201  INFO 10796 --- [nio-8080-exec-2] c.a.a.event.EventLogController           : {
  "data" : "{\"txid\":\"76a353d92ef3177b6ea52981dbe7850e165963fa1468030b08edaad63c043b8b\",\"hash\":\"76a353d92ef3177b6ea52981dbe7850e165963fa1468030b08edaad63c043b8b\",\"version\":2,\"size\":225,\"vsize\":225,\"locktime\":1231284,\"vin\":[{\"txid\":\"1feaaa5370dfc7cc2dcd66df85abc677995ac7c6dee3e773bfd63a6f8ffe9d36\",\"vout\":0,\"scriptSig\":{\"asm\":\"3044022054d890ed094e3ebdf5993d3ad80d30716b7fc288d0738dc8f8aa3048c54945b402206729b09b8fdc38fa4044c25f781f131d09010692dff49a81423bd59f3957ea45[ALL] 025bad200f79ae26515266b51318e69340819adcd3d63b9249f0350fbd0c74ec08\",\"hex\":\"473044022054d890ed094e3ebdf5993d3ad80d30716b7fc288d0738dc8f8aa3048c54945b402206729b09b8fdc38fa4044c25f781f131d09010692dff49a81423bd59f3957ea450121025bad200f79ae26515266b51318e69340819adcd3d63b9249f0350fbd0c74ec08\"},\"sequence\":4294967294}],\"vout\":[{\"value\":4.3528367,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 51049132849221245708ad163a9843c07256203e OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a91451049132849221245708ad163a9843c07256203e88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mnuLVvd1EyBzRLTCUJsRZaugsi2mcpbhWH\"]}},{\"value\":1.8259E-4,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 5bf797f15b5e1add5ffd22cb670c5cfe9c8c9e2a OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9145bf797f15b5e1add5ffd22cb670c5cfe9c8c9e2a88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mouEPcMf6XnGFqZtnVnmWqu4CxQNibr7nH\"]}}],\"hex\":\"0200000001369dfe8f6f3ad6bf73e7e3dec6c75a9977c6ab85df66cd2dccc7df7053aaea1f000000006a473044022054d890ed094e3ebdf5993d3ad80d30716b7fc288d0738dc8f8aa3048c54945b402206729b09b8fdc38fa4044c25f781f131d09010692dff49a81423bd59f3957ea450121025bad200f79ae26515266b51318e69340819adcd3d63b9249f0350fbd0c74ec08feffffff02d6e6f119000000001976a91451049132849221245708ad163a9843c07256203e88ac53470000000000001976a9145bf797f15b5e1add5ffd22cb670c5cfe9c8c9e2a88acb4c91200\",\"blockhash\":\"00000000000031e374763b6855cdde678d5c8cf7ba1817f680c0afe28c23f2a0\",\"confirmations\":1,\"time\":1511338725,\"blocktime\":1511338725}",
  "createdAt" : "2017-11-22T08:25:27.198Z"
}
2017-11-22 00:25:27.204  INFO 10796 --- [nio-8080-exec-3] c.a.a.event.EventLogController           : {
  "data" : "{\"txid\":\"8581a4e48e6e8f7e8693ee60b31a32be8dcefcee80ae35f05ba5a32238f60b71\",\"hash\":\"8581a4e48e6e8f7e8693ee60b31a32be8dcefcee80ae35f05ba5a32238f60b71\",\"version\":1,\"size\":225,\"vsize\":225,\"locktime\":0,\"vin\":[{\"txid\":\"f141bfdabd562e0e317e247c43a0532cb42286b3d047060290e8fb5be709bcab\",\"vout\":1,\"scriptSig\":{\"asm\":\"3044022051d6e6c72f976bc7f63119b926d01dce89b5da519126607edbd47ac123fc891d02203df45bbe69f5a0c0ba57d41d0ed49c6e5436b8e00b6e7f363dfc1456c5c7e6d3[ALL] 03bb318b00de944086fad67ab78a832eb1bf26916053ecd3b14a3f48f9fbe0821f\",\"hex\":\"473044022051d6e6c72f976bc7f63119b926d01dce89b5da519126607edbd47ac123fc891d02203df45bbe69f5a0c0ba57d41d0ed49c6e5436b8e00b6e7f363dfc1456c5c7e6d3012103bb318b00de944086fad67ab78a832eb1bf26916053ecd3b14a3f48f9fbe0821f\"},\"sequence\":4294967295}],\"vout\":[{\"value\":2.5E-4,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 e6ce90aa1eb0015cb21e5e619382372625147aa0 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a914e6ce90aa1eb0015cb21e5e619382372625147aa088ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"n2ZMAZz1wRxqmcz7Ydv65GX4X3oNjFUJ9g\"]}},{\"value\":4.30814559,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 8e8c1d4adef86c11154fd04b5012306715fd4baf OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9148e8c1d4adef86c11154fd04b5012306715fd4baf88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mtWg6ccLiZWw2Et7E5UqmHsYgrAi5wqiov\"]}}],\"hex\":\"0100000001abbc09e75bfbe890020647d0b38622b42c53a0437c247e310e2e56bddabf41f1010000006a473044022051d6e6c72f976bc7f63119b926d01dce89b5da519126607edbd47ac123fc891d02203df45bbe69f5a0c0ba57d41d0ed49c6e5436b8e00b6e7f363dfc1456c5c7e6d3012103bb318b00de944086fad67ab78a832eb1bf26916053ecd3b14a3f48f9fbe0821fffffffff02a8610000000000001976a914e6ce90aa1eb0015cb21e5e619382372625147aa088ac5fb5ad19000000001976a9148e8c1d4adef86c11154fd04b5012306715fd4baf88ac00000000\",\"blockhash\":\"00000000000031e374763b6855cdde678d5c8cf7ba1817f680c0afe28c23f2a0\",\"confirmations\":1,\"time\":1511338725,\"blocktime\":1511338725}",
  "createdAt" : "2017-11-22T08:25:27.202Z"
}
...
```
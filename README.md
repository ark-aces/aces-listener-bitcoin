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
2017-11-22 00:09:07.981  INFO 9979 --- [nio-8080-exec-1] c.a.a.event.EventLogController           : {
  "data" : "{\"result\":{\"txid\":\"105038186ba15f84c8795ce5b2c7cd8aa227743d755a0c07580bee547a3a47eb\",\"hash\":\"e19f943c57fef17df4f955988a7740416dc73b1d2dfc09314c46a3623a1ada0c\",\"version\":1,\"size\":308,\"vsize\":281,\"locktime\":0,\"vin\":[{\"coinbase\":\"03b4c912102f5669614254432f4542312f4144362f1041016500a43aef9e23e11dd3608e0000\",\"sequence\":4294967295}],\"vout\":[{\"value\":0.31705315,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 13b1fb8dd87e3be1c72b5c0c6c1eaebb63c1dd9a OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a91413b1fb8dd87e3be1c72b5c0c6c1eaebb63c1dd9a88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mhK6M9bKFJymoCikrBNQD1EB72de1vbhVG\"]}},{\"value\":0.6341063,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 2cd699d8196e6089a9e79ff1af81d364e5b629c0 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9142cd699d8196e6089a9e79ff1af81d364e5b629c088ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mjc37EJiu8Bjzia6dGA4Ao4ifVDJTNN9aW\"]}},{\"value\":0.47557972,\"n\":2,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 2e0c2320164ab0209b2e3ed82822637afa5c2193 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9142e0c2320164ab0209b2e3ed82822637afa5c219388ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mjiRvBkmauzphEDeVVEa14PFk51nxyX5fP\"]}},{\"value\":0.15852658,\"n\":3,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 79c3fc60f189f90e82bb3b6ef1371a986c98f41a OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a91479c3fc60f189f90e82bb3b6ef1371a986c98f41a88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mrcnqbqJaWqgRCwBWbJcSYhC8R4LbRNfwB\"]}},{\"value\":0.0,\"n\":4,\"scriptPubKey\":{\"asm\":\"OP_RETURN aa21a9eddc20eb74fea520b13feaae74eca59b672726f30a9b0314244d44e719771659f5\",\"hex\":\"6a24aa21a9eddc20eb74fea520b13feaae74eca59b672726f30a9b0314244d44e719771659f5\",\"type\":\"nulldata\"}}],\"hex\":\"010000000001010000000000000000000000000000000000000000000000000000000000000000ffffffff2603b4c912102f5669614254432f4542312f4144362f1041016500a43aef9e23e11dd3608e0000ffffffff05e3c8e301000000001976a91413b1fb8dd87e3be1c72b5c0c6c1eaebb63c1dd9a88acc691c703000000001976a9142cd699d8196e6089a9e79ff1af81d364e5b629c088ac54add502000000001976a9142e0c2320164ab0209b2e3ed82822637afa5c219388ac72e4f100000000001976a91479c3fc60f189f90e82bb3b6ef1371a986c98f41a88ac0000000000000000266a24aa21a9eddc20eb74fea520b13feaae74eca59b672726f30a9b0314244d44e719771659f50120000000000000000000000000000000000000000000000000000000000000000000000000\",\"blockhash\":\"00000000000004d5f546429f37104d1df1d3db8c40070414274669a0694be87e\",\"confirmations\":1,\"time\":1511337953,\"blocktime\":1511337953},\"error\":null,\"id\":\"curltext\"}",
  "createdAt" : "2017-11-22T08:09:07.977Z"
}
2017-11-22 00:09:07.985  INFO 9979 --- [nio-8080-exec-2] c.a.a.event.EventLogController           : {
  "data" : "{\"result\":{\"txid\":\"48b1e12a57ca974bc51a1e95486764531874d2d11c707e634e8b91080c4f87c2\",\"hash\":\"b056f16d4d29e93fb7b68e11128aee4cdf71c7e494a59422953052bff3cb157a\",\"version\":1,\"size\":405,\"vsize\":214,\"locktime\":0,\"vin\":[{\"txid\":\"3c457ad76cb3308aab22e6ea84ef9fbcb68c10d521d386e4f1078f2292ed7ad6\",\"vout\":1,\"scriptSig\":{\"asm\":\"002044269c7c1125e6bd6015a3828ea4941221d6f5a0655048201f64f2a8e0b050db\",\"hex\":\"22002044269c7c1125e6bd6015a3828ea4941221d6f5a0655048201f64f2a8e0b050db\"},\"txinwitness\":[\"\",\"3044022075001712680d86ae56a4f3bc1f9e484cf7fd11a941d760b8a11dc8a8537952210220484514aeb5db7c2ffe11da67a0b0a7e5276d34663fe1d1e558438ca28ffff01201\",\"3045022100ce50466626237f04fff339d4aa1dbe97aa352baff08cfdd88418d884c97f4f1f02206856d945dd584fa7103f8ecf4513e6828ea5eedfe38249bb4819c94b5709079c01\",\"522102cd9dff571726a13ddb65efd9e2ab693de7a2d7e6591a6977e0469a8d4829b295210333729091c8694899483d1bdfa60dcee2af61e21cf46a9e134d0549139239c1af2102eb13e74ccf7ce10ee9f61fb65cd7ba43d108857c0e38fc6e178c3e5ab203ac9a53ae\"],\"sequence\":4294967295}],\"vout\":[{\"value\":92.96504043,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_HASH160 bf2bea9d25d43adf66c7dfb0aac52c6f53652540 OP_EQUAL\",\"hex\":\"a914bf2bea9d25d43adf66c7dfb0aac52c6f5365254087\",\"reqSigs\":1,\"type\":\"scripthash\",\"addresses\":[\"2NAg3fHvye5jTS1KJUQMjZKKX2i6HTp2XGD\"]}},{\"value\":0.1199,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_HASH160 84667a0f2ed489762d23761e1b71cd6428bf0a5d OP_EQUAL\",\"hex\":\"a91484667a0f2ed489762d23761e1b71cd6428bf0a5d87\",\"reqSigs\":1,\"type\":\"scripthash\",\"addresses\":[\"2N5KHwpQBXiVF4nQGPWyG71U93TCyQeH7Mw\"]}}],\"hex\":\"01000000000101d67aed92228f07f1e486d321d5108cb6bc9fef84eae622ab8a30b36cd77a453c010000002322002044269c7c1125e6bd6015a3828ea4941221d6f5a0655048201f64f2a8e0b050dbffffffff02eb641d2a0200000017a914bf2bea9d25d43adf66c7dfb0aac52c6f5365254087f0f3b6000000000017a91484667a0f2ed489762d23761e1b71cd6428bf0a5d870400473044022075001712680d86ae56a4f3bc1f9e484cf7fd11a941d760b8a11dc8a8537952210220484514aeb5db7c2ffe11da67a0b0a7e5276d34663fe1d1e558438ca28ffff01201483045022100ce50466626237f04fff339d4aa1dbe97aa352baff08cfdd88418d884c97f4f1f02206856d945dd584fa7103f8ecf4513e6828ea5eedfe38249bb4819c94b5709079c0169522102cd9dff571726a13ddb65efd9e2ab693de7a2d7e6591a6977e0469a8d4829b295210333729091c8694899483d1bdfa60dcee2af61e21cf46a9e134d0549139239c1af2102eb13e74ccf7ce10ee9f61fb65cd7ba43d108857c0e38fc6e178c3e5ab203ac9a53ae00000000\",\"blockhash\":\"00000000000004d5f546429f37104d1df1d3db8c40070414274669a0694be87e\",\"confirmations\":1,\"time\":1511337953,\"blocktime\":1511337953},\"error\":null,\"id\":\"curltext\"}",
  "createdAt" : "2017-11-22T08:09:07.982Z"
}
2017-11-22 00:09:07.988  INFO 9979 --- [nio-8080-exec-3] c.a.a.event.EventLogController           : {
  "data" : "{\"result\":{\"txid\":\"63b439aea5f3a07186acefe79266cf48137a10e8f19942d7280b655d1c580d10\",\"hash\":\"63b439aea5f3a07186acefe79266cf48137a10e8f19942d7280b655d1c580d10\",\"version\":1,\"size\":372,\"vsize\":372,\"locktime\":0,\"vin\":[{\"txid\":\"eb2f1db031c7969224321d22008cd318c2e823438c7514b2377819ec629df4c2\",\"vout\":1,\"scriptSig\":{\"asm\":\"0 3045022100d171eacd1169217ecc233a3c458d9c31dfc53a631e809ed6d0dc8394681e638b0220681363f21f395f83e4b35660dd8c18058cd06601fcee1b0ce7e6e295503d4077[ALL] 30440220065acb07f799bcf8078392acdf04d78c5b920b713e6b485d54bc67d1adb5107902203735c9520521dd139244bff0243ebfa775d00d864569ebac50d54c2880b24c2f[ALL] 5221033520a038939787260bc1ff6ff80960229dfcdd925b183d9c5e7e2c75f057f4b421037e0d226b30ffccf9e94381d34faaa6c24677ae3c45d542fbd49115ea75e707db21024aee332fd595526741b7a5a253a385a37ac8c77607769d3bc3b09b15bf3ec68853ae\",\"hex\":\"00483045022100d171eacd1169217ecc233a3c458d9c31dfc53a631e809ed6d0dc8394681e638b0220681363f21f395f83e4b35660dd8c18058cd06601fcee1b0ce7e6e295503d4077014730440220065acb07f799bcf8078392acdf04d78c5b920b713e6b485d54bc67d1adb5107902203735c9520521dd139244bff0243ebfa775d00d864569ebac50d54c2880b24c2f014c695221033520a038939787260bc1ff6ff80960229dfcdd925b183d9c5e7e2c75f057f4b421037e0d226b30ffccf9e94381d34faaa6c24677ae3c45d542fbd49115ea75e707db21024aee332fd595526741b7a5a253a385a37ac8c77607769d3bc3b09b15bf3ec68853ae\"},\"sequence\":4294967295}],\"vout\":[{\"value\":0.001,\"n\":0,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 25c9bd6dff69eb0321c09554ce8db2eb757c0bce OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a91425c9bd6dff69eb0321c09554ce8db2eb757c0bce88ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"mixkyHmR9uJZgQYXwgFzAK4GGXkVuhzzJ9\"]}},{\"value\":0.09609963,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_HASH160 c3b62ffc733890e7f8ad9d4de975ec1af1f48d80 OP_EQUAL\",\"hex\":\"a914c3b62ffc733890e7f8ad9d4de975ec1af1f48d8087\",\"reqSigs\":1,\"type\":\"scripthash\",\"addresses\":[\"2NB641J9pLWUSRTzwhrHryYfFb7wWCEeg1o\"]}}],\"hex\":\"0100000001c2f49d62ec197837b214758c4323e8c218d38c00221d32249296c731b01d2feb01000000fdfd0000483045022100d171eacd1169217ecc233a3c458d9c31dfc53a631e809ed6d0dc8394681e638b0220681363f21f395f83e4b35660dd8c18058cd06601fcee1b0ce7e6e295503d4077014730440220065acb07f799bcf8078392acdf04d78c5b920b713e6b485d54bc67d1adb5107902203735c9520521dd139244bff0243ebfa775d00d864569ebac50d54c2880b24c2f014c695221033520a038939787260bc1ff6ff80960229dfcdd925b183d9c5e7e2c75f057f4b421037e0d226b30ffccf9e94381d34faaa6c24677ae3c45d542fbd49115ea75e707db21024aee332fd595526741b7a5a253a385a37ac8c77607769d3bc3b09b15bf3ec68853aeffffffff02a0860100000000001976a91425c9bd6dff69eb0321c09554ce8db2eb757c0bce88aceba292000000000017a914c3b62ffc733890e7f8ad9d4de975ec1af1f48d808700000000\",\"blockhash\":\"00000000000004d5f546429f37104d1df1d3db8c40070414274669a0694be87e\",\"confirmations\":1,\"time\":1511337953,\"blocktime\":1511337953},\"error\":null,\"id\":\"curltext\"}",
  "createdAt" : "2017-11-22T08:09:07.986Z"
}
2017-11-22 00:09:07.992  INFO 9979 --- [nio-8080-exec-4] c.a.a.event.EventLogController           : {
  "data" : "{\"result\":{\"txid\":\"320d71c7b47ffbb13a99cb83d4917c4aaa6b28ad794de2a6a2ff5eb562d62e93\",\"hash\":\"320d71c7b47ffbb13a99cb83d4917c4aaa6b28ad794de2a6a2ff5eb562d62e93\",\"version\":1,\"size\":381,\"vsize\":381,\"locktime\":0,\"vin\":[{\"txid\":\"1fb5452dee75cd1860d6d6fc583afa5878f04b7562e186a10ef88f1e90a54748\",\"vout\":1,\"scriptSig\":{\"asm\":\"3044022010f739eb54cc396b7587099fbdbf84e220b2b37a6a4ead8de49ed73e2d60c90502206c9a1a513ea412cdd66e29daf17d1eef489d3f349b911f7471b25750a98512a6[ALL] 023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8\",\"hex\":\"473044022010f739eb54cc396b7587099fbdbf84e220b2b37a6a4ead8de49ed73e2d60c90502206c9a1a513ea412cdd66e29daf17d1eef489d3f349b911f7471b25750a98512a60121023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8\"},\"sequence\":4294967295},{\"txid\":\"08103bf7c6bfc7bbf776ef5be56e3f875f559bf08caf66c27fea29faf39e2621\",\"vout\":0,\"scriptSig\":{\"asm\":\"30440220731f87e6a213ea01c861e166b1f90f24f99fd42b38c14e41ca45f362161e89ea022002a9dbaf3fce6db6c3962a9067a20f3b5f14d9fdce01616d792b7f9355e83253[ALL] 023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8\",\"hex\":\"4730440220731f87e6a213ea01c861e166b1f90f24f99fd42b38c14e41ca45f362161e89ea022002a9dbaf3fce6db6c3962a9067a20f3b5f14d9fdce01616d792b7f9355e832530121023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8\"},\"sequence\":4294967295}],\"vout\":[{\"value\":0.75362395,\"n\":0,\"scriptPubKey\":{\"asm\":\"0 85d1b0167c3657e69242ed9d2b15f979395e109fe82c4033ac6e94defc0c9cf1\",\"hex\":\"002085d1b0167c3657e69242ed9d2b15f979395e109fe82c4033ac6e94defc0c9cf1\",\"type\":\"witness_v0_scripthash\"}},{\"value\":0.083236,\"n\":1,\"scriptPubKey\":{\"asm\":\"OP_DUP OP_HASH160 5756cf02cec2d5563cf5b43cff21f5b189bcd426 OP_EQUALVERIFY OP_CHECKSIG\",\"hex\":\"76a9145756cf02cec2d5563cf5b43cff21f5b189bcd42688ac\",\"reqSigs\":1,\"type\":\"pubkeyhash\",\"addresses\":[\"moUm5K7T2953LEvJ7y8UUwZhtfd3WgtVWP\"]}}],\"hex\":\"01000000024847a5901e8ff80ea186e162754bf07858fa3a58fcd6d66018cd75ee2d45b51f010000006a473044022010f739eb54cc396b7587099fbdbf84e220b2b37a6a4ead8de49ed73e2d60c90502206c9a1a513ea412cdd66e29daf17d1eef489d3f349b911f7471b25750a98512a60121023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8ffffffff21269ef3fa29ea7fc266af8cf09b555f873f6ee55bef76f7bbc7bfc6f73b1008000000006a4730440220731f87e6a213ea01c861e166b1f90f24f99fd42b38c14e41ca45f362161e89ea022002a9dbaf3fce6db6c3962a9067a20f3b5f14d9fdce01616d792b7f9355e832530121023d638065cec250a2ab6ac96264d7c43b36dc3d64afbe02fdf9ad4b60b90aa2a8ffffffff025bf07d040000000022002085d1b0167c3657e69242ed9d2b15f979395e109fe82c4033ac6e94defc0c9cf110027f00000000001976a9145756cf02cec2d5563cf5b43cff21f5b189bcd42688ac00000000\",\"blockhash\":\"00000000000004d5f546429f37104d1df1d3db8c40070414274669a0694be87e\",\"confirmations\":1,\"time\":1511337953,\"blocktime\":1511337953},\"error\":null,\"id\":\"curltext\"}",
  "createdAt" : "2017-11-22T08:09:07.990Z"
}
...
```
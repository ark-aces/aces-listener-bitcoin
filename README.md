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

#!/bin/bash

sudo apt-add-repository ppa:bitcoin/bitcoin
sudo apt-get update
sudo apt-get install bitcoind

cat > /home/vagrant/.bitcoin/bitcoin.conf <<EOF
rpcuser=bitcoinrpc
rpcpassword=change_this_to_a_long_random_password
testnet=1
rpcallowip=0.0.0.0/0
EOF

bitcoind -daemon -testnet -txindex=1

// execute bitcoin commands:
bitcoin-cli -testnet getblockcount
bitcoin-cli -testnet listtransactions

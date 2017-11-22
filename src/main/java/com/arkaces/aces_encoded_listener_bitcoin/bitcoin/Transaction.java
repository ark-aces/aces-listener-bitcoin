package com.arkaces.aces_encoded_listener_bitcoin.bitcoin;

import lombok.Data;

@Data
public class Transaction {
    private String txid;
}

/*
bitcoin-cli -testnet getrawtransaction 0db1fb519393409cc1afcaae90aed0e5ec031a68ce3af686c4be4c2de9caa90e 1
{
  "txid": "0db1fb519393409cc1afcaae90aed0e5ec031a68ce3af686c4be4c2de9caa90e",
  "hash": "0db1fb519393409cc1afcaae90aed0e5ec031a68ce3af686c4be4c2de9caa90e",
  "version": 1,
  "size": 224,
  "vsize": 224,
  "locktime": 0,
  "vin": [
    {
      "txid": "a8d6121cfcdfb5a739998cf0d3630d252a95efe32294b5b79509e6c09eb57de7",
      "vout": 0,
      "scriptSig": {
        "asm": "3045022100d5cb968aa8996cb269c6d8761a7132f0d36ed39eb90fe2ea04837c912dba3ccb022019aeaab292527449d86c1a34d861c88f2ba8b81f5801215c79d1872940ae73a6[ALL] 0367f1d7d00c85dba1c509e43a3d2b3118fd3b1d6c522772de8ef46ed9f239ecda",
        "hex": "483045022100d5cb968aa8996cb269c6d8761a7132f0d36ed39eb90fe2ea04837c912dba3ccb022019aeaab292527449d86c1a34d861c88f2ba8b81f5801215c79d1872940ae73a601210367f1d7d00c85dba1c509e43a3d2b3118fd3b1d6c522772de8ef46ed9f239ecda"
      },
      "sequence": 4294967295
    }
  ],
  "vout": [
    {
      "value": 0.24000000,
      "n": 0,
      "scriptPubKey": {
        "asm": "OP_HASH160 8ec4a2085975dc2c6c5680e3828241d2f50c7f5d OP_EQUAL",
        "hex": "a9148ec4a2085975dc2c6c5680e3828241d2f50c7f5d87",
        "reqSigs": 1,
        "type": "scripthash",
        "addresses": [
          "2N6G7VpWujg7L6RvFoHShw7xfa134tHjAQb"
        ]
      }
    },
    {
      "value": 0.45998526,
      "n": 1,
      "scriptPubKey": {
        "asm": "OP_DUP OP_HASH160 2958c0c31ad6e14d65fed009e09d994bdc4ede1f OP_EQUALVERIFY OP_CHECKSIG",
        "hex": "76a9142958c0c31ad6e14d65fed009e09d994bdc4ede1f88ac",
        "reqSigs": 1,
        "type": "pubkeyhash",
        "addresses": [
          "mjHaKapuf1tqXy1PAS6auWBDtaQReH9AoQ"
        ]
      }
    }
  ],
  "hex": "0100000001e77db59ec0e60995b7b59422e3ef952a250d63d3f08c9939a7b5dffc1c12d6a8000000006b483045022100d5cb968aa8996cb269c6d8761a7132f0d36ed39eb90fe2ea04837c912dba3ccb022019aeaab292527449d86c1a34d861c88f2ba8b81f5801215c79d1872940ae73a601210367f1d7d00c85dba1c509e43a3d2b3118fd3b1d6c522772de8ef46ed9f239ecdaffffffff0200366e010000000017a9148ec4a2085975dc2c6c5680e3828241d2f50c7f5d87bee1bd02000000001976a9142958c0c31ad6e14d65fed009e09d994bdc4ede1f88ac00000000",
  "blockhash": "0000000017bedf2b3a0c2315c9ff723c09af420bcc4998c56aac9d4290214e44",
  "confirmations": 40,
  "time": 1511253453,
  "blocktime": 1511253453
}

 */

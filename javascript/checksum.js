// Copyright (c) 2019-2022 The Gim.Cool developers
// All Rights Reserved.
// NOTICE: All information contained herein is, and remains
// the property of Gim.Cool and its suppliers,
// if any. The intellectual and technical concepts contained
// herein are proprietary to Gim.Cool
// Dissemination of this information or reproduction of this materia
// is strictly forbidden unless prior written permission is obtained
// from Gim.Cool.

const crypto = require('crypto');

function buildRequestChecksum(params, secret, t, r) {
  const p = params || [];
  p.push(`t=${t}`, `r=${r}`);
  p.sort();
  p.push(`secret=${secret}`);
  return crypto.createHash('sha256').update(p.join('&')).digest('hex');
}

function buildCallbackChecksum(payload) {
  const buff = Buffer.from(crypto.createHash('sha256').update(payload).digest());
  return buff.toString('base64').replace(/\+/g, "-").replace(/\//g, "_");
}


function example_GET_request_checksum() {
  // calculate the checksum for API [GET] /v1/sofa/wallets/689664/notifications
  //   query:
  //     from_time=1561651200
  //     to_time=1562255999
  //     type=2
  //   body: none
  //
  // final API URL should be /v1/sofa/wallets/689664/notifications?from_time=1561651200&to_time=1562255999&type=2&t=1629346605&r=RANDOM_STRING

    // params contains all query strings and post body if any
    const params = ['from_time=1561651200', 'to_time=1562255999', 'type=2'];

    const curTime = 1629346605; // replace with current time, ex: Math.floor(Date.now()/1000);
    const checksum = buildRequestChecksum(params, 'API_SECRET', curTime, 'RANDOM_STRING');

    console.log(checksum);
}

function example_POST_request_checksum() {
  // calculate the checksum for API [POST] /v1/sofa/wallets/689664/autofee
  //   query: none
  //   body: {"block_num":1}
  //
  // final API URL should be /v1/sofa/wallets/689664/autofee?t=1629346575&r=RANDOM_STRING

    // params contains all query strings and post body if any
    const params = ['{"block_num":1}'];

    const curTime = 1629346575; // replace with current time, ex: Math.floor(Date.now()/1000);
    const checksum = buildRequestChecksum(params, 'API_SECRET', curTime, 'RANDOM_STRING');

    console.log(checksum);
}

function example_CALLBACK_checksum() {
  // calculate the checksum for callback notification

  const postBody = `{"type":2,"serial":20000000632,"order_id":"1_2_M1031","currency":"ETH","txid":"","block_height":0,"tindex":0,"vout_index":0,"amount":"10000000000000000","fees":"","memo":"","broadcast_at":0,"chain_at":0,"from_address":"","to_address":"0x8382Cc1B05649AfBe179e341179fa869C2A9862b","wallet_id":2,"state":1,"confirm_blocks":0,"processing_state":0,"addon":{"fee_decimal":18},"decimal":18,"currency_bip44":60,"token_address":""}`;

  const payload = postBody + 'API_SECRET';

  const checksum = buildCallbackChecksum(payload);

  console.log(checksum);
}

example_GET_request_checksum();
example_POST_request_checksum();
example_CALLBACK_checksum();
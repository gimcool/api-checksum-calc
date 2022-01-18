<?php
// Copyright (c) 2019-2022 The Gim.Cool developers
// All Rights Reserved.
// NOTICE: All information contained herein is, and remains
// the property of Gim.Cool and its suppliers,
// if any. The intellectual and technical concepts contained
// herein are proprietary to Gim.Cool
// Dissemination of this information or reproduction of this materia
// is strictly forbidden unless prior written permission is obtained
// from Gim.Cool.

function build_request_checksum($params, $secret, $t, $r) {
    array_push($params, 't='.$t, 'r='.$r);
    sort($params);
    array_push($params, 'secret='.$secret);
    return hash('sha256', implode('&', $params));
}

function base64url_encode($data) {
    return strtr(base64_encode($data), '+/', '-_');
} 

function build_callback_checksum($payload) {
    return base64url_encode(hash('sha256', $payload, true));
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

    // ps contains all query strings and post body if any
    $params = ['from_time=1561651200', 'to_time=1562255999', 'type=2'];

    $curTime = 1629346605; // replace with current time, ex: time()
    $checksum = build_request_checksum($params, "API_SECRET", $curTime, "RANDOM_STRING");

    file_put_contents('php://stdout', $checksum."\n");
}

function example_POST_request_checksum() {
    // calculate the checksum for API [POST] /v1/sofa/wallets/689664/autofee
    //   query: none
    //   body: {"block_num":1}
    //
    // final API URL should be /v1/sofa/wallets/689664/autofee?t=1629346575&r=RANDOM_STRING

    // params contains all query strings and post body if any
    $params = ['{"block_num":1}'];

    $curTime = 1629346575; // replace with current time, ex: time()
    $checksum = build_request_checksum($params, "API_SECRET", $curTime, "RANDOM_STRING");

    file_put_contents('php://stdout', $checksum."\n");
}

function example_CALLBACK_checksum() {
    // calculate the checksum for callback notification

    $postBody = "{\"type\":2,\"serial\":20000000632,\"order_id\":\"1_2_M1031\",\"currency\":\"ETH\",\"txid\":\"\",\"block_height\":0,\"tindex\":0,\"vout_index\":0,\"amount\":\"10000000000000000\",\"fees\":\"\",\"memo\":\"\",\"broadcast_at\":0,\"chain_at\":0,\"from_address\":\"\",\"to_address\":\"0x8382Cc1B05649AfBe179e341179fa869C2A9862b\",\"wallet_id\":2,\"state\":1,\"confirm_blocks\":0,\"processing_state\":0,\"addon\":{\"fee_decimal\":18},\"decimal\":18,\"currency_bip44\":60,\"token_address\":\"\"}";

    $payload = $postBody."API_SECRET";

    $checksum = build_callback_checksum($payload);

    file_put_contents('php://stdout', $checksum."\n");
}

example_GET_request_checksum();
example_POST_request_checksum();
example_CALLBACK_checksum();

?>
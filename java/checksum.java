// Copyright (c) 2019-2022 The Gim.Cool developers
// All Rights Reserved.
// NOTICE: All information contained herein is, and remains
// the property of Gim.Cool and its suppliers,
// if any. The intellectual and technical concepts contained
// herein are proprietary to Gim.Cool
// Dissemination of this information or reproduction of this materia
// is strictly forbidden unless prior written permission is obtained
// from Gim.Cool.

package com.Gim.Cool.api.checksum;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

public class checksum {

    static String sha256(final String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(input.getBytes("utf8"));
        return String.format("%064x", new BigInteger(1, digest.digest()));
    }

    static String buildRequestChecksum(final List<String> params, final String secret, final long time, final String r)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        params.add(String.format("t=%d", time));
        params.add(String.format("r=%s", r));
        Collections.sort(params);
        params.add(String.format("secret=%s", secret));
        return sha256(String.join("&", params));
    }

    static String buildCallbackChecksum(final String payload)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(payload.getBytes("utf8"));
        final Base64.Encoder encoder = Base64.getUrlEncoder();
        return encoder.encodeToString(digest.digest());
    }

    static void example_GET_request_checksum() {
        // calculate the checksum for API [GET] /v1/sofa/wallets/689664/notifications
        //   query:
        //     from_time=1561651200
        //     to_time=1562255999
        //     type=2
        //   body: none
        //
        // final API URL should be /v1/sofa/wallets/689664/notifications?from_time=1561651200&to_time=1562255999&type=2&t=1629346605&r=RANDOM_STRING

        List<String> params = new ArrayList<String>();
        params.add("from_time=1561651200");
        params.add("to_time=1562255999");
        params.add("type=2");

        try {
            final long curTime = 1629346605; // replace with current time, ex: System.currentTimeMillis() / 1000
            String checksum = buildRequestChecksum(params, "API_SECRET", curTime, "RANDOM_STRING");
            System.out.println(checksum);
        } catch (final Exception e) {
        }
    }

    static void example_POST_request_checksum() {
        // calculate the checksum for API [POST] /v1/sofa/wallets/689664/autofee
        //   query: none
        //   body: {"block_num":1}
        //
        // final API URL should be /v1/sofa/wallets/689664/autofee?t=1629346575&r=RANDOM_STRING

        List<String> params = new ArrayList<String>();
        params.add("{\"block_num\":1}");

        try {
            final long curTime = 1629346575; // replace with current time, ex: System.currentTimeMillis() / 1000
            String checksum = buildRequestChecksum(params, "API_SECRET", curTime, "RANDOM_STRING");
            System.out.println(checksum);
        } catch (final Exception e) {
        }
    }

    static void example_CALLBACK_checksum() {
        // calculate the checksum for callback notification

        final String postBody = "{\"type\":2,\"serial\":20000000632,\"order_id\":\"1_2_M1031\",\"currency\":\"ETH\",\"txid\":\"\",\"block_height\":0,\"tindex\":0,\"vout_index\":0,\"amount\":\"10000000000000000\",\"fees\":\"\",\"memo\":\"\",\"broadcast_at\":0,\"chain_at\":0,\"from_address\":\"\",\"to_address\":\"0x8382Cc1B05649AfBe179e341179fa869C2A9862b\",\"wallet_id\":2,\"state\":1,\"confirm_blocks\":0,\"processing_state\":0,\"addon\":{\"fee_decimal\":18},\"decimal\":18,\"currency_bip44\":60,\"token_address\":\"\"}";
    
        final String payload = postBody + "API_SECRET";
    
        try {
            String checksum = buildCallbackChecksum(payload);
            System.out.println(checksum);
        } catch (final Exception e) {
        }
    }

    public static void main(String[] args) {
        example_GET_request_checksum();
        example_POST_request_checksum();
        example_CALLBACK_checksum();
	}
}

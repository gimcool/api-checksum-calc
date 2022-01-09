// Copyright (c) 2019-2022 The Gim.Cool developers
// All Rights Reserved.
// NOTICE: All information contained herein is, and remains
// the property of Gim.Cool and its suppliers,
// if any. The intellectual and technical concepts contained
// herein are proprietary to Gim.Cool
// Dissemination of this information or reproduction of this materia
// is strictly forbidden unless prior written permission is obtained
// from Gim.Cool.

package main

import (
	"crypto/sha256"
	"encoding/base64"
	"fmt"
	"sort"
	"strings"
)

func buildRequestChecksum(params []string, secret string, time int64, r string) string {
	params = append(params, fmt.Sprintf("t=%d", time))
	params = append(params, fmt.Sprintf("r=%s", r))
	sort.Strings(params)
	params = append(params, fmt.Sprintf("secret=%s", secret))

	return fmt.Sprintf("%x", sha256.Sum256([]byte(strings.Join(params, "&"))))
}

func buildCallbackChecksum(payload string) (string, error) {
	sha := sha256.New()
	_, err := sha.Write([]byte(payload))
	if err != nil {
		return "", err
	}
	checksum := sha.Sum(nil)
	return base64.URLEncoding.EncodeToString(checksum), nil
}

func example_GET_request_checksum() {
	// calculate the checksum for API [GET] /v1/sofa/wallets/689664/notifications
	//   query:
	//     from_time=1561651200
	//     to_time=1562255999
	//     type=2
	//   body: none
	//
	// final API URL should be /v1/sofa/wallets/689664/notifications?from_time=1561651200&to_time=1562255999&type=2&t=1629346605&r=RANDOM_STRING

	// params contains all query strings and post body if any
	params := []string{"from_time=1561651200", "to_time=1562255999", "type=2"}

	var curTime int64 = 1629346605 // replace with current time, ex: time.Now().Unix()
	checksum := buildRequestChecksum(params, "API_SECRET", curTime, "RANDOM_STRING")

	fmt.Println(checksum)
}

func example_POST_request_checksum() {
	// calculate the checksum for API [POST] /v1/sofa/wallets/689664/autofee
	//   query: none
	//   body: {"block_num":1}
	//
	// final API URL should be /v1/sofa/wallets/689664/autofee?t=1629346575&r=RANDOM_STRING

	// params contains all query strings and post body if any
	params := []string{`{"block_num":1}`}

	var curTime int64 = 1629346575 // replace with current time, ex: time.Now().Unix()
	checksum := buildRequestChecksum(params, "API_SECRET", curTime, "RANDOM_STRING")

	fmt.Println(checksum)
}

func example_CALLBACK_checksum() {
	// calculate the checksum for callback notification

	postBody := `{"type":2,"serial":20000000632,"order_id":"1_2_M1031","currency":"ETH","txid":"","block_height":0,"tindex":0,"vout_index":0,"amount":"10000000000000000","fees":"","memo":"","broadcast_at":0,"chain_at":0,"from_address":"","to_address":"0x8382Cc1B05649AfBe179e341179fa869C2A9862b","wallet_id":2,"state":1,"confirm_blocks":0,"processing_state":0,"addon":{"fee_decimal":18},"decimal":18,"currency_bip44":60,"token_address":""}`

	payload := postBody + "API_SECRET"

	checksum, _ := buildCallbackChecksum(payload)

	fmt.Println(checksum)
}

func main() {
	example_GET_request_checksum()
	example_POST_request_checksum()
	example_CALLBACK_checksum()
}

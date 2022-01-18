// Copyright (c) 2019-2022 The Gim.Cool developers
// All Rights Reserved.
// NOTICE: All information contained herein is, and remains
// the property of Gim.Cool and its suppliers,
// if any. The intellectual and technical concepts contained
// herein are proprietary to Gim.Cool
// Dissemination of this information or reproduction of this materia
// is strictly forbidden unless prior written permission is obtained
// from Gim.Cool.

using System;
using System.Collections.Generic;
using System.Security.Cryptography;
using System.Text;

namespace c__checksum
{
    class Program
    {
        static string BuildRequestChecksum(List<string> ps, string secret, long time, string r)
        {
            ps.Add(String.Format("t={0}", time));
            ps.Add(String.Format("r={0}", r));
            ps.Sort(delegate(string x, string y){
              return String.Compare(x, y, StringComparison.Ordinal);
            });
            ps.Add(String.Format("secret={0}", secret));
            return sha256(String.Join("&", ps.ToArray()));
        }

        static string sha256(string value)
        {
            StringBuilder builder = new StringBuilder();
            using (SHA256 hash = SHA256.Create())
            {
                byte[] result = hash.ComputeHash(Encoding.UTF8.GetBytes(value));
                foreach (Byte b in result)
                {
                    builder.Append(b.ToString("x2"));
                }
            }
            return builder.ToString();
        }

        static string BuildCallbackChecksum(string payload)
        {
            using (SHA256 hash = SHA256.Create())
            {
                byte[] result = hash.ComputeHash(Encoding.UTF8.GetBytes(payload));
                return System.Convert.ToBase64String(result)
                    .Replace('+', '-').Replace('/', '_');
            }
        }

        static void Example_GET_request_checksum()
        {
            // calculate the checksum for API [GET] /v1/sofa/wallets/689664/notifications
            //   query:
            //     from_time=1561651200
            //     to_time=1562255999
            //     type=2
            //   body: none
            //
            // final API URL should be /v1/sofa/wallets/689664/notifications?from_time=1561651200&to_time=1562255999&type=2&t=1629346605&r=RANDOM_STRING

            // ps contains all query strings and post body if any
            List<string> ps = new List<string>(new string[] { "from_time=1561651200", "to_time=1562255999", "type=2" });

            long curTime = 1629346605; // replace with current time, ex: DateTimeOffset.Now.ToUnixTimeSeconds()
            string checksum = BuildRequestChecksum(ps, "API_SECRET", curTime, "RANDOM_STRING");

            Console.WriteLine(checksum);
        }

        static void Example_POST_request_checksum()
        {
            // calculate the checksum for /v1/sofa/wallets/689664/autofee
            // post body: {"block_num":1}

            // ps contains all query strings and post body if any
            List<string> ps = new List<string>(new string[] { "{\"block_num\":1}" });

            long curTime = 1629346575; // replace with current time, ex: DateTimeOffset.Now.ToUnixTimeSeconds();
            string checksum = BuildRequestChecksum(ps, "API_SECRET", curTime, "RANDOM_STRING");

            Console.WriteLine(checksum);
        }

        static void Example_CALLBACK_checksum()
        {
            // calculate the checksum for callback notification

            string postBody = "{\"type\":2,\"serial\":20000000632,\"order_id\":\"1_2_M1031\",\"currency\":\"ETH\",\"txid\":\"\",\"block_height\":0,\"tindex\":0,\"vout_index\":0,\"amount\":\"10000000000000000\",\"fees\":\"\",\"memo\":\"\",\"broadcast_at\":0,\"chain_at\":0,\"from_address\":\"\",\"to_address\":\"0x8382Cc1B05649AfBe179e341179fa869C2A9862b\",\"wallet_id\":2,\"state\":1,\"confirm_blocks\":0,\"processing_state\":0,\"addon\":{\"fee_decimal\":18},\"decimal\":18,\"currency_bip44\":60,\"token_address\":\"\"}";

            string payload = postBody + "API_SECRET";

            string checksum = BuildCallbackChecksum(payload);

            Console.WriteLine(checksum);
        }
        
        static void Main(string[] args)
        {
            Example_GET_request_checksum();
            Example_POST_request_checksum();
            Example_CALLBACK_checksum();
        }
    }
}

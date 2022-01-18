# Overview

- The repository provides how to calculate the API checksum and verify callback integrity for the [Gim.Cool API Wallet System](https://github.com/gimcool/wallet-api-mock-server).

# API Authentication

- The Gim.Cool API Wallet system verifies all incoming requests. All requests must include X-API-CODE, X-CHECKSUM headers otherwise caller will get a 403 Forbidden error.

### How to acquire and refresh API code and secret
- Request the API code/secret from the **Wallet Details** page on the web control panel for the first time.

### How to make a correct request?
- Put the API code in the X-API-CODE header.
- Calculate the checksum with the corresponding API secret and put the checksum in the X-CHECKSUM header.
  - The checksum calculation will use all the query parameters, the current timestamp, user-defined random string and the post body (if any).
- The final request must conform following conditions:
	- Required Headers:
		- X-API-CODE: API-CODE
		- X-CHECKSUM: CHECKSUM
		- Content-Type: application/json
		- User-Agent: USER-AGENT
	- API URL:
		- https://API-Wallet-SERVICE-URL/v1/sofa/wallets/CALLING-API-PATH?QUERY-PARAMS&t=REQUEST-TIME&r=RANDOM-STRING
			- where `t` and `r` are required
		
- Refer to the code snippets to know how to calculate the checksum for API.
	- [Go](https://github.com/gimcool/api-checksum-calc/blob/main/go/checksum.go#L40)
	- [Javascript](https://https://github.com/gimcool/api-checksum-calc/blob/main/javascript/checksum.js#L27)
	- [Java](https://https://github.com/gimcool/api-checksum-calc/blob/main/java/checksum.java#L49)
	- [PHP](https://https://github.com/gimcool/api-checksum-calc/blob/main/php/checksum.php#L27)
	- [C#](https://https://github.com/gimcool/api-checksum-calc/blob/main/c%23/checksum.cs#L55)
	- [Python](https://https://github.com/gimcool/api-checksum-calc/blob/main/python/checksum.py#L29)

# Callback Integrity Verification

- For callback integrity verification
	- Step 1: Calculate the checksum of the combination of callback payload and API secret.
	- Setp 2: Compare with the X-CHECKSUM header.

- Refer to the code snippets to know how to calculate the checksum of the callback.
	- [Go](https://github.com/gimcool/api-checksum-calc/blob/main/go/checksum.go#L75)
	- [Javascript](https://github.com/gimcool/api-checksum-calc/blob/main/javascript/checksum.js#L62)
	- [Java](https://https://github.com/gimcool/api-checksum-calc/blob/main/java/checksum.java#L90)
	- [PHP](https://https://github.com/gimcool/api-checksum-calc/blob/main/php/checksum.php#L62)
	- [C#](https://https://github.com/gimcool/api-checksum-calc/blob/main/c%23/checksum.cs#L89)
	- [Python](https://https://github.com/gimcool/api-checksum-calc/blob/main/python/checksum.py#L64)

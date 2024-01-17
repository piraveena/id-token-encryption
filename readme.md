
When the application uses the encrypted id tokens in the logout flow, the app can't share its key pairs with IS to decrypt the token. To support this use case, the expectation in the logout flow is to

1. In the app side, Decrypt the encrypted id_token by app's private key when the ID token is sent to app by IS.
2. Now it is a plain id_token in app side
3. Again App needs to encrypt the id_token by the IS public certificate
4. Send it to IS as id_token_hint in the in the logout request.
5. Then IS can decrypt it by it's own private key. An app can't share it's own key pairs with IS.
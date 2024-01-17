package org.example;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class Encrypt {

    private static JWEHeader header;
    private static Payload encryptedPayload;


    private static void decryptByAppsKey() throws Exception {

        // App's keystore location.
        File file = new File("/Users/piraveena/Documents/on-prem-is/id_token_encrpt/cert1/identity.p12");
        InputStream targetStream = new FileInputStream(file);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

        // Load the keystore with the password.
        keystore.load(targetStream, "password".toCharArray());

        // Alias of the app's cert.
        String alias = "notebook";

        // Get the private key. Password for the key store is 'wso2carbon'.
        RSAPrivateKey privateKey = (RSAPrivateKey) keystore.getKey(alias, "password".toCharArray());

        // Enter encrypted ID String which is sent by IS.
        String encryptedJWTString =
                "eyJraWQiOiJNamxqT0dWa09UQTBZVE5oWkRCbE9Ea3habUV6WlRNNE5EYzVaakJsTmpRNVptVmpNell3WkRrMk5HWmtNVGcxTnpjM1lXRmlOamN3TWpGaE9UUmtZdyIsImN0eSI6IkpXVCIsImVuYyI6IkExMjhHQ00iLCJhbGciOiJSU0EtT0FFUCJ9.J56590m1Vj8Zsw1KEsqIovl-A2-GIgLGqe47RRkA4QaaHwJHol1lnTNkSomiTxXV3fUolfeJb1T_zQlNrubi25XU-rx_HLVVIxKozy_Ut_cJa6Oj-8fwK6LGH6x_3bj3ikzpt7W7u7exsihPPQD06iccTLxatfopydQoZHcQ_HFl9IvDePczWlQbEQ4183MJMOh2r4m2pVntQYZE_5R1y1TtDX0FGZ2arWcbzSuIltefHhzVSwcGrmFYBtnwzdY74CuX4-uuB25y9Lc0z9qEjQaO7VH-8D0ecXvY7xjGeBCcVM7B4nUkLfBQ1TCFyR0-8WHPdlDTMC6dWxHn3161HQ.LT77TJfN7hSnfPKW.tVJoxJtnk1pgcmgT5dsuIbntbNN2M2d8u6wfxY2iQxkmV2y-gvggC-NyViAtHWG3LXml6zeGQAlVOlM6eWTXAgtFYp8RzlUQ0nKJoUuqWntPWckWPWo4_gusgpYyWndiD5LC_nqFOtODIKtbHxcet0Frgz2l4PNCVggi2x7MPyE4rdMMw5LIV5TqJI6NjPAU_V9V2bF4IMbrOxRJtr9s-h8oCall5rg0OqlWn20RTTtLqDsd9SE8qLD2N8dGtrmkKljTGLbCreROiGny9KNPWqLNejgR3isjTTFPvyJIQHtIkaSrb92SsdfAUh161mvxzlCC3AdRhdgCfj3hm2EtS2Ue5Qthm4Prt7UFvpGDJ2tk5LqwhwR9AIqsH084cm3TqpWhJZ8Qlj4aAS2di3GhwYDsdVRfL81pW2Vgv_ObCBi8lPvdMvaivm5qsrNk2XQ4GEPN2usZa7E8kBq5PEb1TyhtNCul3HR9wlsNAftYmocVgHD8krzCjnAaDUGLP1ltihI7xTiVENLa0IHYfEdUUbSqab_fSSlAN07ndTcBeF6I2YY1-i7JSeTtwi309Zo_WMD2aCbAHqrxGqsiJQed0pc8g6dtDGnrat7rQrzaAXGI39TjiYzDBrFkp__dtHCrlVs1Yhvf8nHEcRKDXia4Mdhc9L8lvB8RGmGtTslbHqvn5qP1veS5Szs2j05WnwfcjjC-Tzvd_4vMpKzjpyPl-QzYeMqtqoF5ShTATrT5waPz_ZPuE9XH6ggua9QNvba2VpJmpsECHUoztcHycwrJRj_h6Q-8jSjSeVZadzB6glG4zOFCb12J_OuhUHFHu0h0pDvt5SObdMcgYzNtu6SzyI0cU0cxkZY19RzxmdkIG-5bNp31byGcVQYceMREOMjetdwz-dfFIbbfgikNaS790mXIAgmdHGxwacdmyAgvH0zPx31NRXFQ7YHZuTQ0ffTeJRk-JGSGvnnqsC2vv56B7R3VNpLXVbAEy7kKNTYCBvwVfOlVYyR2kRbUG7nIFZLxwVAMWtvUMACYifem8nr6ECTiFw6Tgdgzn94CbIbyEH4cbQIMldJzmNKbkC2vHrXmV3Smg2DTQbPINcyKp85pCsrAObkvGi3-O_gMOU2aDFqEnZoabm9OR9WbZacDYH9h_DHjhXit8FZXbBK5RbmhOriP30W69aGnTUoGacrgRkPyWU911RBSFEr7Q7inWUGoQjOsdck0R9qx3FrvIFrHXMZfTvyxDnpU6nmuNr1-G28oz8LYEqhFgTSvWc5JhYsVKDW_4STe0xdEcALcuN3lcbspKCVk5Z0BscA-sG1jf_dFzwfLZvSnOedfyxLmgicvZ4Ptx3XijCW8aT1HcqeukqSdmKRbjWGRBluRZVTVs5aIz5WPLjUi9XwQ9-QtgZ-pHivDbiPel8VWZhZCsU01OUN5TYFi27QWrFSX1ebnGktT1fdee4c5a5NT_bUryAjiHtGH5UD9n_-tblCk1icsEebauuSFD7yzrKKBrDlmHMRXAJ0IudoPrT8V2ub1eSOVzBx7gwPZBt66UriMHtIBbaVNN0sc9VoxBPYiGs9ouw_09BalBpnL2na-1nNryau1bi2704TXNSziNbHpSWxN2bGy49EUZvsCa5fEC4AQH6wAWUlVYNUYivwfzmZfTAbViOfTZ99fm3MrYV1kKw5Ok3nC.US2JLNT8lEcQwwLRKnVRBQ";                EncryptedJWT jwt = EncryptedJWT.parse(encryptedJWTString);
        RSADecrypter decrypter = new RSADecrypter(privateKey);

        jwt.decrypt(decrypter);

        header = jwt.getHeader();
        encryptedPayload = jwt.getPayload();

        // Printing decrypted id token info.
        System.out.println("Decrypted ID token header: " + header.toJSONObject());
        System.out.println("Decrypted ID token payload: " + encryptedPayload);
    }

    private static void encryptByISCert() throws Exception {

        // IS keystore location.
        File file = new File("/Users/piraveena/Documents/on-prem-is/id_token_encrpt/cert1/wso2carbon.jks");
        InputStream targetStream = new FileInputStream(file);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

        // Load the keystore with the password.
        keystore.load(targetStream, "wso2carbon".toCharArray());

        String alias = "wso2carbon";

        Certificate cert = keystore.getCertificate(alias);
        RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

        JWTClaimsSet claimsSet = encryptedPayload.toSignedJWT().getJWTClaimsSet();
        // Create the encrypted JWT object
        EncryptedJWT jwt = new EncryptedJWT(header, claimsSet);

        // Create an encrypter with the specified public RSA key
        RSAEncrypter encrypter = new RSAEncrypter(publicKey);

        // Do the actual encryption
        jwt.encrypt(encrypter);

        // Printing decrypted id token header.
        System.out.println("Encrypted ID token: " + jwt.serialize());
    }
    public static void main(String[] args) throws Exception {

        // First decrypt the id token by apps key.
        decryptByAppsKey();

        // Then encrypt the id token by IS cert.
        encryptByISCert();
    }
}

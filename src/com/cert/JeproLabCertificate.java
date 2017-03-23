package com.cert;

import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;

import org.apache.log4j.Level;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;


import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.InvalidKeyException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by jeprodev on 15/03/2017.
 */
public class JeproLabCertificate {
    private final int bitLength = 1024;
    private SecureRandom secureRandom;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private TBSCertificateStructure tbsCertificateStructure;
    private X509Certificate certificate;
    private RSAPrivateCrtKeyParameters certificatePrivateKey;
    private String certificateFilePath, certificatePassWord, certificateAlias;
    private boolean useBCAPI;

    public JeproLabCertificate(String filePath, String passWord, String alias, boolean useBCAPI){
        certificateFilePath = filePath;
        certificateAlias = alias;
        certificatePassWord = passWord;
        this.useBCAPI = useBCAPI;
        try{
            File certificateFile = new File(filePath);

            if(!certificateFile.exists() && !certificateFile.createNewFile()){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, new Throwable("can't create certificate file please check directory right"));
            }
            FileInputStream inputStream = new FileInputStream(certificateFile);


            KeyStore certificateKeyStore = KeyStore.getInstance("PKCS12");
            certificateKeyStore.load(inputStream, passWord.toCharArray());
            Key key = certificateKeyStore.getKey(certificateAlias, certificatePassWord.toCharArray());
            if(key == null){ System.out.println(key);
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, new Throwable("Got null from key store!"));
            }
            RSAPrivateCrtKey privateCrtKey = (RSAPrivateCrtKey)key;
            certificatePrivateKey = new RSAPrivateCrtKeyParameters(
                privateCrtKey.getModulus(), privateCrtKey.getPublicExponent(), privateCrtKey.getPrivateExponent(), privateCrtKey.getPrimeP(),
                privateCrtKey.getPrimeQ(), privateCrtKey.getPrimeExponentP(), privateCrtKey.getPrimeExponentQ(), privateCrtKey.getCrtCoefficient()
            );
            certificate = (X509Certificate)certificateKeyStore.getCertificate(certificateAlias);

            if(certificate == null){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, new Throwable("ce certificate n'exste pas"));
            }
            certificate.verify(certificate.getPublicKey());
        }catch(KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | InvalidKeyException | NoSuchProviderException | SignatureException | IOException ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }
    }

    private void generateCertificate(String certificateNumber, int validityDays, String exportFile, String exportPassWord){
        /*try {
            secureRandom = new SecureRandom();
            if(useBCAPI) {
                RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
                generator.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3), secureRandom, bitLength, 80));
                AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
                RSAKeyParameters publicKeyParams = (RSAKeyParameters)keyPair.getPublic();
                RSAPrivateCrtKeyParameters privateKeyParams = (RSAPrivateCrtKeyParameters)keyPair.getPrivate();
                RSAPublicKeyStructure publicKeyStructure = new RSAPublicKeyStructure(publicKeyParams.getModulus(), publicKeyParams.getExponent());
                publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(publicKeyParams.getModulus(), publicKeyParams.getExponent()));
                privateKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(publicKeyParams.getModulus(),
                    publicKeyParams.getExponent(), privateKeyParams.getExponent(), privateKeyParams.getP(), privateKeyParams.getQ(),
                    privateKeyParams.getDP(), privateKeyParams.getDQ(), privateKeyParams.getQInv()));
            }else {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(bitLength, secureRandom);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                privateKey = keyPair.getPrivate();
                publicKey = keyPair.getPublic();
            }

            Calendar expiry = Calendar.getInstance();
            expiry.add(Calendar.DAY_OF_YEAR, validityDays);

            X509Name x509Name = new X509Name("CN=" + certificateNumber);

            V3TBSCertificateGenerator certificateGenerator = new V3TBSCertificateGenerator();
            certificateGenerator.setSerialNumber(new DERInteger(BigInteger.valueOf(System.currentTimeMillis())));
            certificateGenerator.setIssuer(PrincipalUtil.getSubjectX509Principal(certificate));
            certificateGenerator.setSubject(x509Name);

            DERObjectIdentifier derObjectIdentifier = X509Util.getAlgorithmOID("SHA1WithRSAEncryption");
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(derObjectIdentifier, new DERNull());
            certificateGenerator.setSignature(algorithmIdentifier);
            certificateGenerator.setSubjectPublicKeyInfo(
                new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
                    new ByteArrayInputStream(publicKey.getEncoded())).readObject())
            );
            certificateGenerator.setStartDate(new Time(new Date(System.currentTimeMillis())));
            certificateGenerator.setEndDate(new Time(expiry.getTime()));

            SHA1Digest digester = new SHA1Digest();
            AsymmetricBlockCipher rsa = new PKCS1Encoding(new RSAEngine());
            tbsCertificateStructure = certificateGenerator.generateTBSCertificate();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
            derOutputStream.writeObject(tbsCertificateStructure);

            byte[] signature;
            if(useBCAPI) {
                byte[] certificationBlock = byteArrayOutputStream.toByteArray();
                digester.update(certificationBlock, 0, certificationBlock.length);
                byte[] hash = new byte[digester.getDigestSize()];
                digester.doFinal(hash, 0);
                rsa.init(true, certificatePrivateKey);
                DigestInfo digestInfo = new DigestInfo(new AlgorithmIdentifier(X509ObjectIdentifiers.id_SHA1, null), hash);
                byte[] digest = digestInfo.getEncoded(ASN1Encodable.DER);
                signature = rsa.processBlock(digest, 0, digest.length);
            }else{
                PrivateKey certPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(
                    new RSAPrivateCrtKeySpec(certificatePrivateKey.getModulus(), certificatePrivateKey.getPublicExponent(),
                        certificatePrivateKey.getExponent(), certificatePrivateKey.getP(), certificatePrivateKey.getQ(),
                        certificatePrivateKey.getDP(), certificatePrivateKey.getDQ(), certificatePrivateKey.getQInv())
                );
                Signature sig = Signature.getInstance(derObjectIdentifier.getId());
                sig.initSign(certPrivateKey, secureRandom);
                sig.update(byteArrayOutputStream.toByteArray());
                signature = sig.sign();
            }
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(tbsCertificateStructure);
            v.add(algorithmIdentifier);
            v.add(new DERBitString(signature));

            X509CertificateObject certificateClient = new X509CertificateObject(new X509CertificateStructure(new DERSequence(v)));
            certificateClient.verify(certificate.getPublicKey());

            PKCS12BagAttributeCarrier certificateBag = certificateClient;
            certificateBag.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("My friendly name for the certificate"));
            certificateBag.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, new SubjectKeyIdentifierStructure(publicKey));

            KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(null, null);

            X509Certificate[] chain = new X509Certificate[2];
            chain[0] = certificateClient;
            chain[1] = certificate;

            store.setKeyEntry("My friendly name for the new private key", privateKey,
                exportPassWord.toCharArray(), chain);

            FileOutputStream fileOutputStream = new FileOutputStream(exportFile);
            store.store(fileOutputStream, exportPassWord.toCharArray());
        }catch(NoSuchAlgorithmException | IOException | NoSuchProviderException | SecurityException | SignatureException | DataLengthException | CryptoException | KeyStoreException | CertificateException | InvalidKeyException | InvalidKeySpecException ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }*/
    }


    /** The test CA can e.g. be created with
     *
     * echo -e "AT\nUpper Austria\nSteyr\nMy Organization\nNetwork tests\nTest CA certificate\nme@myserver.com\n\n\n" | \
     openssl req -new -x509 -outform PEM -newkey rsa:2048 -nodes -keyout /tmp/ca.key -keyform PEM -out /tmp/ca.crt -days 365;
     echo "test password" | openssl pkcs12 -export -in /tmp/ca.crt -inkey /tmp/ca.key -out ca.p12 -name "Test CA" -passout stdin
     *
     * The created certificate can be displayed with
     *
     * openssl pkcs12 -nodes -info -in test.p12 > /tmp/test.cert && openssl x509 -noout -text -in /tmp/test.cert
     */

    public static void main(String[] args) throws Exception {
        new JeproLabCertificate("ca.p12", "test password", "Test CA", false).generateCertificate("Test CN", 30, "test.p12", "test");
    }
}

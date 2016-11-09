#include "name_arche_retrofitutil_api_EncryptionClient.h"
#include "md5.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     name_arche_retrofitutil_api_EncryptionClient
 * Method:    signWithSalt
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
const char* SALT = "#thetestsalt";

JNIEXPORT jstring JNICALL Java_name_arche_retrofitutil_api_EncryptionClient_signWithSalt
  (JNIEnv *env, jclass clzz, jstring jstr) {
  	const char *sourceStr = (*env)->GetStringUTFChars(env, jstr, 0);
  	char *sourceStrWithSalt = join(sourceStr, SALT);
  	(*env)->ReleaseStringUTFChars(env, jstr, sourceStr);
  	unsigned char digest[16], digestHex[33];
  	memset(digestHex,0,33);
  	MD5_CTX md5;
  	MD5Init(&md5);
  	MD5UpdaterString(&md5, sourceStrWithSalt);
  	MD5Final(digest, &md5);
  	ByteToHexStr(digest, digestHex, 16);
  	free(sourceStrWithSalt);
  	return (*env)->NewStringUTF(env, digestHex);
  }

#ifdef __cplusplus
}
#endif

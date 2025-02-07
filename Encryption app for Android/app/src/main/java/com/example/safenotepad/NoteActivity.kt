package com.example.safenotepad

import android.content.Intent
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import java.security.InvalidKeyException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class NoteActivity : AppCompatActivity() {
//version 12:40
    private lateinit var note: EditText
    private lateinit var save: Button
    private val KEY_NAME = "myKey"
    private val VALIDITY_DURATION_SECONDS = 5 * 1000 * 60
    private lateinit var promptInfo : PromptInfo
    private lateinit var biometricPrompt: BiometricPrompt
    private var opMode = 0
    private val ITS_ENCRYPTING = 1
    private val ITS_DECRYPTING = 2
    private val BASE64_MODE = Base64.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        note = findViewById(R.id.note)
        save = findViewById(R.id.save)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("login")
            .setSubtitle("Log in using your biometric or device credentials")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        biometricPrompt = BiometricPrompt(this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()

                    if(opMode == ITS_ENCRYPTING){
                        /***ENCRYPTION***/
                        try {
                            //encrypt
                            val plainTextStr = note.text.toString()
                            val plainTextBytes = Base64.decode(plainTextStr, BASE64_MODE)
                            val encryptedBytes = result.cryptoObject?.cipher?.doFinal(
                                plainTextBytes
                            )
                            val encryptedStr = Base64.encodeToString(encryptedBytes, BASE64_MODE)
                            //save encrypted text
                            val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putString("note", encryptedStr)
                            val ivBytes = result.cryptoObject?.cipher?.iv
                            val ivStr = Base64.encodeToString(ivBytes, BASE64_MODE)
                            editor.putString("iv", ivStr)
                            editor.apply()
                            startActivity(Intent(this@NoteActivity, LoginActivity::class.java))

                            /***ENCRYPTION***/
                        } catch (e: InvalidKeyException) {
                            Log.e("MY_APP_TAG", "Key is invalid.")
                        } catch (e: UserNotAuthenticatedException) {
                            Log.d("MY_APP_TAG", "The key's validity timed out.")
                        }
                    }

                    else if (opMode == ITS_DECRYPTING){
                        /***DECRYPTION***/

                        try {
                            val encryptedTextStr = getSharedPreferences("my_prefs", MODE_PRIVATE)
                                .getString("note", "")
                            val encryptedTextBytes = Base64.decode(encryptedTextStr, BASE64_MODE)
                            val plainTextBytes = result.cryptoObject?.cipher?.doFinal(
                                encryptedTextBytes
                            )
                            val plainTextStr = Base64.encodeToString(plainTextBytes, BASE64_MODE)
                            note.setText(plainTextStr)


                        /***DECRYPTION***/
                        } catch (e: InvalidKeyException) {
                            Log.e("MY_APP_TAG", "Key is invalid.")
                        } catch (e: UserNotAuthenticatedException) {
                            Log.d("MY_APP_TAG", "The key's validity timed out.")
                        }
                    }

                    else{
                        Log.d("MY_APP_TAG", "opMode error")
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        readNote()

        save.setOnClickListener{ writeNote()}

    }

    private fun writeNote() {
        val cipher = getCipher()
        val secretKey = getSecretKey()
        try{cipher.init(Cipher.ENCRYPT_MODE, secretKey)}
        catch(e: Exception){
            e.printStackTrace()
            e.message
        }
        opMode = ITS_ENCRYPTING
        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipher)
        )
    }

    private fun readNote() {
        val cipher = getCipher()
        val secretKey = getSecretKey()
        val prefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val ivStr = prefs.getString("iv", "")
        if(ivStr.equals("")){ return }
        val ivBytes = Base64.decode(ivStr, BASE64_MODE)
        val iv =IvParameterSpec(ivBytes)
        try{cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)}
        catch(e: Exception){
            e.printStackTrace()
            e.message
        }
        opMode = ITS_DECRYPTING
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if(!keyStore.containsAlias(KEY_NAME)){
            generateSecretKey(KeyGenParameterSpec.Builder(
                KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationParameters(VALIDITY_DURATION_SECONDS,
                    KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL)
                .build())
        }
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

}
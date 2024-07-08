package com.tausif702.cryptomessenger.frgments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.tausif702.cryptomessenger.R
import com.tausif702.cryptomessenger.databinding.FragmentDecryptBinding
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class DecryptFragment : Fragment() {
    private lateinit var binding: FragmentDecryptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDecryptBinding.inflate(inflater, container, false)

        binding.btnDecrypt.setOnClickListener {
            if (binding.etEncryptedMessage.text.toString()
                    .isEmpty() || binding.etPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "please enter message and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                val message = binding.etEncryptedMessage.text.toString()
                val password = binding.etPassword.text.toString()
                binding.tvUserMsg.visibility = View.VISIBLE

                val encryptedMessage = encrypt(message, password)
                binding.tvOriginalMessage.text = encryptedMessage
                binding.etEncryptedMessage.text?.clear()
                binding.etPassword.text?.clear()
            }

        }

        binding.btnDecrypt.setOnClickListener {
            if (binding.etEncryptedMessage.text.toString().isEmpty() || binding.etPassword.text.toString()
                    .isEmpty()
            ) {
                binding.etEncryptedMessage.error = "Required"
                binding.etPassword.error = "Required"
            } else {
                binding.cvDecryptedMessage.visibility = View.VISIBLE
                val encryptedMessage = binding.etEncryptedMessage.text.toString()
                val password = binding.etPassword.text.toString()
                val decryptedMessage = decrypt(encryptedMessage, password)
                binding.tvOriginalMessage.text = decryptedMessage
                binding.tvOriginalMessage.setTextColor(resources.getColor(R.color.black))
                binding.copyIcon.setImageResource(R.drawable.copy)
            }
        }

        binding.copyIcon.setOnClickListener {
            val text = binding.tvOriginalMessage.text.toString()
            copyToClipboard(text)
            binding.copyIcon.setImageResource(R.drawable.ic_check)
        }
        binding.sharedicon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, binding.tvOriginalMessage.text.toString())
            startActivity(Intent.createChooser(intent, "Share Message"))
        }
        return binding.root
    }

    private fun hashPassword(password: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return SecretKeySpec(hashedBytes, "AES")
    }

    private fun encrypt(message: String, password: String): String {
        val keySpec: SecretKey = hashPassword(password)
        val cipher: Cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    private fun decrypt(encryptedMessage: String, password: String): String {
        return try {
            val keySpec: SecretKey = hashPassword(password)
            val cipher: Cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))
            String(decryptedBytes)
        } catch (e: Exception) {
            binding.tvOriginalMessage.text = "Decryption failed: Incorrect password"
            binding.tvOriginalMessage.setTextColor(resources.getColor(R.color.red))
            ""
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager =
            getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
    }

    companion object {
        private const val AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"
    }
}
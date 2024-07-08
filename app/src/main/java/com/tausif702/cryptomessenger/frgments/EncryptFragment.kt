package com.tausif702.cryptomessenger.frgments


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tausif702.cryptomessenger.R
import com.tausif702.cryptomessenger.databinding.FragmentEncryptBinding
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class EncryptFragment : Fragment() {
    private lateinit var binding: FragmentEncryptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEncryptBinding.inflate(inflater, container, false)


        binding.btnEncrypt.setOnClickListener {
            if (binding.etMessage.text.toString().isEmpty() || binding.etPassword.text.toString()
                    .isEmpty()
            ) {
                binding.etMessage.error = "Required"
                binding.etPassword.error = "Required"
                return@setOnClickListener
            } else {
                val encryptedMessage = encrypt(
                    binding.etMessage.text.toString(),
                    binding.etPassword.text.toString()
                )
                binding.tvEncryptedMessage.text = encryptedMessage
                binding.etMessage.text?.clear()
                binding.etPassword.text?.clear()
                binding.tvUserMsg.visibility = View.VISIBLE
                binding.copyIcon.setImageResource(R.drawable.copy)
                binding.cvEncryptedMessage.visibility = View.VISIBLE

            }

        }


        binding.copyIcon.setOnClickListener {
            copyToClipboard(binding.tvEncryptedMessage.text.toString())
            Toast.makeText(requireContext(), "copied", Toast.LENGTH_SHORT).show()
            binding.copyIcon.setImageResource(R.drawable.ic_check)
        }
        binding.sharedicon.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, binding.tvEncryptedMessage.text.toString())
            startActivity(Intent.createChooser(intent, "Share Message"))
        }
        return binding.root
    }

    private fun hashPassword(password: String): SecretKey {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        val hashPassword = SecretKeySpec(hashedBytes, "AES")
        Log.d("MyTag", "hashPassword:$hashPassword")
        return hashPassword

    }

    private fun encrypt(message: String, password: String): String {
        val keySpec: SecretKey = hashPassword(password)
        val cipher: Cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    companion object {
        private const val AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = ContextCompat.getSystemService(
            requireContext(),
            ClipboardManager::class.java
        ) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)


    }

}
package id.whynot.submission3.view.welcome

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import id.whynot.submission3.view.login.LoginActivity
import id.whynot.submission3.view.signup.SignUpActivity
import id.whynot.submission3.databinding.ActivityWellcomeBinding

class WellcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWellcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWellcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startanimation()
        binding.apply {
            Login.setOnClickListener {
                val intent = Intent(this@WellcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            Signup.setOnClickListener {
                val intent = Intent(this@WellcomeActivity, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
            setting.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }

    }

    private fun startanimation() {
        ObjectAnimator.ofFloat(binding.Logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE

        }.start()
        ObjectAnimator.ofFloat(binding.DIcoding, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE

        }.start()

    }

}
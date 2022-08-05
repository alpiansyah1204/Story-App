package id.whynot.submission3.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import id.whynot.submission3.MainActivity
import id.whynot.submission3.view.signup.SignUpActivity
import id.whynot.submission3.databinding.ActivityLoginBinding
import id.whynot.submission3.preference.Userprefreference
import id.whynot.submission3.request.Requestlogin
import id.whynot.submission3.viewmodel.StoryViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userprefreference: Userprefreference
    private lateinit var viewModel: StoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playanimation()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[StoryViewModel::class.java]
        userprefreference = Userprefreference(this)
        binding.apply {
            btnSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
            LoginBTN.setOnClickListener {
                login()
            }

        }

    }

    private fun login() {
        showloading(true)
        val request = Requestlogin()
        request.email = binding.inputemail.text.toString()
        request.password = binding.inputpasword.text.toString()
        viewModel.setLogin(this@LoginActivity, request)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getLogin().observe(this) {
                Log.e("login: ittt", it?.error.toString())
                if (it?.error != null) {
                    Log.e("login: ", "if dikerjakan")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("login: ", "else dikerjakan")
                    binding.inputpasword.text?.clear()
                    binding.inputemail.text?.clear()
                }
                showloading(false)
            }
        }, 5000)
    }


    private fun playanimation() {
        ObjectAnimator.ofFloat(binding.Logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginText = ObjectAnimator.ofFloat(binding.LoginText, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.EmailText, View.ALPHA, 1f).setDuration(300)
        val inputemail = ObjectAnimator.ofFloat(binding.inputemail, View.ALPHA, 1f).setDuration(300)
        val passwordText =
            ObjectAnimator.ofFloat(binding.passwordText, View.ALPHA, 1f).setDuration(300)
        val inputpasword =
            ObjectAnimator.ofFloat(binding.inputpasword, View.ALPHA, 1f).setDuration(300)
        val loginBTN = ObjectAnimator.ofFloat(binding.LoginBTN, View.ALPHA, 1f).setDuration(300)
        val bottomLinearLayout =
            ObjectAnimator.ofFloat(binding.BottomLinearLayout, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                loginText,
                emailText,
                inputemail,
                passwordText,
                inputpasword,
                loginBTN,
                bottomLinearLayout
            )
            start()
        }
    }

    private fun showloading(istrue: Boolean) {
        if (istrue) {
            binding.blackscreen.visibility = View.VISIBLE
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.blackscreen.visibility = View.GONE
            binding.progressbar.visibility = View.GONE
        }
    }
}

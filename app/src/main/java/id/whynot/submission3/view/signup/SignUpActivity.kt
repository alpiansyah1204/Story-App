package id.whynot.submission3.view.signup

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
import id.whynot.submission3.databinding.ActivitySignUpBinding
import id.whynot.submission3.request.RequestSignup
import id.whynot.submission3.view.login.LoginActivity
import id.whynot.submission3.viewmodel.StoryViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: StoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[StoryViewModel::class.java]
        playanimation()
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
        binding.SignupBTN.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        showloading(true)
        val request = RequestSignup()
        request.name = binding.inputname.text.toString().trim()
        request.email = binding.inputemail.text.toString().trim()
        request.password = binding.inputpasword.text.toString().trim()
        viewModel.setSignup(this@SignUpActivity, request)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getSignup().observe(this) {
                Log.e("login: ittt", it?.error.toString())
                if (it?.error != null) {
                    Log.e("login: ", "if dikerjakan")
                    binding.inputpasword.text?.clear()
                    binding.inputemail.text?.clear()
                    binding.inputname.text?.clear()
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.e("login: ", "else dikerjakan")
                    binding.inputpasword.text?.clear()
                    binding.inputemail.text?.clear()
                    binding.inputname.text?.clear()
                }
                showloading(false)
            }
        }, 6000)
    }


    private fun playanimation() {
        ObjectAnimator.ofFloat(binding.Logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val singuptext = ObjectAnimator.ofFloat(binding.singuptext, View.ALPHA, 1f).setDuration(300)
        val nametext = ObjectAnimator.ofFloat(binding.nametext, View.ALPHA, 1f).setDuration(300)
        val inputname = ObjectAnimator.ofFloat(binding.inputname, View.ALPHA, 1f).setDuration(300)
        val emailtext = ObjectAnimator.ofFloat(binding.emailtext, View.ALPHA, 1f).setDuration(300)
        val inputemail = ObjectAnimator.ofFloat(binding.inputemail, View.ALPHA, 1f).setDuration(300)
        val passwordtext =
            ObjectAnimator.ofFloat(binding.passwordtext, View.ALPHA, 1f).setDuration(300)
        val inputpasword =
            ObjectAnimator.ofFloat(binding.inputpasword, View.ALPHA, 1f).setDuration(300)
        val signupBTN = ObjectAnimator.ofFloat(binding.SignupBTN, View.ALPHA, 1f).setDuration(300)
        val bottomLinearLayout =
            ObjectAnimator.ofFloat(binding.BottomLinearLayout, View.ALPHA, 1f).setDuration(300)
        AnimatorSet().apply {
            playSequentially(
                singuptext,
                nametext,
                inputname,
                emailtext,
                inputemail,
                passwordtext,
                inputpasword,
                signupBTN,
                bottomLinearLayout,
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
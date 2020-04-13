package hu.ait.tanzina.discswap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setAnimation()

        Handler().postDelayed({
            goToListActivity()
        },3000)

    }

    private fun setAnimation(){
        var myAnim = AnimationUtils.loadAnimation(
            this@SplashActivity,
            R.anim.logo_splash
        )
        logo.startAnimation(myAnim)
    }

    private fun goToListActivity(){
        val ListActivityIntent = Intent()
        ListActivityIntent.setClass(this@SplashActivity, LoginActivity::class.java)
        startActivity(ListActivityIntent)
        finish()
    }
}

package com.example.p_kart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.p_kart.activity.LoginActivity
import com.example.p_kart.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navCOntroller = navHostFragment!!.findNavController()


        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav)

        binding.bottomBar.setupWithNavController(popupMenu.menu, navCOntroller)





        binding.bottomBar.onItemSelected = {
            when (it) {
                0 -> {
                    i = 0
                    navCOntroller.navigate(R.id.homeFragment)
                }
                1 -> i = 1
                2 -> i = 2

            }
        }


        // change the title while switch the fragment
        navCOntroller.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title = when (destination.id) {
                    R.id.cartFragment -> "My Cart"
                    R.id.moreFragment -> "My Dashboard"
                    else -> "P-Kart"
                }
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (i == 0){
            finish()
        }
    }
}
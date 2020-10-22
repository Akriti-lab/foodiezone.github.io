package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import fragment.MyProfileFragment
import com.akriti.foodiezone.R
import com.akriti.foodiezone.util.Restaurantdatabase
import com.google.android.material.navigation.NavigationView
import database.RestaurantEntity
import fragment.AllrestaurantFragment
import fragment.FAQsFragment
import fragment.FavouritesFragment

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences:SharedPreferences


    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.CoordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.FrameLayout)
        navigationView = findViewById(R.id.NavigationView)
        setUpToolbar()
        openAllrestaurant()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@HomeActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.itmHome ->{
                    openAllrestaurant()
                    drawerLayout.closeDrawers()
                }
                R.id.itmFAQs ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout, FAQsFragment())
                        .commit()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.itmOrderHistory ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.FrameLayout,
                            fragment.OrderHistoryFragment()
                        ).commit()
                    supportActionBar?.title="Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.itmLogout ->{
                    supportFragmentManager.beginTransaction()
                    val dialog=AlertDialog.Builder(this@HomeActivity)
                    dialog.setTitle("Warning")
                    dialog.setMessage("Are you sure to exit Foodie Zone?")
                    dialog.setNegativeButton("Cancel",null)
                    dialog.setPositiveButton("Yes"){text,listener->
                        sharedPreferences.edit().clear().apply()
                        finish()
                        startActivity(Intent(this@HomeActivity,
                            LoginActivity::class.java))

                    }
                    dialog.create()
                    dialog.show()
                }
                R.id.itmFavouriteRestaurants ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout, FavouritesFragment()).commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.itmProfile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.FrameLayout,
                            MyProfileFragment()
                        ).commit()
                        supportActionBar?.title="My Profile"
                    drawerLayout.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true


        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id== android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
       return super.onOptionsItemSelected(item)
    }
    fun openAllrestaurant(){
        val fragment= AllrestaurantFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FrameLayout,fragment)
        transaction.commit()
        supportActionBar?.title="All Reastaurant"
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.FrameLayout)
        when(frag){
            !is AllrestaurantFragment ->openAllrestaurant()
            else -> super.onBackPressed()
        }
    }
    class DBAsyncTask (val context: Context,val restaurantEntity: RestaurantEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        val db= Room.databaseBuilder(context,Restaurantdatabase::class.java,"restaurant_db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                              //check db if the book is favourite or not
                    val restaurant:RestaurantEntity?=db.restaurantDao().getRestaurantById(restaurantEntity.RestaurantId.toString())
                    db.close()
                    return restaurant!=null
                }
                2->{
                               //Save the book into db as favourite
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3->{
                              //Remove the book from favourites
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

}
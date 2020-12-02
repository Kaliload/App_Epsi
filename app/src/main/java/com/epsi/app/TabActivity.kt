package com.epsi.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_tab.*

class TabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_tab)

      insert_fragment_here.adapter = ChatStatusCallsFragmentAdapter(this)
      TabLayoutMediator(tablayout, insert_fragment_here){ tab, position ->
        when (position) {
          0 -> tab.text = "Chats"
          1 -> tab.text = "Status"
          2 -> tab.text = "Calls"
        }
      }.attach()
    }
}

package com.epsi.app

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChatStatusCallsFragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int {
    return 3
  }

  override fun createFragment(position: Int): Fragment {
    when (position) {
      0 -> return ChatFragment() //ChildFragment1 at position 0
      1 -> return StatusFragment() //ChildFragment2 at position 1
      2 -> return CallsFragment() //ChildFragment3 at position 2
    }
    return ChatFragment() //does not happen

  }

}

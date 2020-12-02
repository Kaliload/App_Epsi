package com.epsi.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.frag.*

class MyFragment : Fragment() {

  private lateinit var mListener: OnFragmentInteractionListener

  interface OnFragmentInteractionListener {
    fun closeFrag(){
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.frag, container, false)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is OnFragmentInteractionListener) {
      mListener = context
    } else {
      throw ClassCastException("erreur")
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    back_button.setOnClickListener {
      mListener.closeFrag()
    }
    back_button1.setOnClickListener {
      mListener.closeFrag()
    }
    close_profile.setOnClickListener {
      mListener.closeFrag()
    }
  }
}

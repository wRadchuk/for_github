package com.wradchuk.forgithub.test_navigation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.wradchuk.forgithub.R

class ForTestNavigationFragment: Fragment(R.layout.fragment_for_test_navigation) {

    companion object {

        const val ARGS = "text"
        fun newInstance(text: String) = ForTestNavigationFragment().apply {
            arguments = bundleOf(ARGS to text)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.firstChildTextView)?.let { textView ->
            textView.text = arguments?.getString(ARGS) ?: throw Exception("Не удалось получить текст из аргументов")
        }
    }
}
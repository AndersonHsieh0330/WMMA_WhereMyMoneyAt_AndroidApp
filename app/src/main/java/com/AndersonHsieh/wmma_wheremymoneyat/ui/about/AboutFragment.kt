package com.AndersonHsieh.wmma_wheremymoneyat.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var SwipeRevealLayoutInfoBTN: TextView
    private lateinit var RetrofitInfoBTN: TextView
    private lateinit var termsNConditions: TextView
    private lateinit var privacyPolicy: TextView

    private lateinit var gitHubLink: ImageButton
    private lateinit var instagramLink: ImageButton
    private lateinit var linkedInLink: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }

        private fun initUI() {

            SwipeRevealLayoutInfoBTN = binding.AboutFragmentSwipeViewLayoutInfoBTN
            RetrofitInfoBTN = binding.AboutFragmentRetrofitInfoBTN
            termsNConditions = binding.AboutFragmentTermsNConditions
            privacyPolicy = binding.AboutFragmentPrivacyPolicy

            gitHubLink = binding.AboutFragmentGithubLink
            instagramLink = binding.AboutFragmentInstagramLink
            linkedInLink = binding.AboutFragmentLinkedInLink

            //to my social media profile
            gitHubLink.setOnClickListener {
                goToUrl(resources.getString(R.string.GitHubLink))
            }
            instagramLink.setOnClickListener {
                goToUrl(resources.getString(R.string.InstagramLink))
            }
            linkedInLink.setOnClickListener {
                goToUrl(resources.getString(R.string.LinkedInLink))
            }

            SwipeRevealLayoutInfoBTN.setOnClickListener {
                goToUrl(resources.getString(R.string.SwipeRevealLayout_Link))
            }
            RetrofitInfoBTN.setOnClickListener {
                goToUrl(resources.getString(R.string.Retrofit_Link))

            }
        }

        private fun goToUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
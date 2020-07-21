package duodev.valerio.electric.Settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.valerio.electric.R
import duodev.valerio.electric.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_contact_us.*

class ContactUsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
    }

    private fun setListeners() {
        emailLayout.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "plain/text"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("valerioelectric20@gmail.com"))
            startActivity(i)
        }

        phoneLayout.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:+917500004895")
            startActivity(i)
        }
    }

    companion object {
        fun newInstance() = ContactUsFragment()
    }
}
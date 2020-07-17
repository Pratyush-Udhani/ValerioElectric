package duodev.valerio.electric.Admin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import duodev.valerio.electric.Admin.Adapter.AddedPlugsAdapter
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.fragment_admin_panel.*
import kotlinx.android.synthetic.main.layout_add_station.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminPanelFragment : Fragment(), AddedPlugsAdapter.OnClick {

    private val plugDialog by lazy { Dialog(requireContext()) }
    private var stationUrl: Uri = Uri.EMPTY
    private var companyUrl: Uri = Uri.EMPTY
    private val plugList: MutableList<Connector> = mutableListOf()
    private var owner = ""
    private var storeReference = FirebaseStorage.getInstance().reference
    private var companyBitmap = Uri.EMPTY
    private var stationBitmap = Uri.EMPTY
    private var uploaded = MutableLiveData<Boolean>(false)
    private val addedPlugsAdapter by lazy { AddedPlugsAdapter(mutableListOf(), this) }
    private val layoutManager by lazy { LinearLayoutManager(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObserver()
        setUpListeners()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        plugRecycler.apply {
            adapter = this@AdminPanelFragment.addedPlugsAdapter
            layoutManager = this@AdminPanelFragment.layoutManager
        }
    }

    private fun setUpListeners() {
        uploadCompanyImage.setOnClickListener {
            openFilePicker(PICK_COMPANY_IMAGE)
        }

        uploadStationImage.setOnClickListener {
            openFilePicker(PICK_STATION_IMAGE)
        }

        privateOwner.setOnClickListener {
            publicOwner.isChecked = false
            owner = "Private Owner"
        }

        publicOwner.setOnClickListener {
            privateOwner.isChecked = false
            owner = "Public Owner"
        }

        removeCompanyImageButton.setOnClickListener {
            removePreviewImage(COMPANY)
        }

        removeStationImageButton.setOnClickListener {
            removePreviewImage(STATION)
        }

        addPlugButton.setOnClickListener {
            showPlugDialog()
        }

        uploadStationButton.setOnClickListener {
            if (stationAddress.text.isNotEmpty()
                && stationAddress.text.isNotEmpty()
                && stationCity.text.isNotEmpty()
                && stationLat.text.isNotEmpty()
                && stationLong.text.isNotEmpty()
                && stationSlots.text.isNotEmpty()
                && companyName.text.isNotEmpty()
                && owner.isNotEmpty()
            ) {
                if (stationUrl != Uri.EMPTY && companyUrl != Uri.EMPTY) {
                    if (plugList.size != 0) {
                        uploadFile()
                    } else {
                        activity?.toast("add a plug")
                    }
                } else {
                    activity?.toast("upload company image")
                }
            } else {
                activity?.toast("Enter all details")
            }
        }
    }

    private fun setUpObserver() {
        uploaded.observe(viewLifecycleOwner, Observer {
            if (uploaded.value!!) {
                uploadToDatabase()
            }
        })
    }

    private fun uploadToDatabase() {
        val firestore = FirebaseFirestore.getInstance()
        val id = firestore.collection(STATIONS).document().id

        firestore.collection(STATIONS).document(id).set(
            Station(
                stationLocation = stationCity.trimString(),
                stationAddress = stationAddress.trimString(),
                ownerCompany = Company(
                    name = companyName.trimString(),
                    imageUri = companyBitmap.toString()
                ),
                serviceProvider = serviceProvider.trimString(),
                location = GeoPoint(
                    stationLat.text.toString().toDouble(),
                    stationLong.text.toString().toDouble()
                ),
                imageUrl = stationBitmap.toString(),
                numberOfStations = stationSlots.text.toString().toInt(),
                stationId = id,
                connectorType = plugList,
                ownership = owner
            )
        ).addOnSuccessListener {
            activity?.toast("Station added")
            progressBar.makeGone()
            setDefs()
        }
    }

    private fun setDefs() {
        addedPlugsAdapter.removeAll()
        plugList.clear()
        removePreviewImage(STATION)
        removePreviewImage(COMPANY)
        stationAddress.setText("")
        stationCity.setText("")
        stationSlots.setText("")
        companyName.setText("")
        serviceProvider.setText("")
        stationLat.setText("")
        stationLong.setText("")
        privateOwner.isChecked = false
        publicOwner.isChecked = false
    }

    private fun uploadFile() {
        progressBar.makeVisible()
        val companyStorageReference = storeReference.child(COMPANY_IMAGE).child(
            companyName.text.toString() + "." + getFileExtension(companyUrl)
        )
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                companyStorageReference.putFile(companyUrl).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    companyStorageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                       // uploaded.value = true
                        companyBitmap = task.result
                        uploadSecond()
                    }
                }
                return@withContext
            }
            return@launch
        }

    }

    private fun uploadSecond() {
        val stationStorageReference = storeReference.child(STATION_IMAGE).child(
            stationLat.text.toString() + "." + getFileExtension(stationUrl)
        )

        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                stationStorageReference.putFile(stationUrl).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    stationStorageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        stationBitmap = task.result
                        uploaded.value = true
                    }
                }
                return@withContext
            }
            return@launch
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = context?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(cr?.getType(uri))
    }

    private fun removePreviewImage(flag: String) {
        when (flag) {
            STATION -> {
                stationUrl = Uri.EMPTY
                stationImageLayout.makeGone()
                uploadStationImage.makeVisible()
            }
            COMPANY -> {
                companyUrl = Uri.EMPTY
                companyImageLayout.makeGone()
                uploadCompanyImage.makeVisible()
            }
        }
    }

    private fun openFilePicker(request: Int) {
        val intent: Intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_COMPANY_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                companyUrl = data?.data!!
                Glide.with(this).load(companyUrl).into(companyImage)
                companyImageLayout.makeVisible()
                uploadCompanyImage.makeGone()
            }
        }

        if (requestCode == PICK_STATION_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                stationUrl = data?.data!!
                Glide.with(this).load(stationUrl).into(stationImage)
                stationImageLayout.makeVisible()
                uploadStationImage.makeGone()
            }
        }
    }

    private fun showPlugDialog() {
        val view: View = layoutInflater.inflate(R.layout.dialog_add_plug, null)
        plugDialog.apply {
            setContentView(view)
            create()
            plugDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val plugName: AutoCompleteTextView = view.findViewById(R.id.plugName)
            val plugPrice: EditText = view.findViewById(R.id.plugCost)
            val addPlugButton: CardView = view.findViewById(R.id.addPlugButton)

            val plugAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.plugs,
                R.layout.autocomplete_text
            )

            var plug = ""

            plugName.threshold = 0
            plugName.setAdapter(plugAdapter)
            plugName.setDropDownBackgroundResource(R.drawable.bg_white_soft_box)
            plugName.dropDownVerticalOffset = 20

            plugName.setOnItemClickListener { parent, view, position, id ->
                plug = plugAdapter.getItem(position).toString()
            }

            plugName.doOnTextChanged { text, start, before, count ->
                plug = ""
            }

            plugName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    plugName.showDropDown()
                    plugName.addTextChangedListener {
                        if (it.isNullOrEmpty()) {
                            plugName.showDropDown()
                        }
                    }
                } else {
                    closeKeyboard(requireContext(), v)
                    v.clearFocus()
                }
            }

            addPlugButton.setOnClickListener {
                if (plug != "") {
                    Connector(
                        type = plug,
                        price = plugPrice.text.toString()
                    ).also {
                        plugList.add(it)
                        addedPlugsAdapter.addItem(it)
                        dismiss()
                    }
                } else {
                    activity?.toast("select port type")
                }
            }
            show()
        }
    }


    override fun onRemoved(item: Connector, position: Int) {
        plugList.remove(item)
    }

    companion object {
        fun newInstance() = AdminPanelFragment()
    }
}
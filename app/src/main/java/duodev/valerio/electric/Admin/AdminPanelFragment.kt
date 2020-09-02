package duodev.valerio.electric.Admin

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import duodev.valerio.electric.Admin.Adapter.AddedPlugsAdapter
import duodev.valerio.electric.Home.HomeActivity
import duodev.valerio.electric.Login.LoginActivity
import duodev.valerio.electric.R
import duodev.valerio.electric.Utils.*
import duodev.valerio.electric.base.BaseFragment
import duodev.valerio.electric.pojos.Company
import duodev.valerio.electric.pojos.Connector
import duodev.valerio.electric.pojos.Station
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_station_location.*
import kotlinx.android.synthetic.main.fragment_admin_panel.*
import kotlinx.android.synthetic.main.layout_add_station.*
import kotlinx.android.synthetic.main.layout_add_station.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class AdminPanelFragment : BaseFragment(), AddedPlugsAdapter.OnClick {

    private val plugDialog by lazy { Dialog(requireContext()) }
    private var stationUrl: Uri = Uri.EMPTY
    private var companyUrl: Uri = Uri.EMPTY
    private var editStationUrl: Uri = Uri.EMPTY
    private var editCompanyUrl: Uri = Uri.EMPTY
    private var plugList: MutableList<Connector> = mutableListOf()
    private var owner = ""
    private var storeReference = FirebaseStorage.getInstance().reference
    private var companyBitmap = Uri.EMPTY
    private var stationBitmap = Uri.EMPTY
    private var uploaded = MutableLiveData<Boolean>(false)
    private val addedPlugsAdapter by lazy { AddedPlugsAdapter(mutableListOf(), this) }
    private val layoutManager by lazy { LinearLayoutManager(requireContext()) }
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var flag = ""
    private lateinit var editStation: HashMap<String,Any>
    private val dialog by lazy { Dialog(requireContext()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(FLAG)!!
            if(flag === EDIT){
                editStation = it.getSerializable(STATION) as HashMap<String, Any>
            }
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
        setUpUI()
        setUpObserver()
        setUpListeners()
        setUpRecycler()
    }

    private fun setUpUI() {
        if (flag == ADMIN) {
//            (activity as HomeActivity).bottomNavCard.makeGone()
            adminLogout.makeVisible()
            backButton.makeGone()
            headingTitle.text = "Panel"
            addStationText.text = "Upload Station"
        }

        if (flag === EDIT) {
            headingText.text = "Edit Station"
            addStationText.text = "Edit Station"
            stationAddress.setText(editStation[ADDRESS].toString())
            stationSlots.setText(editStation[SLOTS].toString())
            stationCity.setText(editStation[LOCATION].toString())
            companyName.setText((editStation[OWNER] as Company).name)
            plugList = editStation[CONNECTOR] as MutableList<Connector>
            addedPlugsAdapter.addAll(plugList)
            serviceProviderPhone.setText((editStation[OWNER] as Company).phone)
            serviceProviderEmail.setText((editStation[OWNER] as Company).email)
            serviceProvider.setText(editStation[PROVIDER].toString())
            lat = editStation[LATITUDE] as Double
            long = editStation[LONGITUDE] as Double
            editCompanyUrl = (editStation[OWNER] as Company).imageUri.toUri()
            editStationUrl = editStation[IMAGE_URL].toString().toUri()
            stationUrl = editStation[IMAGE_URL].toString().toUri()
            companyUrl = (editStation[OWNER] as Company).imageUri.toUri()
            owner = editStation[OWNERSHIP].toString()

            when(editStation[OWNERSHIP].toString()) {
                "Private" -> ownerGroup.check(R.id.privateOwner)
                "Public" -> ownerGroup.check(R.id.publicOwner)
            }
            Glide.with(this)
                .load((editStation[OWNER] as Company).imageUri.toUri())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(companyImage)
            companyImageLayout.makeVisible()
            uploadCompanyImage.makeGone()

            Glide.with(this)
                .load(editStation[IMAGE_URL].toString().toUri())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(stationImage)
            stationImageLayout.makeVisible()
            uploadStationImage.makeGone()

        }

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

        ownerGroup.setOnCheckedChangeListener { _, checkedId ->
            privateOwner.setOnClickListener {
                publicOwner.isChecked = false
                owner = "Private Owner"
            }
            publicOwner.setOnClickListener {
                privateOwner.isChecked = false
                owner = "Public Owner"
            }
        }

        locationButton.setOnClickListener {
                startActivityForResult(AddStationLocation.newInstance(requireContext()), CODE)
                activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
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

        adminLogout.setOnClickListener {
            startActivity(LoginActivity.newInstance(requireContext()))
            activity?.overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        backButton.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
        }

        uploadStationButton.setOnClickListener {
            if (flag == ADMIN || flag == EDIT) {
                if (stationAddress.text.isNotEmpty()
                    && stationAddress.text.isNotEmpty()
                    && stationCity.text.isNotEmpty()
                    && stationSlots.text.isNotEmpty()
                    && companyName.text.isNotEmpty()
                    && owner.isNotEmpty()
                ) {
                    if (stationUrl != Uri.EMPTY && companyUrl != Uri.EMPTY) {
                        if (plugList.size != 0) {
    //                        setLocation()
                            uploadFile()
    //                        activity?.toast("Station Edited Successfully")
    //                        (activity as HomeActivity).supportFragmentManager.popBackStackImmediate()
                        } else {
                            activity?.toast("add a plug")
                        }
                    } else {
                        activity?.toast("upload company image")
                    }
                } else {
                    activity?.toast("Enter all details")
                }
            } else {
                if (stationAddress.text.isNotEmpty()
                    && stationCity.text.isNotEmpty()
                    && companyName.text.isNotEmpty()) {

                BackgroundMail.newBuilder(requireContext())
                    .withUsername(ADMIN_EMAIL)
                    .withPassword(ADMIN_PASS)
                    .withSenderName("VEcharge Bharat")
                    .withMailTo("valerioappdev@gmail.com")
                    .withType(BackgroundMail.TYPE_PLAIN)
                    .withSubject("Station Add Request")
                    .withBody(
                        """
                            Please add the following station with details as follows
                            
                            Station Address: ${stationAddress.text}
                            Station city: ${stationCity.text}
                            Station Company: ${companyName.text}
                        """.trimIndent()
                    )
//            .withProcessVisibility(false)
                    .send()
                    activity?.toast("Your Station Add request has been sent.")
                    setDefs()
                } else {
                    requireContext().toast("Please enter address, company name, and city")
                }
            }
        }
    }

    private fun setLocation() {
        val geoCoder = Geocoder(requireContext()).getFromLocationName(stationAddress.text.toString(), 1)
        if (geoCoder.size > 0) {
            if (geoCoder[0].hasLatitude() && geoCoder[0].hasLongitude()){
                lat = Geocoder(requireContext()).getFromLocationName(stationAddress.text.toString(), 1)[0].latitude
                long = Geocoder(requireContext()).getFromLocationName(stationAddress.text.toString(), 1)[0].longitude
                uploadFile()

            } else {
                if (flag == ADMIN)
                    activity?.toast("No lat long found")
                else
                    activity?.toast("Invalid address. Contact us for more details")
            }
        } else {
            activity?.toast("Please enter complete address")
        }
    }

    private fun setUpObserver() {
        uploaded.observe(viewLifecycleOwner, Observer {
            if (uploaded.value!!) {
                if (flag == ADMIN)
                    uploadToDatabase()
                else if (flag === EDIT){
                    updateDatabase()
                }else{
                    progressBar.makeGone()
                    activity?.toast("User side")
                }
            }
        })
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
        serviceProviderPhone.setText("")
        serviceProviderEmail.setText("")
        stationLat.setText("")
        stationLong.setText("")
        privateOwner.isChecked = false
        publicOwner.isChecked = false
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
                    imageUri = companyBitmap.toString(),
                    phone = serviceProviderPhone.text.toString(),
                    email = serviceProviderEmail.text.toString()
                ),
                serviceProvider = serviceProvider.trimString(),
                location = GeoPoint(lat, long),
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

    private fun updateDatabase() {
        val firestore = FirebaseFirestore.getInstance()
        val id = editStation[ID].toString()
        val StationBitmap = if(stationUrl.toString() !== editStationUrl.toString())  {
            stationBitmap.toString()
        }  else{
            editStationUrl.toString()
        }
        val companyBitmap = if(companyUrl.toString() !== editCompanyUrl.toString())  {
            companyBitmap.toString()
        }  else{
            editCompanyUrl.toString()
        }
        Log.d("TAG", "updateDatabase: editStationID: $id")
        firestore.collection(STATIONS).document(id).set(
            Station(
                stationLocation = stationCity.trimString(),
                stationAddress = stationAddress.trimString(),
                ownerCompany = Company(
                    name = companyName.trimString(),
                    imageUri = companyBitmap.toString(),
                    phone = serviceProviderPhone.text.toString(),
                    email = serviceProviderEmail.text.toString()
                ),
                serviceProvider = serviceProvider.trimString(),
                location = GeoPoint(lat, long),
                imageUrl = StationBitmap.toString(),
                numberOfStations = stationSlots.text.toString().toInt(),
                stationId = id,
                connectorType = plugList,
                ownership = owner
            )
        ).addOnSuccessListener {
            Log.d("TAG", "updateDatabase: Updated")
            activity?.toast("Station Edit Successful")
            progressBar.makeGone()
            setDefs()
        }
        .addOnFailureListener {
            Log.d("TAG", "updateDatabase: Station Update Failed" + it.message)
            activity?.toast("Station update Failed")
            progressBar.makeGone()
            throw it


        }
    }

    private fun uploadFile() {

        if(companyUrl.toString() !== editCompanyUrl.toString()) {

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
        }else{
            uploaded.value = true
        }

    }

    private fun uploadSecond() {
        if(stationUrl.toString() !== editStationUrl.toString()) {
            val stationStorageReference = storeReference.child(STATION_IMAGE).child(
                long.toString() + "." + getFileExtension(stationUrl)
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
        }else{
            uploaded.value = true
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
                Glide.with(this)
                    .load(companyUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(companyImage)
                companyImageLayout.makeVisible()
                uploadCompanyImage.makeGone()
            }
        }

        if (requestCode == PICK_STATION_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                stationUrl = data?.data!!
                Glide.with(this)
                    .load(stationUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(stationImage)
                stationImageLayout.makeVisible()
                uploadStationImage.makeGone()
            }
        }

        if (requestCode == CODE) {
            Log.d("TATATA","received intent")
            if (data != null) {
                Log.d("TATATA","data not null")
                lat = data.getDoubleExtra(AddStationLocation.LAT, 0.0)
                long = data.getDoubleExtra(AddStationLocation.LONG, 0.0)
                stationAddress.setText(data.getStringExtra(AddStationLocation.ADDRESS))
                Log.d("TATATATA", "$lat, $long")
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
            val plugId: EditText = view.findViewById(R.id.plugId)

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
                    if (plugPrice.text.isNotEmpty()) {
                        Connector(
                            type = plug,
                            price = plugPrice.text.toString(),
                            id = plugId.text.toString()
                        ).also {
                            plugList.add(it)
                            addedPlugsAdapter.addItem(it)
                            dismiss()
                        }
                    } else {
                        activity?.toast("enter port price")
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

        const val ADDRESS = "address"
        const val PROVIDER = "provider"
        const val CONNECTOR = "connector"
        const val OWNER = "owner"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ID = "id"
        const val LOCATION = "location"
        const val IMAGE_URL = "imageUrl"
        const val OWNERSHIP = "ownership"
        const val SLOTS = "slots"
        private const val FLAG = "flag"
        const val CODE =  19

        fun newInstance(flag: String) = AdminPanelFragment().apply {
            arguments = Bundle().apply {
                putString(FLAG, flag)
            }
        }
        fun editInstance(flag: String,station: Serializable?) = AdminPanelFragment().apply {
            arguments = Bundle().apply {
                putString(FLAG,flag)
                putSerializable(STATION,station)
            }
        }






    }
}
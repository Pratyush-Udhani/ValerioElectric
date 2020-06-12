package duodev.valerio.electric.pojos

data class Station (
    val stationName: String,
    val stationAddress: String,
    val stationAvailability: Boolean,
    val stationPort: String,
    val starred: Boolean
)
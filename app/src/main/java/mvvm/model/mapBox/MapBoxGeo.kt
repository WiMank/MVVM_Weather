package mvvm.model.mapBox

object MapBoxGeo {


    data class Map(
        var type: String,
        var query: List<String>,
        var features: Feature
    )


    data class Context(
        var id: String,
        var text: String,
        var wikidata: String,
        var shortCode: String
    )


    data class Feature(
        var id: String,
        var type: String,
        var placeType: List<String>,
        var relevance: Double,
        var properties: Properties,
        var text: String,
        var placeName: String,
        var matchingText: String,
        var matchingPlaceName: String,
        var center: List<Double>,
        var geometry: Geometry,
        var address: String,
        var context: Context

    )


    data class Geometry(
        var type: String,
        var coordinates: List<Double>,
        var interpolated: Boolean,
        var omitted: Boolean

    )

    class Properties
}
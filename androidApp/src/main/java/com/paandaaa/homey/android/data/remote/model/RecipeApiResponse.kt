import com.google.gson.annotations.SerializedName

data class RecipeApiResponse(
    @SerializedName("results") val results: List<RemoteRecipe>
)

data class RemoteRecipe(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("instructions") val instructions: String
)
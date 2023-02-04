package com.mrwhoknows.moviemultiverse.net.dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.mrwhoknows.moviemultiverse.model.Credits
import com.mrwhoknows.moviemultiverse.util.Constants

@Keep
data class MovieCreditsResponse(
    @SerializedName("cast") val cast: List<Cast>,
    @SerializedName("crew") val crew: List<Crew>,
    @SerializedName("id") val id: Int
) {
    @Keep
    data class Cast(
        @SerializedName("adult") val adult: Boolean,
        @SerializedName("cast_id") val castId: Int,
        @SerializedName("character") val character: String,
        @SerializedName("credit_id") val creditId: String,
        @SerializedName("gender") val gender: Int,
        @SerializedName("id") val id: Long,
        @SerializedName("known_for_department") val knownForDepartment: String,
        @SerializedName("name") val name: String,
        @SerializedName("order") val order: Int,
        @SerializedName("original_name") val originalName: String,
        @SerializedName("popularity") val popularity: Double,
        @SerializedName("profile_path") val profilePath: String
    )

    @Keep
    data class Crew(
        @SerializedName("adult") val adult: Boolean,
        @SerializedName("credit_id") val creditId: String,
        @SerializedName("department") val department: String,
        @SerializedName("gender") val gender: Int,
        @SerializedName("id") val id: Long,
        @SerializedName("job") val job: String,
        @SerializedName("known_for_department") val knownForDepartment: String,
        @SerializedName("name") val name: String,
        @SerializedName("original_name") val originalName: String,
        @SerializedName("popularity") val popularity: Double,
        @SerializedName("profile_path") val profilePath: String
    )
}

fun MovieCreditsResponse.Crew.toCreditsModel() = Credits(
    name = name,
    id = id,
    gender = gender,
    profileImgUrl = "${Constants.PROFILE_IMG_BASE_URL}$profilePath",
    department = knownForDepartment,
    characterOrJobName = job
)

fun MovieCreditsResponse.Cast.toCreditsModel() = Credits(
    name = name,
    id = id,
    gender = gender,
    profileImgUrl = "${Constants.PROFILE_IMG_BASE_URL}$profilePath",
    department = knownForDepartment,
    characterOrJobName = character
)
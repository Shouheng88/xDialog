package me.shouheng.xdialogsample.data

import androidx.annotation.IntDef
import me.shouheng.xdialogsample.data.AddressSelectLevel.Companion.LEVEL_AREA
import me.shouheng.xdialogsample.data.AddressSelectLevel.Companion.LEVEL_CITY

/** The max select level of province: city, area, province etc. */
@IntDef(value = [LEVEL_CITY, LEVEL_AREA])
@Target(allowedTargets = [AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER])
annotation class AddressSelectLevel {
    companion object {
        const val LEVEL_CITY                        = 0
        const val LEVEL_AREA                        = 1
    }
}
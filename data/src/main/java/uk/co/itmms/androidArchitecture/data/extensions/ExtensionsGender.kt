package uk.co.itmms.androidArchitecture.data.extensions

import uk.co.itmms.androidArchitecture.domain.entities.Gender

fun String.toGender(): Gender =
    if (this == "male") Gender.Male else Gender.Female
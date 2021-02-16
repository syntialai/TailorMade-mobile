package com.future.tailormade.feature.faceSwap.model

data class FaceLandmarkPosition(

    var top: Float,

    var left: Float? = null,

    var bottom: Float,

    var right: Float? = null,

    var width: Int = 0,

    var height: Int = 0,

    var rotation: Float,

    var minusRotation: Float
)

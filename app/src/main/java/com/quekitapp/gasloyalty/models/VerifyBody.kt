package com.quekitapp.gasloyalty.models

data class VerifyBody(
    val mobile_no: String,
    val payment_amount: String,
    val quantity: String,
    val EventID:String,
    val recovery_code: String
)
package com.quekitapp.gasloyalty.models

data class ScanModel(
    val balance: String,
    val mobile: String,
    val name: String,
    val pk: String,
    val plate_no: String,
    val ssn: String,
    val tag_id: String,
    val valid: String,
    val maintenance_date: String,
    val EventID:String?

)
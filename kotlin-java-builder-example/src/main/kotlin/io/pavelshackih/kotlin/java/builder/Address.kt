package io.pavelshackih.kotlin.java.builder

@SimpleJavaBuilder
data class Address(
        val id: Long,
        val name: String,
        val zip: String?
)
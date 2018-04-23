package io.pavelshackih.kotlin.java.builder

@SimpleJavaBuilder
data class User(
        val id: Long,
        val name: String?,
        val age: Int,
        var address: Address?)
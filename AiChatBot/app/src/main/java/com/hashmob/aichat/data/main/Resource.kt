package com.hashmob.aichat.data.main

class Resource<T> private constructor(val status: Status, val data: T?, val error: Throwable?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    override fun equals(obj: Any?): Boolean {
        if (obj!!.javaClass != javaClass || obj.javaClass != Resource::class.java) {
            return false
        }
        val resource: Resource<*>? = obj as Resource<*>?
        if (resource!!.status != status) {
            return false
        }
        if (data != null) {
            if (resource.data !== data) {
                return false
            }
        }
        return if (error != null) {
            resource.error === error
        } else true
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable): Resource<T> {
            return Resource(Status.ERROR, null, error)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}
package com.laaltentech.abou.myapplication.util

import com.laaltentech.abou.myapplication.di.WebService

class WebServiceHolder {
    private var webservice: WebService? = null

    fun apiService(): WebService? {
        return this.webservice
    }

    fun setAPIService(webservice: WebService) {
        this.webservice = webservice
    }

    companion object {
        internal var webServiceHolder: WebServiceHolder? = null

        val instance: WebServiceHolder
            get() {
                if (webServiceHolder == null) {
                    webServiceHolder = WebServiceHolder()
                }
                return webServiceHolder!!
            }
    }
}
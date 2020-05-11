package com.laaltentech.abou.myapplication.util

class URL_HUB{
    companion object{
        val BASE_URL = "https://subhash-industiries-api.herokuapp.com/api/"
        val PIC_UPLOAD_URL = "https://subhash-industiries-api.herokuapp.com/file"
        val INSERT_GAME_DATA = "game_data"
        val POST_USER_DETAILS = BASE_URL +"users"
        val FETCH_ALL_USER_DETAILS = BASE_URL +"users"
        val TRY_LOGIN = BASE_URL +"login"
        val FETCH_ORDERS = BASE_URL +"orders"
        val FETCH_ORDERS_BY_CUST_ID = BASE_URL +"orders_by_id"
        val INSERT_NEW_ORDERS = BASE_URL +"orders"
        val FETCH_ORDERS_BY_ORDER_ID = BASE_URL +"orders_by_orderID"
        val UPDATE_ORDERS = BASE_URL +"orders"
        val FETCH_PROFILE = BASE_URL +"user"
        val FETCH_IMAGES = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=dd4a16666bdf3c2180b43bec8dd1534a&format=json&nojsoncallback=1&per_page=10&extras=description, license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo, tags, machine_tags, o_dims, views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"
    }

}
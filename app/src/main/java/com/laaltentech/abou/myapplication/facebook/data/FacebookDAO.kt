package com.laaltentech.abou.myapplication.facebook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FacebookDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFacebookProfileData(facebookProfileData: FacebookProfileData)

    @Query("SELECT * FROM FacebookProfileData WHERE id = :userId")
    fun fetchFacebookProfile(userId : String) : LiveData<FacebookProfileData>

    @Query("SELECT * FROM FacebookPageListData")
    fun fetchAllFacebookPageListData() : LiveData<List<FacebookPageListData>>

    @Query("SELECT * FROM FacebookPageData WHERE pageId = :pageID")
    fun fetchAllFacebookPageData( pageID:String) : LiveData<FacebookPageData?>

    @Query("DELETE FROM FacebookPageData")
    fun deleteFacebookPageData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFacebookPageData(facebookPageData: FacebookPageData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllFacebookPageListData(userListData: List<FacebookPageListData>?)
}
package com.ed.turbowash_android.repositories

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class GeneralDatabaseActionsRepo {
    private val storageRef: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    suspend fun uploadImageToFirebaseStorageAwait(pathString: String, bitmap: Bitmap): String {
        val savePath = storageRef.child(pathString)

        val bytesStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytesStream)
        val data = bytesStream.toByteArray()

        // Start the upload task and await its completion
        val uploadTask = savePath.putBytes(data).await()

        // After the upload is complete, await the URL
        val downloadUrl = uploadTask.storage.downloadUrl.await()
        return downloadUrl.toString()
    }
}
package com.nasahapps.sampleflickrapp.api

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID

class FlickrApiTest {

    private val mockPhotosResponse = PhotosResponse(
        photos = FlickrPhotoCollection(
            page = 1,
            pages = 10,
            photo = listOf(
                FlickrPhoto(
                    id = UUID.randomUUID().toString(),
                    secret = UUID.randomUUID().toString(),
                    server = UUID.randomUUID().toString(),
                    title = "Photo Title"
                )
            )
        )
    )

    @Test
    fun api_onRecentPhotosSuccess_shouldReturnPhotosResponse() = runTest {
        val api = mockk<FlickrApi>()
        coEvery { api.getRecentPhotos() } returns mockPhotosResponse

        api.getRecentPhotos()
        coVerify { api.getRecentPhotos() }

        confirmVerified(api)
    }

    @Test
    fun api_onRecentPhotosFailure_shouldThrowException() = runTest {
        val api = mockk<FlickrApi>()
        coEvery { api.getRecentPhotos() } throws Throwable()

        var exceptionThrown = false
        try {
            api.getRecentPhotos()
            coVerify { api.getRecentPhotos() }
        } catch (e: Throwable) {
            exceptionThrown = true
        }
        assert(exceptionThrown)
    }

    @Test
    fun api_onSearchPhotosSuccess_shouldReturnPhotosResponse() = runTest {
        val api = mockk<FlickrApi>()
        coEvery { api.searchForPhotos("query") } returns mockPhotosResponse

        api.searchForPhotos("query")
        coVerify { api.searchForPhotos("query") }

        confirmVerified(api)
    }

    @Test
    fun api_onSearchPhotosFailure_shouldThrowException() = runTest {
        val api = mockk<FlickrApi>()
        coEvery { api.searchForPhotos("query") } throws Throwable()

        var exceptionThrown = false
        try {
            api.searchForPhotos("query")
            coVerify { api.searchForPhotos("query") }
        } catch (e: Throwable) {
            exceptionThrown = true
        }
        assert(exceptionThrown)
    }

}
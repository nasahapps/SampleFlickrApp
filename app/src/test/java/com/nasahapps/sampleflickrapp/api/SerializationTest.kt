package com.nasahapps.sampleflickrapp.api

import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test

class SerializationTest {

    @Test
    fun photosResponse_serializesSuccessfully() {
        val json = """
            {
               "photos": {
                    "page": 1,
                    "pages": 10,
                    "perpage": 100,
                    "total": 1000,
                    "photo": [
                        {
                            "id": "54475307037",
                            "owner": "52357722@N00",
                            "secret": "d223b3f9d3",
                            "server": "65535",
                            "farm": 66,
                            "title": "Spontaneous sprouts",
                            "ispublic": 1,
                            "isfriend": 0,
                            "isfamily": 0,
                            "dateupload": "1745620787"
                        },
                        {
                            "id": "54475307297",
                            "owner": "195122525@N06",
                            "secret": "d490e53131",
                            "server": "65535",
                            "farm": 66,
                            "title": "Tea",
                            "ispublic": 1,
                            "isfriend": 0,
                            "isfamily": 0,
                            "dateupload": "1745620799"
                        },
                        {
                            "id": "54475307662",
                            "owner": "35962850@N05",
                            "secret": "a7b827ac90",
                            "server": "65535",
                            "farm": 66,
                            "title": "Tulip Fields",
                            "ispublic": 1,
                            "isfriend": 0,
                            "isfamily": 0,
                            "dateupload": "1745620815"
                        }
                    ]
               }
            }
        """.trimIndent()
        val jsonParser = Json {
            ignoreUnknownKeys = true
        }

        val actualPhotosResponse = jsonParser.decodeFromString<PhotosResponse>(json)
        val expectedPhotosResponse = PhotosResponse(
            photos = FlickrPhotoCollection(
                page = 1,
                pages = 10,
                photo = listOf(
                    FlickrPhoto(
                        id = "54475307037",
                        secret = "d223b3f9d3",
                        server = "65535",
                        title = "Spontaneous sprouts"
                    ),
                    FlickrPhoto(
                        id = "54475307297",
                        secret = "d490e53131",
                        server = "65535",
                        title = "Tea"
                    ),
                    FlickrPhoto(
                        id = "54475307662",
                        secret = "a7b827ac90",
                        server = "65535",
                        title = "Tulip Fields"
                    )
                )
            )
        )
        Assert.assertEquals(actualPhotosResponse, expectedPhotosResponse)
    }

}
package com.importre.example.photos

import com.importre.example.main.photos.getTotalPage
import org.junit.Test
import kotlin.test.assertEquals

class PhotosTest {

    @Test
    fun testTotalPage() {
        assertEquals(1, getTotalPage(""))
        assertEquals(1, getTotalPage("asdfljasdf"))

        val link = """link:
        <http://jsonplaceholder.typicode.com/photos?_page=1>; rel="first",
        <http://jsonplaceholder.typicode.com/photos?_page=2>; rel="next",
        <http://jsonplaceholder.typicode.com/photos?_page=500>; rel="last"
        """
        assertEquals(500, getTotalPage(link))
    }
}

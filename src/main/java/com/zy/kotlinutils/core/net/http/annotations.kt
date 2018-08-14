package com.zy.kotlinutils.core.net.http

annotation class Query(val name: String)

annotation class Path(val name: String)

annotation class Field(val name: String)

annotation class Body(val name: String = "")

annotation class Header(val name: String)

// http methods

annotation class POST(val path: String = "")

annotation class GET(val path: String = "")

annotation class PUT(val path: String = "")

annotation class DELETE(val path: String = "")
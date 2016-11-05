package org.ar25.androidno.api

class ParseErrorException : Exception {
    constructor() : super() { }
    constructor(message: String) : super(message) { }
    constructor(message: String, cause: Throwable) : super(message, cause) { }
    constructor(cause: Throwable) : super(cause) { }
}



package com.hungry.oauthsample.domain

class Unauthorized(): Exception("Request is Unauthorized")

class Forbidden(): Exception("Request is not allowed")
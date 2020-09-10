package com.myha.coin.utils

class Constant {
    companion object {
        const val AUTH = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJXN3ZYMWVuZEpOZUFGRVhqcEF1NE90S1piVHZ5NjByV1lYTzRtYmlLNVU2M0RlN3lIMyIsImp0aSI6ImQyZjY4ZjMzNzI5N2M0ODg3MmU1MDlmYTM5MWI1MTAzY2ZhMGFjNWYyODg1ZDhjNjU0MTY0OWFhNDAzODRlZDk5Y2E3ZmVlZDgzMjA5NTVkIiwiaWF0IjoxNTk5NzQ5NDUxLCJuYmYiOjE1OTk3NDk0NTEsImV4cCI6MTU5OTc1MzA1MSwic3ViIjoiIiwic2NvcGVzIjpbXX0.bablD9uT4UFncXuR_LWMglKdcgFXUcinEb2jUlcUZjN-aBJ_K8cxpw9LRZeOhd7tpoDLFX-GiKMzmWYnEjOGktlyiEJsfu-2t2w3VA6JciqexoOzPNtKjNy0a2_Oni4qhhjxeR2TJ_Ko1pB7ah4ivpsmSTXhDIgS34mswNTIwdMwZZLyLZr5L5zY2mD2DFAdr7pni8G705S1eI7iwgvuHsxPIxb79LdFR8QojoMIe3m1j7CHxqfP0crgBfMz17Yn0sTuzCkw97SZIy_jpmOkwmvI-OTuJcyVUkYjKzFBZIYyxcNGze1M1fFdjr6HaQgkKY0iDE32JyIURrzWKAglqA"
        private const val PK = "W7vX1endJNeAFEXjpAu4OtKZbTvy60rWYXO4mbiK5U63De7yH3"
        private const val SK = "Ssxw7nypH6yBYqYZKuHlH2DHkdqLEPm6p6lHyvQe"
        const val GENERATE_TOKEN =
            "curl -d \"grant_type=client_credentials&client_id=$PK&client_secret=$SK\" https://api.petfinder.com/v2/oauth2/token"
    }
}
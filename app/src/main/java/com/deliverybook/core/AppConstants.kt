package com.deliverybook.core

object AppConstants {

    object Db {
        const val NAME = "delivery_book.db"
    }

    object Navigation {
        const val ROUTE_CONTACTS_LIST = "contacts_list"
        const val ROUTE_CONTACT_DETAIL = "contact_detail"
        const val ARG_DNI = "dni"
    }

    object Paging {
        const val PAGE_SIZE = 20
    }

    object Search {
        const val MIN_LENGTH = 3
        const val RECENT_LIMIT = 30
    }

    object Splash {
        const val MIN_DISPLAY_MS = 2_000L
    }
}

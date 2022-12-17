package com.mth.example.socialmediaapp.ui.activities

import com.mth.example.socialmediaapp.model.Status

interface MoveToComment {
    public fun initComentEvent(status: Status): Unit
}